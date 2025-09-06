package com.ahm.erp.erpmanager.controller;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.enums.ResponseCode;
import org.springframework.http.ResponseEntity;

public interface ResponseHandler {

    default <T> ResponseEntity<Response<T>> ok(T response) {
        return ResponseEntity.ok(Response.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(response)
                .build());
    }
}
