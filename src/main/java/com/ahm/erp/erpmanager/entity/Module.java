package com.ahm.erp.erpmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="MODULE")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String icon;
    private String description;
}
