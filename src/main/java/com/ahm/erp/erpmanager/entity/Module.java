package com.ahm.erp.erpmanager.entity;

import com.ahm.erp.erpmanager.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "MODULE")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String icon;
    private String description;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Double price;
    private String tag;
    private String version;
    private String categories;

}
