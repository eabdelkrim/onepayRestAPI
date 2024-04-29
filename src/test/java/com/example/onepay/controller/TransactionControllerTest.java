package com.example.onepay.controller;

import com.example.onepay.domain.OrderItem;
import com.example.onepay.domain.PaymentMethodType;
import com.example.onepay.domain.PaymentStatus;
import com.example.onepay.domain.PaymentTransaction;
import com.example.onepay.service.PaymentTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @MockBean
    PaymentTransactionService paymentTransactionService;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_create_new_transaction() throws Exception {
        PaymentTransaction request = new PaymentTransaction(1, BigDecimal.valueOf(112.87d), PaymentMethodType.CREDIT_CARD, PaymentStatus.NEW, new ArrayList<>());
        request.addOrderItem(new OrderItem(1, "LAPTOP", 2, BigDecimal.valueOf(10),null));
        request.addOrderItem(new OrderItem(2, "CARTRIDGE", 1, BigDecimal.valueOf(10),null));
        request.addOrderItem(new OrderItem(3, "FLASH USB", 5, BigDecimal.valueOf(10),null));
        request.addOrderItem(new OrderItem(4, "EXTERNAL HARD DRIVE", 4, BigDecimal.valueOf(10),null));

        mockMvc.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        ArgumentCaptor<PaymentTransaction> paymentTransactionCaptor = ArgumentCaptor.forClass(PaymentTransaction.class);
        verify(paymentTransactionService, times(1)).save(paymentTransactionCaptor.capture());
        assertThat(paymentTransactionCaptor.getValue().getId()).isEqualTo(1);
        assertThat(paymentTransactionCaptor.getValue().getAmount()).isEqualTo(BigDecimal.valueOf(112.87d));
    }

}