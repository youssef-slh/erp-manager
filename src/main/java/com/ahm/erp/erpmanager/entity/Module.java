package com.ahm.erp.erpmanager.entity;

import com.ahm.erp.erpmanager.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "ORGANIZATION_MODULE",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Set<Organization> organizations = new HashSet<>();

    @ManyToMany(mappedBy = "modules")
    private Set<User> users = new HashSet<>();
}
