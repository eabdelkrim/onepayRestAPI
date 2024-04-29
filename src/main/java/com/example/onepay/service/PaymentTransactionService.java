package com.example.onepay.service;

import com.example.onepay.domain.OrderItem;
import com.example.onepay.domain.PaymentTransaction;
import com.example.onepay.exception.BusinessException;
import com.example.onepay.repository.PaymentTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public List<PaymentTransaction> findAll() {
        return paymentTransactionRepository.findAll();
    }

    public PaymentTransaction save(PaymentTransaction paymentTransaction) throws BusinessException {

        PaymentTransaction previousPaymentTransaction = null;
        for(OrderItem orderItem : paymentTransaction.getOrderItems()){
            orderItem.setPaymentTransaction(paymentTransaction);
        }

        if (paymentTransaction.getId() != null)
            previousPaymentTransaction = findById(paymentTransaction.getId());

        if (previousPaymentTransaction == null && !paymentTransaction.isNew()) {
            throw new BusinessException("A new transaction must be in the \"NEW\" state");
        }

        if (previousPaymentTransaction != null
                && previousPaymentTransaction.isNew()
                && paymentTransaction.isCaptured()) {
            throw new BusinessException("it is not possible to change the transaction status to \"CAPTURED\" if the transaction is not in the \"AUTHORIZED\" state");
        }

        if (previousPaymentTransaction != null
                && previousPaymentTransaction.isCaptured()
                && !paymentTransaction.isCaptured()) {
            throw new BusinessException("it is not possible to modify the status of a \"CAPTURED\" transaction");
        }

        if (previousPaymentTransaction != null && previousPaymentTransaction.isChanged(paymentTransaction)) {
            throw new UnsupportedOperationException("the modification of the order items is not allowed");
        }

        return paymentTransactionRepository.save(paymentTransaction);
    }

    public PaymentTransaction findById(Integer id) {
        return paymentTransactionRepository.findById(id).orElse(null);
    }
}
