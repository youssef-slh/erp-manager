package com.ahm.erp.erpmanager.dto;

import com.ahm.erp.erpmanager.enums.Currency;

public record ModuleCreationRequest(String name, String icon, String description, Currency currency, Double price,
                                    String version, String tag, String categories) {
}
