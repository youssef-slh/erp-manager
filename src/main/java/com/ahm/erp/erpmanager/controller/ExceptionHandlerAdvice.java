package com.ahm.erp.erpmanager.controller;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Response> baseResponseHandler(BaseException exception) {
        log.error("Failure : {}", exception.getMessage());
        return ResponseEntity.badRequest()
                .body(Response.builder()
                        .code(exception.getCode().getCode())
                        .message(exception.getCode().getMessage())
                        .build());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exceptionHandling(Exception exception) {
        log.error("Internal Server Error : {}", exception.getMessage());
        return ResponseEntity.internalServerError()
                .body(Response.builder()
                        .code("500")
                        .message(exception.getMessage())
                        .build());

    }
}
