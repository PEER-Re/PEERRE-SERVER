package org.umc.peerre.global.error.exception;


import org.umc.peerre.global.error.ErrorCode;

public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}