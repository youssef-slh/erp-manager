package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class OrganizationAlreadyExistException extends BusinessException {
    public OrganizationAlreadyExistException() {
        super(ResponseCode.ORGANIZATION_EXISTS_EXCEPTION);
    }
}
