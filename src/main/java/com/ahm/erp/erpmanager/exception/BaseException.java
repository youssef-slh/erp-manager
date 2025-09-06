package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;
import lombok.Data;

@Data
public class BaseException extends RuntimeException {

    ResponseCode code;

    public BaseException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public BaseException(String message) {
        super(message);
    }
}
