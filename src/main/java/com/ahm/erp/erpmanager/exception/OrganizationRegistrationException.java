package com.ahm.erp.erpmanager.exception;

import com.ahm.erp.erpmanager.enums.ResponseCode;

public class OrganizationRegistrationException extends TechnicalException {
    public OrganizationRegistrationException() {
        super(ResponseCode.ORGANIZATION_INSERT_EXCEPTION);
    }
}
