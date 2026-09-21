package com.ordriva.fulfillment.repository;

import com.ordriva.fulfillment.domain.Fulfillment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FulfillmentRepository extends JpaRepository<Fulfillment, Long> {
}