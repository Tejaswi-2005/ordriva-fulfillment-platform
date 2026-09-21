package com.ordriva.warehouses.repository;

import com.ordriva.warehouses.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("""
            select w from Warehouse w
            where (:search = '' or lower(w.name) like lower(concat('%', :search, '%'))
                or lower(w.code) like lower(concat('%', :search, '%'))
                or lower(w.city) like lower(concat('%', :search, '%')))
              and (:active is null or w.active = :active)
            """)
    Page<Warehouse> search(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);
}