package com.japan.compass.annotation.exception;

import lombok.Getter;

// 外部システムエラーのようなアプリケーションで対処しようがないエラーで利用
@Getter
public class SystemException extends RuntimeException {

    private final Errors error;

    public SystemException(Errors error) {
        this.error = error;
    }

    public SystemException(Errors error, Throwable e) {
        super(e);
        this.error = error;
    }

    public SystemException(Errors error, String message) {
        super(message);
        this.error = error;
    }
}
