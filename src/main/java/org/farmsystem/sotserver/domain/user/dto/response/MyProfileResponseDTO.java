package org.farmsystem.sotserver.domain.user.dto.response;

import org.farmsystem.sotserver.domain.user.entity.User;

import java.util.List;

public record MyProfileResponseDTO(
        Long userId,
        String imageUrl,
        String nickname,
        String email,
        String introduction,
        List<String> talents,
        List<String> skills
) {
    public static MyProfileResponseDTO from(User user) {
        return new MyProfileResponseDTO(
                user.getUserId(),
                user.getImageUrl(),
                user.getNickname(),
                user.getEmail(),
                user.getIntroduction(),
                user.getParsedList(user.getTalents()),
                user.getParsedList(user.getSkills())
        );
    }
}
