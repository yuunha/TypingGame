package hello.typing_game_be.user.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;


import hello.typing_game_be.user.dto.PublicUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Template s3Template;

    @Transactional
    public void update(Long userId, String name) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.setNickname(name);
    }

    /**
     * 유저 정보 조회 + 서명된 URL 생성
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String profileImageUrl = null;
        if (user.getProfileImageKey() != null) {
            // 서명된 URL 생성, 만료 60분
            profileImageUrl = s3Template.createSignedGetURL(bucket, user.getProfileImageKey(), Duration.ofMinutes(30)).toString();;
        }

        return UserResponse.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .profileImageUrl(profileImageUrl)
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

    @Transactional(readOnly = true)
    public PublicUserResponse getPublicProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String profileImageUrl = null;
        if (user.getProfileImageKey() != null) {
            profileImageUrl = s3Template
                    .createSignedGetURL(bucket, user.getProfileImageKey(), Duration.ofMinutes(30))
                    .toString();
        }

        return PublicUserResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }

    @Transactional
    public void registerNickname(Long userId,String nickname){
        // 중복 체크
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.setNickname(nickname);
    }

    @Transactional
    public void deleteUserByUserId(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }else{
            userRepository.deleteByUserId(userId);
        }
    }

    //TODO: 리팩토링 필요
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            System.out.println("해당 userId " + userId+ "가 존재하지 않습니다.");
            return new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        });
    }

    public Page<UserResponse> searchUsersByNickname(String nickname, Pageable pageable) {
        Page<User> usersPage = userRepository.findByNicknameContainingIgnoreCase(nickname, pageable);

        return usersPage.map(user -> {
            String profileImageUrl = null;
            if (user.getProfileImageKey() != null) {
                profileImageUrl = s3Template.createSignedGetURL(
                    bucket, user.getProfileImageKey(), Duration.ofMinutes(30))
                    .toString();
            }

            return UserResponse.builder()
                .nickname(user.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
        });
    }


    // 프로필 업로드
    @Transactional
    public String uploadProfileImage(MultipartFile file, Long userId) throws IOException {

        //유저 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 기존 프로필 이미지가 있다면 삭제
        if(user.getProfileImageKey() != null){
            deleteProfileImage(userId);
        }

        // 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // S3 key 설정 (username 기준, 확장자 포함)
        String key = "profile/" + userId + extension;

        // ObjectMetadata 설정
        ObjectMetadata metadata = ObjectMetadata.builder()
            .contentType(file.getContentType())
            .contentLength(file.getSize())
            .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Template.upload(bucket, key, inputStream, metadata);
        }

        // DB에 S3 key 저장
        user.setProfileImageKey(key);

        return key; // 필요시 key 반환
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //s3 객체 삭제
        s3Template.deleteObject(bucket, user.getProfileImageKey());

        //user객체의 profileImageKey 제거
        user.setProfileImageKey(null);
    }
}
