package org.DVM.Stock;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

public class Stock {
    private ArrayList<Item> items = new ArrayList<Item>(); // 지금 다른데랑 타입이 안맞아요
    public Stock() {
        loadItemList();
    }

    public boolean checkStock(int item_code, int item_num){
        for (Item item : items) {
            if (item.code() == item_code && (item.quantity() - item.prePayed_amount()) >= item_num) {
                return true;
            }
        }
        return false;
    }

    public Item getItems(int item_code) {
        for (Item item : items) {
            if (item.code() == item_code) {
                return item;
            }
        }
        return null;
    }

    public void updateStock(int item_code, int item_num, boolean isPrePayed, String vCode){}

    public ArrayList<Item> itemList(){
        return items;
    }

    private void loadItemList() {
        String FilePath = System.getProperty("user.dir");
        FilePath += "/src/main/java/org/DVM/Stock/items.txt";
//        System.out.println(FilePath);
        File itemsFile = new File(FilePath);
        if (!itemsFile.exists()) {
            System.err.println("Items file not found");
            exit(1);
        }
//        System.out.println("Items file found");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(itemsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] itemInfo = line.split(" ");

                Item item = new Item(itemInfo[0], Integer.parseInt(itemInfo[1]), Integer.parseInt(itemInfo[2]), Integer.parseInt(itemInfo[3]));
//                System.out.println(item);
                this.items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Error reading items file");
            exit(1);
        }

    }

}
