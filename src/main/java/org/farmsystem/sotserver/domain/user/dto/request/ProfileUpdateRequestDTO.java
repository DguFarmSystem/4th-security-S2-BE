package org.farmsystem.sotserver.domain.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProfileUpdateRequestDTO(
    MultipartFile profileImage,
    String introduction,
    List<String> talent,
    List<String> skill
) {
}
