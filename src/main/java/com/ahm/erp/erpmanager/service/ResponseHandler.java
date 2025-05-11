package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.Response;

public interface ResponseHandler {
    default Response success(){
        return Response.builder()
                .code("0")
                .message("success").build();
    }
}
