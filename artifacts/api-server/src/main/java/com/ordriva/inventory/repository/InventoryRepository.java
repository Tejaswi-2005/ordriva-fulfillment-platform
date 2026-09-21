package com.ordriva.inventory.repository;

import com.ordriva.inventory.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("""
            select i from Inventory i
            join fetch i.product p
            join fetch i.warehouse w
            where (:productId is null or p.id = :productId)
              and (:warehouseId is null or w.id = :warehouseId)
              and (:search = '' or lower(p.name) like lower(concat('%', :search, '%'))
                or lower(p.sku) like lower(concat('%', :search, '%'))
                or lower(w.code) like lower(concat('%', :search, '%')))
            """)
    Page<Inventory> search(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId,
            @Param("search") String search,
            Pageable pageable
    );

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}