package com.japan.compass.annotation.controller;

import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public ModelAndView unhandledException(Exception ex) {
        log.error("unhandled Exception.", ex);
        return errorPage(HttpStatus.INTERNAL_SERVER_ERROR, Errors.INTERNAL_SERVER_ERROR.getCode());
    }

    @ExceptionHandler({ SystemException.class })
    public ModelAndView handleSystemException(SystemException ex) {
        log.error("system exception. " + ex.getError().toString(), ex);
        return errorPage(ex.getError());
    }

    @ExceptionHandler({ ApplicationException.class })
    public ModelAndView handleApplicationException(ApplicationException ex) {
        log.warn("application exception. " + ex.getError().toString());
        return errorPage(ex.getError());
    }

    // 必須のリクエストパラメータがない場合
    @ExceptionHandler({ MissingServletRequestParameterException.class })
    public ModelAndView handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.info("missing servlet request parameter exception.", ex);
        return errorPage(Errors.INVALID_REQUEST);
    }

    // リクエストバリデーションエラー
    @ExceptionHandler({ ConstraintViolationException.class })
    public ModelAndView handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String errorMessage = "contraint violation exception.";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" " + violation.getMessage()));
            errorMessage += builder.toString();
        }
        log.info(errorMessage);
        return errorPage(Errors.INVALID_REQUEST);
    }

    // リクエストパラメータの型が異なる場合
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("method argument type mismatch exception.", ex);
        return errorPage(Errors.INVALID_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.info("max upload size exceeded exception.", ex);
        return errorPage(Errors.FILE_UPLOAD_SIZE_EXCEED);
    }

    private ModelAndView errorPage(HttpStatus status, String code) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        mav.setStatus(status);
        mav.addObject("errorCode", code);
        return mav;
    }

    private ModelAndView errorPage(Errors error) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        mav.setStatus(error.getStatus());
        mav.addObject("errorCode", error.getCode());
        return mav;
    }
}
