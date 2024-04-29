package com.example.onepay.controller;

import com.example.onepay.exception.BusinessException;
import com.example.onepay.domain.PaymentTransaction;
import com.example.onepay.exception.TransactionNotFoundException;
import com.example.onepay.service.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> getTransactions() {

        return new ResponseEntity<>(paymentTransactionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{transactionId}")
    public PaymentTransaction getTransaction(@PathVariable("transactionId") Integer transactionId) throws TransactionNotFoundException {
        PaymentTransaction PaymentTransaction = paymentTransactionService.findById(transactionId);

        if (PaymentTransaction == null) {
            throw new TransactionNotFoundException("Transaction not found");
        }

        return paymentTransactionService.findById(transactionId);
    }

    @PostMapping
    public ResponseEntity<PaymentTransaction> createTransaction(@RequestBody PaymentTransaction PaymentTransaction) throws BusinessException, UnsupportedOperationException {

        PaymentTransaction paymentTransaction = paymentTransactionService.save(PaymentTransaction);

        return new ResponseEntity<>(paymentTransaction, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{transactionId}")
    public ResponseEntity<PaymentTransaction> updateTransaction(@RequestBody PaymentTransaction PaymentTransaction, @PathVariable Integer transactionId) throws BusinessException, UnsupportedOperationException, TransactionNotFoundException {

        PaymentTransaction paymentTransaction = paymentTransactionService.findById(transactionId);

        if (paymentTransaction == null) {
            throw new TransactionNotFoundException("Transaction not found");
        } else {
            paymentTransaction = paymentTransactionService.save(PaymentTransaction);
        }

        return new ResponseEntity<>(paymentTransaction, HttpStatus.CREATED);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);

    }
}
