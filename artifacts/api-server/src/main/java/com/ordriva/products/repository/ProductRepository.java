package com.ordriva.products.repository;

import com.ordriva.products.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            select p from Product p
            where (:search = '' or lower(p.sku) like lower(concat('%', :search, '%'))
                or lower(p.name) like lower(concat('%', :search, '%')))
              and (:active is null or p.active = :active)
            """)
    Page<Product> search(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);

    Optional<Product> findBySkuIgnoreCase(String sku);
}