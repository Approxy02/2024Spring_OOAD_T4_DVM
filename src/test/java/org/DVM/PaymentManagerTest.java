package org.DVM;

import org.DVM.Control.Payment.Card;
import org.DVM.Control.Payment.PaymentManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentManagerTest {
    private final PaymentManager paymentManager = new PaymentManager();

    @Test
    public void testPay() {
        Card card = new Card("1111", "123", 0);
        assertTrue(paymentManager.pay(card, 100.0f), "Payment should be successful");
        assertTrue(paymentManager.pay(card, 10.0f), "Payment should be successful");
    }

    @Test
    public void testPay2() {
        Card card = new Card("4444", "123", 0);
        assertFalse(paymentManager.pay(card, 400.0f), "Payment should be Failed");
        assertTrue(paymentManager.pay(card, 100.0f), "Payment should be successful");
    }
}
