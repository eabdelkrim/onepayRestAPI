package com.example.onepay.repository;

import com.example.onepay.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderRepository extends JpaRepository<OrderItem, Integer> {
}
