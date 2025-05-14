package org.farmsystem.sotserver.global.error.exception;

import org.farmsystem.sotserver.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}