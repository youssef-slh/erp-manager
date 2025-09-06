package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class InvalidRequestException extends BusinessException {
    public InvalidRequestException() {
        super(ResponseCode.INVALID_REQUEST);
    }
}
