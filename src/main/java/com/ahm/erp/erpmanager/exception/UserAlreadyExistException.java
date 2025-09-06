package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class UserAlreadyExistException extends BusinessException {
    public UserAlreadyExistException() {
        super(ResponseCode.USER_EXISTS_EXCEPTION);
    }
}
