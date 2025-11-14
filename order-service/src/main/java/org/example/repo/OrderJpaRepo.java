package org.example.repo;

import org.example.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderJpaRepo extends JpaRepository<Order, Long> {

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "update orders set status = :status where id = :orderId"
    )
    void updateStatus(@Param("orderId")Long orderId, @Param("status") String status);
}
