package org.DVM;


import org.DVM.Control.Controller;
import org.DVM.Stock.Item;
import org.DVM.Stock.Stock;

public class Main {
    public static void main(String[] args) {

//        Item[] items = new Item[20];
//        items[0] = new Item("콜라", 1, 0, 0); //재고가 진짜 없거나 우리 DVM에서 취급 X인 음료
//        items[1] = new Item("사이다", 2, 0, 0);
//        items[2] = new Item("사이다", 2, 0, 0);
//        items[3] = new Item("사이다", 2, 0, 0);
//        items[4] = new Item("사이다", 2, 0, 0);
//        items[5] = new Item("사이다", 2, 0, 0);
//        items[6] = new Item("사이다", 2, 0, 0);
//        items[7] = new Item("사이다", 2, 0, 0);
//        items[8] = new Item("사이다", 2, 0, 0);
//        items[9] = new Item("사이다", 2, 0, 0);
//        items[10] = new Item("사이다", 2, 0, 0);
//        items[11] = new Item("사이다", 2, 0, 0);
//        items[14] = new Item("사이다", 2, 0, 0);
//        items[15] = new Item("사이다", 2, 0, 0);
//        items[16] = new Item("사이다", 2, 0, 0);
//        items[17] = new Item("사이다", 2, 0, 0);
//        items[18] = new Item("사이다", 2, 0, 0);
//        items[19] = new Item("사이다", 2, 0, 0);

        Stock stock = new Stock();

        for(Item item : stock.itemList()){
            System.out.println(item);
        }

        Controller controller = new Controller();
//        controller.start();

    }
}