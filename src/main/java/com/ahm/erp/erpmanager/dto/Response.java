package com.ahm.erp.erpmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    private String code;
    private String message;
    private T data;
}
