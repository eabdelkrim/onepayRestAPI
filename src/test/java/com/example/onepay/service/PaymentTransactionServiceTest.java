package com.example.onepay.service;

import com.example.onepay.domain.OrderItem;
import com.example.onepay.domain.PaymentMethodType;
import com.example.onepay.domain.PaymentStatus;
import com.example.onepay.domain.PaymentTransaction;
import com.example.onepay.exception.BusinessException;
import com.example.onepay.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceTest {

    @InjectMocks
    PaymentTransactionService paymentTransactionService;

    @Mock
    PaymentTransactionRepository paymentTransactionRepository;

    @Test
    public void should_create_payment_when_status_is_new() {
        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());

        Mockito.when(paymentTransactionRepository.save(Mockito.any(PaymentTransaction.class))).thenReturn(actual);

        PaymentTransaction expected = paymentTransactionService.save(actual);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void should_throw_exception_for_new_payment_when_status_different_from_new() {

        PaymentTransaction actual = new PaymentTransaction(null, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.CAPTURED, new ArrayList<>());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> paymentTransactionService.save(actual));

        Assertions.assertEquals("A new transaction must be in the \"NEW\" state", exception.getMessage());

    }

    @Test
    public void should_retrieve_valid_transaction_when_it_created() {
        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(112.87d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        actual.addOrderItem(new OrderItem(1, "LAPTOP", 2, BigDecimal.valueOf(10), null));
        actual.addOrderItem(new OrderItem(2, "CARTRIDGE", 1, BigDecimal.valueOf(10), null));
        actual.addOrderItem(new OrderItem(3, "FLASH USB", 5, BigDecimal.valueOf(10), null));
        actual.addOrderItem(new OrderItem(4, "EXTERNAL HARD DRIVE", 4, BigDecimal.valueOf(10), null));

        Mockito.when(paymentTransactionRepository.findById(Mockito.any())).thenReturn(Optional.of(actual));

        PaymentTransaction expected = paymentTransactionService.findById(1);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void should_throw_exception_when_update_non_authorized_transaction_to_captured() {

        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.CAPTURED, new ArrayList<>());
        PaymentTransaction previous = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> paymentTransactionService.save(actual));

        Assertions.assertEquals("A new transaction must be in the \"NEW\" state", exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_update_captured_status() {

        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        PaymentTransaction previous = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.CAPTURED, new ArrayList<>());
        Mockito.when(paymentTransactionRepository.findById(Mockito.any())).thenReturn(Optional.of(previous));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> paymentTransactionService.save(actual));

        Assertions.assertEquals("it is not possible to modify the status of a \"CAPTURED\" transaction", exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_update_transaction_order_list() {

        List<OrderItem> orderList1 = new ArrayList<OrderItem>();
        orderList1.add(new OrderItem(1, "LAPTOP", 2, BigDecimal.valueOf(10), null));
        orderList1.add(new OrderItem(2, "CARTRIDGE", 1, BigDecimal.valueOf(10), null));
        List<OrderItem> orderList2 = new ArrayList<OrderItem>();
        orderList2.add(new OrderItem(1, "LAPTOP", 1, BigDecimal.valueOf(10), null));
        orderList2.add(new OrderItem(2, "CARTRIDGE", 10, BigDecimal.valueOf(10), null));
        orderList2.add(new OrderItem(3, "FLASH USB", 5, BigDecimal.valueOf(10), null));
        orderList2.add(new OrderItem(4, "EXTERNAL HARD DRIVE", 4, BigDecimal.valueOf(10), null));
        PaymentTransaction actual = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.CAPTURED, orderList2);
        PaymentTransaction previous = new PaymentTransaction(1, BigDecimal.valueOf(110.12d), PaymentMethodType.CREDIT_CARD, PaymentStatus.CAPTURED, orderList1);
        Mockito.when(paymentTransactionRepository.findById(Mockito.any())).thenReturn(Optional.of(previous));

        UnsupportedOperationException exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> paymentTransactionService.save(actual));

        Assertions.assertEquals("the modification of the order items is not allowed", exception.getMessage());
    }
}