package com.ordriva.warehouses.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouses", indexes = {
        @Index(name = "idx_warehouses_code", columnList = "code"),
        @Index(name = "idx_warehouses_active", columnList = "active")
})
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false)
    private boolean active = true;

    protected Warehouse() {
    }

    public Warehouse(String name, String code, String city, String state, String country, boolean active) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.state = state;
        this.country = country;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public boolean isActive() { return active; }

    public void update(String name, String code, String city, String state, String country, boolean active) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.state = state;
        this.country = country;
        this.active = active;
    }
}