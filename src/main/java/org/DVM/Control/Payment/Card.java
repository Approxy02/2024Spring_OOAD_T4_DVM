package org.DVM.Control.Payment;

public class Card {
    public String cardNum;
    public String cardType;
    public int balance;

    public Card(String cardNum, String cardType, int balance) {
        this.cardNum = cardNum;
        this.cardType = cardType;
        this.balance = balance;
    }


}
