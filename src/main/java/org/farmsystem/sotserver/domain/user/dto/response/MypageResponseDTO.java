package org.farmsystem.sotserver.domain.user.dto.response;

import org.farmsystem.sotserver.domain.article.dto.response.MyArticleResponseDTO;
import org.farmsystem.sotserver.domain.form.dto.response.MyApplicationResponseDTO;
import org.farmsystem.sotserver.domain.form.dto.response.MyApplicationResultResponseDTO;

import java.util.List;

public record MypageResponseDTO(
        MyProfileResponseDTO myProfile,
        List<MyApplicationResultResponseDTO> myApplicationResults,
        List<MyApplicationResponseDTO> myApplications,
        List<MyArticleResponseDTO> myArticles
) {
    public static MypageResponseDTO of( MyProfileResponseDTO myProfile, List<MyApplicationResultResponseDTO> myApplicationResults,
                                        List<MyApplicationResponseDTO> myApplications, List<MyArticleResponseDTO> myArticles){
        return new MypageResponseDTO(
                myProfile,
                myApplicationResults,
                myApplications,
                myArticles
        );
    }
}
