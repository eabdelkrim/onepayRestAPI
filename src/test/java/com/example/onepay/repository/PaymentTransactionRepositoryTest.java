package com.example.onepay.repository;

import com.example.onepay.domain.OrderItem;
import com.example.onepay.domain.PaymentMethodType;
import com.example.onepay.domain.PaymentStatus;
import com.example.onepay.domain.PaymentTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PaymentTransactionRepositoryTest {

    @Autowired PaymentTransactionRepository paymentTransactionRepository;

    @Test
    public void should_find_transaction(){
        PaymentTransaction actual = new PaymentTransaction(null, BigDecimal.valueOf(112.87d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        actual.addOrderItem(new OrderItem(null,"LAPTOP", 2, BigDecimal.valueOf(10),null));
        actual.addOrderItem(new OrderItem(null,"CARTRIDGE", 1, BigDecimal.valueOf(10),null));
        actual.addOrderItem(new OrderItem(null,"FLASH USB", 5, BigDecimal.valueOf(10),null));
        actual.addOrderItem(new OrderItem(null,"EXTERNAL HARD DRIVE", 4, BigDecimal.valueOf(10),null));

        paymentTransactionRepository.save(actual);
        Optional<PaymentTransaction> expected = paymentTransactionRepository.findById(2);

        Assertions.assertTrue(expected.isPresent());
        Assertions.assertNotNull(expected.get());
        Assertions.assertEquals(actual.getId(), expected.get().getId());
    }

    @Test
    public void should_save_transaction(){
        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(112.87d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        orderItemList.add(new OrderItem(1, "LAPTOP", 2, BigDecimal.valueOf(10), actual));
        orderItemList.add(new OrderItem(2, "CARTRIDGE", 1, BigDecimal.valueOf(10), actual));
        orderItemList.add(new OrderItem(3, "FLASH USB", 5, BigDecimal.valueOf(10), actual));
        orderItemList.add(new OrderItem(4, "EXTERNAL HARD DRIVE", 4, BigDecimal.valueOf(10), actual));
        actual.setOrderItems(orderItemList);

        PaymentTransaction expected = paymentTransactionRepository.save(actual);

        Assertions.assertEquals(actual.getId(), expected.getId());
    }

    @Test
    public void should_update_existing_transaction(){
        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(112.87d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        orderItemList.add(new OrderItem(1, "LAPTOP", 2, BigDecimal.valueOf(10), actual));
        orderItemList.add(new OrderItem(2, "CARTRIDGE", 1, BigDecimal.valueOf(10), actual));
        orderItemList.add(new OrderItem(3, "FLASH USB", 5, BigDecimal.valueOf(10), actual));
        orderItemList.add(new OrderItem(4, "EXTERNAL HARD DRIVE", 4, BigDecimal.valueOf(10), actual));
        actual.setOrderItems(orderItemList);

        PaymentTransaction expected = paymentTransactionRepository.save(actual);

        actual.setAmount(BigDecimal.valueOf(200));

        expected = paymentTransactionRepository.save(actual);

        Assertions.assertEquals(BigDecimal.valueOf(200), expected.getAmount());
    }
}
