package com.ahm.erp.erpmanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TR_USER_MODULE")
public class UserModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ElementCollection
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @Column(name = "active")
    private boolean active;
}
