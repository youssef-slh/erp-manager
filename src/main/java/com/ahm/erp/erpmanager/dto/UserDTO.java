package com.ahm.erp.erpmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserDTO(String id,
                      String firstName,
                      String lastName,
                      String username,
                      String email,
                      @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
                      LocalDateTime creationDate) {
}
