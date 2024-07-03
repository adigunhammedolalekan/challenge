package com.starlingapp.roundup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RestClientResponseException.class, HttpServerErrorException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleResponseStatusException(Exception exception) {
        var message = exception.getMessage();
        var code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (exception instanceof RestClientResponseException) {
            code = ((RestClientResponseException) exception).getStatusCode().value();
        }
        if (exception instanceof HttpServerErrorException) {
            code = ((HttpServerErrorException) exception).getStatusCode().value();
        }
        return new ResponseEntity<>(new ErrorResponse(code, message), HttpStatus.valueOf(code));
    }
}
