package org.farmsystem.sotserver.global.error.exception;

import org.farmsystem.sotserver.global.error.ErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}