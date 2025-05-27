package org.farmsystem.sotserver.domain.form.dto.request;

import org.farmsystem.sotserver.domain.form.entity.FormStatus;

public record FormStatusRequestDTO (
    Long applicationId,
    FormStatus formStatus
){
}
