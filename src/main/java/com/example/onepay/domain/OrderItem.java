package com.example.onepay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_TRANSACTION_ID", nullable = false)
    @JsonIgnore
    private PaymentTransaction paymentTransaction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && Objects.equals(id, orderItem.id) && Objects.equals(productName, orderItem.productName) && Objects.equals(price, orderItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, quantity, price);
    }
}
