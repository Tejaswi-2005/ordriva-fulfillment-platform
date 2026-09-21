package com.ordriva.common.domain;

public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    INVENTORY_RESERVING,
    PAYMENT_PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    FAILED
}