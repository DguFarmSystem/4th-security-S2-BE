package org.farmsystem.sotserver.domain.article.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;

@Getter
@NoArgsConstructor
public class ArticleStatusRequest {
    private ArticleStatus status;
}
