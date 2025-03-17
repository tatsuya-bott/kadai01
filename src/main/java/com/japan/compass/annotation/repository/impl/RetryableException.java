package com.japan.compass.annotation.repository.impl;

class RetryableException extends RuntimeException {

    RetryableException(Throwable cause) {
        super(cause);
    }
}
