package com.project.common.exception.handler;

import com.project.common.exception.helper.ValidationExceptionHelper;
import com.project.common.response.ApiRes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiRes<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiRes<Object> response = new ApiRes<>();

        return ValidationExceptionHelper.methodArgumentNotValidExceptionHandle(response, e);
    }

}