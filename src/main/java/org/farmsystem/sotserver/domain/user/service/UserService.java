package org.farmsystem.sotserver.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.farmsystem.sotserver.domain.user.dto.request.ProfileUpdateRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.request.UserLoginRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.response.UserTokenResponseDTO;
import org.farmsystem.sotserver.domain.user.entity.Role;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.farmsystem.sotserver.global.config.auth.jwt.JwtProvider;
import org.farmsystem.sotserver.global.error.exception.BusinessException;
import org.farmsystem.sotserver.global.error.exception.EntityNotFoundException;
import org.farmsystem.sotserver.global.s3.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.farmsystem.sotserver.global.error.ErrorCode.FILE_UPLOAD_FAILED;
import static org.farmsystem.sotserver.global.error.ErrorCode.USER_NOT_FOUND;


@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final S3Uploader s3Uploader;

    public User saveUser(String socialId, String imageUrl, UserLoginRequestDTO request) {
        return userRepository.findBySocialId(socialId)
                .orElseGet(() -> userRepository.save(
                        // 동국대학교 인증 전 기본 권한은 ROLE_PENDING
                        new User(socialId, imageUrl, Role.ROLE_PENDING)
                ));
    }

    public UserTokenResponseDTO issueTempToken(Long userId) {
        String accessToken = jwtProvider.getIssueToken(userId, true);
        return UserTokenResponseDTO.of(accessToken);
    }

    // 마이페이지 수정
    public void updateProfile(Long userId, ProfileUpdateRequestDTO profileUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        MultipartFile image = profileUpdateRequest.profileImage();
        String introduction = profileUpdateRequest.introduction();
        List<String> skills = Optional.ofNullable(profileUpdateRequest.skill()).orElse(List.of());
        List<String> talents = Optional.ofNullable(profileUpdateRequest.talent()).orElse(List.of());

        if (image != null && !image.isEmpty() ) {user.updateProfileImage(uploadProfileImage(image));}
        if (introduction != null) {user.updateIntroduction(introduction);}
        // 리스트를 콤마로 구분하여 문자열로 변환
        if (!skills.isEmpty()) {user.updateSkills(String.join(",", skills));}
        if (!talents.isEmpty()) {user.updateTalents(String.join(",", talents));}
    }

    private String uploadProfileImage(MultipartFile profileImage) {
        try {
            String key = s3Uploader.upload(profileImage, "profile-images");
            return s3Uploader.getFileUrl(key);
        } catch (IOException e) {
            throw new BusinessException(FILE_UPLOAD_FAILED);
        }
    }
}
