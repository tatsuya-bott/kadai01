package com.japan.compass.annotation.exception;

import lombok.Getter;

// エラーごとにレスポンスを変えたいなど、内部でハンドリングしたいエラーで利用
@Getter
public class ApplicationException extends Exception {

    private final Errors error;

    public ApplicationException(Errors error) {
        this.error = error;
    }
}
