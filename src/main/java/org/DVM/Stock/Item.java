package org.DVM.Stock;

public class Item {

    public Item(String name, int code, int quantity, int prePayed_amount) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.prePayed_amount = prePayed_amount;
    }

    public String name;
    public int code;
    public int quantity;
    public int prePayed_amount;
}

