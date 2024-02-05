package org.umc.peerre.global.error.exception;

import org.umc.peerre.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}