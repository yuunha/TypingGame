package hello.typing_game_be.user.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Template s3Template;

    //회원가입
    @Transactional
    public Long register(UserCreateRequest request) {

        // loginId 중복 검증

        if (userRepository.existsByLoginId(request.getLoginId())) { //중복 유저 검증
            throw new BusinessException(ErrorCode.DUPLICATE_LOGINID);
        }

        // username 중복 검증
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }

        User user = User.builder()
            .username(request.getUsername())
            .loginId(request.getLoginId())
            .password(passwordEncoder.encode(request.getPassword())) // 인코딩
            .build();
        userRepository.save(user);

        return user.getUserId();
    }

    // public void login(UserRequest request) {
    //     // System.out.println("loginId: " + request.getLoginId()+"password"+request.getPassword());
    //     // System.out.println("존재여부"+userRepository.existsByLoginId(request.getLoginId()));
    //     // userRepository.findByLoginId(request.getLoginId()).ifPresent(u -> {
    //     //     System.out.println("DB 저장된 password = " + u.getPassword());
    //     //     System.out.println("matches 결과 = " + passwordEncoder.matches(request.getPassword(), u.getPassword()));
    //     // });
    //
    //     // 1. 아이디 존재 여부 확인
    //     User user = userRepository.findByLoginId(request.getLoginId())
    //         .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    //
    //     // 2. 비밀번호 일치 여부 확인
    //     if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    //         throw new BusinessException(ErrorCode.INVALID_PASSWORD);
    //     }
    // }
    @Transactional
    public void update(Long userId, String name) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.setUsername(name);
    }

    public UserResponse getUserByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return new UserResponse(user.getUserId(),user.getLoginId(), user.getUsername());
    }

    @Transactional
    public void deleteUserByLoginId(String loginId) {
        if(!userRepository.existsByLoginId(loginId)){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }else{
            userRepository.deleteByLoginId(loginId);
        }
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            System.out.println("해당 userId " + userId+ "가 존재하지 않습니다.");
            return new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        });
    }

    public List<UserResponse> searchUsersByUsername(String username, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username,pageable).stream()
            .map(UserResponse::fromEntity)
            .toList();
    }


    // 프로필 업로드
    @Transactional
    public String uploadProfileImage(MultipartFile file, String username) throws IOException {

        //유저 조회
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // TODO: 기존 프로필 이미지가 있다면 삭제

        // 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // S3 key 설정 (username 기준, 확장자 포함)
        String key = "profile/" + username + extension;

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
        userRepository.save(user);

        return key; // 필요시 key 반환
    }

}
