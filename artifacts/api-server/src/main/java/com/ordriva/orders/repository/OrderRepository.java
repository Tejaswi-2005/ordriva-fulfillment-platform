package com.ordriva.orders.repository;

import com.ordriva.common.domain.OrderStatus;
import com.ordriva.orders.domain.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    @Query("""
            select distinct o from CustomerOrder o
            join fetch o.user u
            join fetch o.warehouse w
            where (:search = '' or lower(o.orderNumber) like lower(concat('%', :search, '%'))
                or lower(u.name) like lower(concat('%', :search, '%')))
              and (:status is null or o.status = :status)
              and (:warehouseId is null or w.id = :warehouseId)
            """)
    Page<CustomerOrder> search(
            @Param("search") String search,
            @Param("status") OrderStatus status,
            @Param("warehouseId") Long warehouseId,
            Pageable pageable
    );

    @Query("select distinct o from CustomerOrder o join fetch o.user join fetch o.warehouse left join fetch o.items i left join fetch i.product where o.id = :id")
    Optional<CustomerOrder> findDetailedById(@Param("id") Long id);

    Optional<CustomerOrder> findByOrderNumber(String orderNumber);
}