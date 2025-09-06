package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class ModuleNotFoundException extends BusinessException {
    public ModuleNotFoundException() {
        super(ResponseCode.MODULE_NOT_FOUND_EXCEPTION);
    }
}
