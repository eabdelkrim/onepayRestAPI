package com.example.onepay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_METHOD_TYPE")
    private PaymentMethodType paymentMethodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private PaymentStatus status;

    @OneToMany(mappedBy = "paymentTransaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @JsonIgnore
    public void addOrderItem(OrderItem orderItem) {

        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }

        orderItems.add(orderItem);
        orderItem.setPaymentTransaction(this);
    }

    @JsonIgnore
    public boolean isNew() {
        return PaymentStatus.NEW.equals(status);
    }

    @JsonIgnore
    public boolean isCaptured() {
        return PaymentStatus.CAPTURED.equals(status);
    }

    public boolean isChanged(PaymentTransaction paymentTransaction){
        for(OrderItem orderItem : orderItems){
            if(!paymentTransaction.getOrderItems().contains(orderItem)){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentTransaction that = (PaymentTransaction) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount) && paymentMethodType == that.paymentMethodType && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, paymentMethodType, status);
    }
}
