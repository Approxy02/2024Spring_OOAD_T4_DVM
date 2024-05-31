package org.DVM.Control;

import org.DVM.Control.Communication.CommunicationManager;
import org.DVM.Control.Communication.Message;
import org.DVM.Control.Communication.OtherDVM;
import org.DVM.Control.Payment.Card;
import org.DVM.Control.Payment.CardReader;
import org.DVM.Control.Payment.PaymentManager;
import org.DVM.Control.Verification.VerificationManager;
import org.DVM.Stock.Stock;
import org.DVM.UI.UIManager;
import org.DVM.Stock.Item;

import java.util.ArrayList;

public class Controller {
    private UIManager uiManager;
    private Stock stock;
    private VerificationManager verificationManager;
    private CommunicationManager communicationManager;
    private PaymentManager paymentManager;
    private CardReader cardReader;
    private ArrayList<OtherDVM> otherDVMs;

    public Controller() {
        uiManager = new UIManager();
        stock = new Stock();
        verificationManager = new VerificationManager();
        communicationManager = new CommunicationManager();
        paymentManager = new PaymentManager();
        cardReader = new CardReader();
        otherDVMs = new ArrayList<OtherDVM>();
    }

    public void start() {
        // Display MainUI in a new thread
        new Thread(() -> {
            uiManager.display("MainUI", null, null, null, null);
        }).start();

        // Wait for user input in the main thread
        String userInput = uiManager.waitForInputString();
        System.out.println("User input: " + userInput);

        if (userInput.length() != 10 && userInput.contains(" ")) {
            System.out.println("이건 결제 버튼");
            String[] itemInfo = userInput.split(" ");
            int itemCode = Integer.parseInt(itemInfo[0]);
            int itemNum = Integer.parseInt(itemInfo[1]);

            selectItems(itemCode, itemNum);
        } else {
            System.out.println("이건 인증코드 버튼");
        }
    }

    public void selectItems(int item_code, int item_num) {
        if (stock.checkStock(item_code, item_num)) {
            System.out.println("Item is available");
            Item item = new Item(stock.getItems(item_code).name(), item_code, item_num, 0);
            System.out.println(item);
            uiManager.display("PaymentUI-1", stock.itemList(), item, null, null);
            String cardInfo = uiManager.waitForInputString();
            System.out.println("Card Info : " + cardInfo);

        } else {
//            communicationManager.
        }
    }

    public void insertCard(Card card){

    }

    public void inputVCode(String vCode){}

    public boolean checkPrepayAvailability(Message msg_info){
        return true;
    }

    public boolean checkStock(Message msg_info){
        return true;
    }

    public float calculateDistance(int coor_x, int coor_y){
        return 0;
    }

    public void minDistance(){

    }

    public void dispenseItems(int item_code, int item_num){

    }

}
