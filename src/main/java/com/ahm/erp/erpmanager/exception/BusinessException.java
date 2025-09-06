package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class BusinessException extends BaseException{
    public BusinessException(ResponseCode responseCode) {
        super(responseCode);
    }
}
