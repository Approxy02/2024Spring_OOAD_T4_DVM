package org.DVM.Stock;

public class Stock {
    private Item[] items;

    public Stock(Item[] items) {
        this.items = items;
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

    public Item[] itemList(){
        return items;
    }

}
