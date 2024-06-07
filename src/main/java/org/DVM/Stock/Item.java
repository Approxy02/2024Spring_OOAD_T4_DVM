package org.DVM.Stock;

public class Item {

    public Item(String name, int code, int quantity, int prePayed_amount) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.prePayed_amount = prePayed_amount;
        init();
    }

    public String name;
    public int code;
    public int quantity;
    public int prePayed_amount;
    public int price;

    public void init(){
        if(code == 1){
            price = 2000;
        }
        else if(code == 2){
            price = 2000;
        }
        else if(code == 3){
            price = 1500;
        }
        else if(code == 4){
            price = 1500;
        }
        else if(code == 5){
            price = 2000;
        }
        else if(code == 6){
            price = 1500;
        }
        else if(code == 7){
            price = 1500;
        }
        else if(code == 8){
            price = 1000;
        }
        else if(code == 9){
            price = 1000;
        }
        else if(code == 10){
            price = 2500;
        }
        else if(code == 11){
            price = 2000;
        }
        else if(code == 12){
            price = 1000;
        }
        else if(code == 13){
            price = 2500;
        }
        else if(code == 14){
            price = 3000;
        }
        else if(code == 15){
            price = 3000;
        }
        else if(code == 16){
            price = 3000;
        }
        else if(code == 17){
            price = 2000;
        }
        else if(code == 18){
            price = 2000;
        }
        else if(code == 19){
            price = 1000;
        }
        else if(code == 20){
            price = 2500;
        }

    }
}

