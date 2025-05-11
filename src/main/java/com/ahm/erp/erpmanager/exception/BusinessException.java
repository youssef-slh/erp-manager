package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class BusinessException extends RuntimeException{
    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
    }
}
