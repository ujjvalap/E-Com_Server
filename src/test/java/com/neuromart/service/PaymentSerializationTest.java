package com.neuromart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.neuromart.dto.PaymentProcessRequest;
import com.neuromart.dto.PaymentVerificationRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentSerializationTest {

    private PaymentService paymentService;
    private RazorpayClient razorpayClient;
    private OrderClient orderClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        razorpayClient = Mockito.mock(RazorpayClient.class);
        orderClient = Mockito.mock(OrderClient.class);

        // Inject mocked OrderClient into RazorpayClient
        ReflectionTestUtils.setField(
                razorpayClient,
                "orders",
                orderClient
        );

        paymentService = new PaymentService(razorpayClient);

        // Use isolated test credentials
        ReflectionTestUtils.setField(
                paymentService,
                "keyId",
                "rzp_test_unit_test_key"
        );

        ReflectionTestUtils.setField(
                paymentService,
                "keySecret",
                "unit_test_secret"
        );

        objectMapper = new ObjectMapper();
    }

    @Test
    void testProcessPaymentFallbackSerialization() throws Exception {

        when(orderClient.create(any(JSONObject.class)))
                .thenThrow(new RuntimeException("Simulated Razorpay failure"));

        Map<String, Object> response =
                paymentService.processPayment(
                        new PaymentProcessRequest(250.0)
                );

        assertNotNull(response);
        assertEquals(true, response.get("success"));

        String json = objectMapper.writeValueAsString(response);

        assertTrue(json.contains("\"id\":\"order_"));
        assertTrue(json.contains("\"amount\":25000"));
    }

    @Test
    void testGetApiKey() throws Exception {

        Map<String, String> keyMap = paymentService.getApiKey();

        assertEquals(
                "rzp_test_unit_test_key",
                keyMap.get("key")
        );

        String json = objectMapper.writeValueAsString(keyMap);

        assertTrue(json.contains("rzp_test_unit_test_key"));
    }

    @Test
    void testVerifyPayment() throws Exception {

        PaymentVerificationRequest request =
                new PaymentVerificationRequest(
                        "pay_123",
                        "order_456",
                        "invalid_sig"
                );

        Map<String, Object> result =
                paymentService.verifyPayment(request);

        assertNotNull(result);
        assertEquals(false, result.get("success"));
        assertEquals(
                "Payment verification failed",
                result.get("message")
        );

        String json = objectMapper.writeValueAsString(result);

        assertNotNull(json);
    }
}