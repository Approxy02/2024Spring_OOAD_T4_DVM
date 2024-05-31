package org.DVM.Control.Payment;

import org.DVM.Stock.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Bank {

    private ArrayList<Card> cards = new ArrayList<Card>();

    public Bank(){
        init();
    }

    public boolean checkCardAvailabilty(Card card, float totalCost) {
        for (Card c : cards) {
            if (c.cardNum.equals(card.cardNum)) {
                if (c.balance >= totalCost) {
                    c.balance -= (int) totalCost;
//                    System.out.println("Payment successful");
//                    System.out.println(c.balance);
                    return true;
                }
            }
        }
        return false;
    }

    public void init() {
        String FilePath = System.getProperty("user.dir");
        FilePath += "/src/main/java/org/DVM/Control/Payment/cards.txt";
//        System.out.println(FilePath);
        File itemsFile = new File(FilePath);
        if (!itemsFile.exists()) {
            System.err.println("cards file not found");
            exit(1);
        }
//        System.out.println("Items file found");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(itemsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cardInfo = line.split(" ");

                Card card = new Card(cardInfo[0], cardInfo[1], Integer.parseInt(cardInfo[2]));

                this.cards.add(card);
            }
        } catch (Exception e) {
            System.err.println("Error reading card file");
            exit(1);
        }
    }
}
