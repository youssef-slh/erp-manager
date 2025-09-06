package com.ahm.erp.erpmanager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {

    USD("$");

    private final String symbol;
}
