package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class TechnicalException extends RuntimeException{
    public TechnicalException(ResponseCode responseCode) {
        super(responseCode.getMessage());
    }
}
