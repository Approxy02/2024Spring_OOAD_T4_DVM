package org.DVM.Control.Payment;

public class PaymentManager {

    Bank bank = new Bank();

    public boolean pay(Card cardInfo, float totalCost) {
        return bank.checkCardAvailabilty(cardInfo, totalCost);
    }
}
