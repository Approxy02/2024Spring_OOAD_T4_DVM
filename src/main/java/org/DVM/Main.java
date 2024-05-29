package org.DVM;


import org.DVM.Control.Controller;
import org.DVM.Stock.Item;

public class Main {
    public static void main(String[] args) {

        Item[] items = new Item[20];
        items[0] = new Item("콜라", 1, 0, 0); //재고가 진짜 없거나 우리 DVM에서 취급 X인 음료
        items[1] = new Item("사이다", 2, 0, 0);


    }
}