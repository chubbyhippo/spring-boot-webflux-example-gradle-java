package com.example.movieinfoservice.interfaces.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleRequestBodyError(WebExchangeBindException exception) {
        log.error("Exception caught in handleRequestBodyError : {}", exception.getMessage());
        var error = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(objectError -> Objects.requireNonNullElse(objectError.getDefaultMessage(), ""))
                .sorted()
                .collect(Collectors.joining(","));

        log.error("Error is : {}", error);
        return error;

    }

}
