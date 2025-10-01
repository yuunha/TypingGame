package hello.typing_game_be.user.controller;


import java.io.IOException;

import hello.typing_game_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.dto.UserProfileResponse;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.dto.UserUpdateRequest;

import hello.typing_game_be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

//    @Operation( summary = "회원정보(닉네임) 수정", responses = {
//        @ApiResponse(responseCode = "200", description = "회원정보(이름) 수정 성공"),
//        @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
//    })
//    @PutMapping("/user/{id}")
//    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest request
//    ) {
//        userService.update(id,request.getUsername());
//        return ResponseEntity.ok().build();
//    }


    @Operation( summary = "회원정보 조회", responses = {
        @ApiResponse(responseCode = "200", description = "회원정보 조회 성공"),
        @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    })
    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(
                new UserResponse(user.getUserId(),user.getNickname(),user.getProfileImageKey())
        );
    }


    @Operation( summary = "회원 삭제", responses = {
        @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    })
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        userService.deleteUserByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation( summary = "nickname으로 유저 검색", description = "GET /users?username=홍길&page=0&size=5", responses = {
            @ApiResponse(responseCode = "200", description = "회원정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    })
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> searchUsers(
        @RequestParam String username,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nickname").ascending());
        Page<UserResponse> users = userService.searchUsersByNickname(username,pageable);
        return ResponseEntity.ok(users);
    }


    @Operation( summary = "프로필 사진 업로드", description = "content-type : multipart/form-data, 속성이름 : file ", responses = {
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공, 사진 url 반환 ")
    })
    @PostMapping("/user/profile")
    public ResponseEntity<UserProfileResponse> uploadFile(
        @RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
            String key = userService.uploadProfileImage(file, userDetails.getUserId()); // username = loginId
            return ResponseEntity.ok(new UserProfileResponse(key));
    }

    @Operation( summary = "프로필 사진 삭제", responses = {
        @ApiResponse(responseCode = "200", description = "파일 삭제 성공")
    })
    @DeleteMapping("/user/profile")
    public ResponseEntity<Void> deleteFile(@AuthenticationPrincipal CustomUserDetails userDetails){
        userService.deleteProfileImage(userDetails.getUserId()); // username = loginId
        return ResponseEntity.ok().build();
    }

}



