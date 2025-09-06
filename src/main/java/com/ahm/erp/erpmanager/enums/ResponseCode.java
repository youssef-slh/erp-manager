package com.ahm.erp.erpmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS("0","success",HttpStatus.OK),
    ORGANIZATION_INSERT_EXCEPTION("1","unable to save organization record to database",HttpStatus.INTERNAL_SERVER_ERROR),
    ORGANIZATION_EXISTS_EXCEPTION("2","organization already exists",HttpStatus.BAD_REQUEST),
    USER_EXISTS_EXCEPTION("3","user already exists",HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("4","request is invalid",HttpStatus.BAD_REQUEST),
    MODULE_NOT_FOUND_EXCEPTION("5","module not found",HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
