package org.umc.peerre.global.error.exception;

import org.umc.peerre.global.error.ErrorCode;

public class TemporaryException extends BusinessException{
    public TemporaryException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public TemporaryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
