package org.DVM.Control;

import org.DVM.Control.Communication.*;
import org.DVM.Control.Payment.Card;
import org.DVM.Control.Payment.CardReader;
import org.DVM.Control.Payment.PaymentManager;
import org.DVM.Control.Verification.VerificationManager;
import org.DVM.Stock.Stock;
import org.DVM.UI.UIManager;
import org.DVM.Stock.Item;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Controller {
    private UIManager uiManager;
    private Stock stock;
    private VerificationManager verificationManager;
    private CommunicationManager communicationManager;
    private PaymentManager paymentManager;
    private CardReader cardReader;
    private ArrayList<OtherDVM> otherDVMs = new ArrayList<OtherDVM>();
    private Item item;
    private final String src_id = "Team4";
    private final int our_x = 40;
    private final int our_y = 40;
    private OtherDVM minDVM = null;

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

        Thread thread = new Thread(() -> {
            communicationManager.startServer(new MessageCallback() {
                @Override
                public void onMessageReceived(Message message) {
                    System.out.println("Received message at Controller: " + message.msg_type + " from " + message.src_id);

                    HashMap<String, String> msg_content = new HashMap<>();
                    switch (message.msg_type) {

                        case req_stock -> {
                            Item item = checkStock(message);


                            msg_content.put("item_code", String.valueOf(item.code));
                            msg_content.put("item_num", String.valueOf(item.quantity));
                            msg_content.put("coor_x", String.valueOf(our_x));
                            msg_content.put("coor_y", String.valueOf(our_y));

                            Message msg_info = new Message(MessageType.resp_stock, src_id, message.src_id, msg_content);
                            Message returnMsg = communicationManager.createMessage(msg_info);
                            communicationManager.sendMessageToClient(returnMsg);
                        }

                        case req_prepay -> {
                            boolean availabilty = checkPrepayAvailability(message);

                            msg_content.put("item_code", message.msg_content.get("item_code"));
                            msg_content.put("item_num", message.msg_content.get("item_num"));
                            msg_content.put("availability", availabilty ? "T" : "F");

                            Message msg_info = new Message(MessageType.resp_stock, src_id, message.src_id, msg_content);
                            Message returnMsg = communicationManager.createMessage(msg_info);
                            communicationManager.sendMessageToClient(returnMsg);
                        }
                    }
                }
            });
        });
        thread.start();

        uiManager.display("MainUI", null, null, null, null);

        mainAction();
    }

    public void mainAction() {
        // Wait for user input in the main thread
        String userInput = uiManager.waitForInputString();
        System.out.println("User input: " + userInput);

        if (userInput.length() != 10 && userInput.contains(" ")) {
            System.out.println("이건 결제 버튼");
            String[] itemInfo = userInput.split(" ");
            int itemCode = Integer.parseInt(itemInfo[0]);
            int itemNum = Integer.parseInt(itemInfo[1]);

            if (itemCode >= 1 && itemCode <= 20)
                selectItems(itemCode, itemNum);
            else {
                uiManager.displayError("잘못된 상품 코드입니다. 다시 입력해주세요");

                mainAction();
            }
        } else {
            System.out.println("이건 인증코드 버튼");
            inputVCode(userInput);
        }
    }

    public void selectItems(int item_code, int item_num) {
        if (stock.checkStock(item_code, item_num)) {
            System.out.println("Item is available");
            Item item = new Item(stock.getItems(item_code).name, item_code, item_num, 0);
            this.item = item;
            System.out.println(item);
            uiManager.display("PaymentUI_1", stock.itemList(), item, null, null);
            String cardInfo = uiManager.waitForInputString();
            System.out.println("Card Info : " + cardInfo);
            Card card = new Card(cardInfo, null, item_num * 100); // 넘겨주는 balance가 결제할 금액

            insertCard(card);

        } else {
            System.out.println("Item is not available");

            for (int i = 1; i <= 9; i++) {
                String dst_id = "Team";
                if (i == 4)
                    continue;
                String id = String.valueOf(i);
                dst_id = dst_id + id;

                HashMap<String, String> msg_content = new HashMap<>();

                msg_content.put("item_code", String.valueOf(item_code));
                msg_content.put("item_num", String.valueOf(item_num));

                Message msg_info = new Message(MessageType.req_stock, src_id, dst_id, msg_content);
                Message returnMsg = communicationManager.requestCheckStockToDVM(msg_info);

                if (Integer.parseInt(returnMsg.msg_content.get("item_num")) >= item.quantity) {
                    otherDVMs.add(new OtherDVM(dst_id, Integer.parseInt(returnMsg.msg_content.get("coor_x")), Integer.parseInt(returnMsg.msg_content.get("coor_y"))));
//                   calculateDistance(Integer.parseInt(returnMsg.msg_content.get("coor_x")), Integer.parseInt(returnMsg.msg_content.get("coor_y")));
                }
            }

            minDistance();

            uiManager.display("LocationInfoUI", stock.itemList(), item, minDVM, null);
            // 이후 prepay
            // prepay 구현을 아직 안함 ㅋㅋ

        }
    }

    public void insertCard(Card card) {
//        CardReader cardReader = new CardReader();
        Card cardInfo = cardReader.getCardInfo(card);

//        PaymentManager paymentManager = new PaymentManager();
        if (paymentManager.pay(cardInfo, cardInfo.balance)) {
            System.out.println("Payment success");
            uiManager.display("PaymentUI_2", stock.itemList(), item, null, null);
            String garbage = uiManager.waitForInputString();

            dispenseItems(item.code, item.quantity);

        } else {
            System.out.println("Payment failed");

            uiManager.displayError("결제에 실패했습니다! 다시 시도해주세요");

            uiManager.display("MainUI", null, null, null, null);

            mainAction();

        }

    }

    public void inputVCode(String vCode) {
        if (verificationManager.verifyVCode(vCode)) {
            Item dis = verificationManager.getItems(vCode);
            verificationManager.removeVCode(vCode);
            dispenseItems(dis.code, dis.quantity);
        } else {
            uiManager.displayError("인증코드를 다시 입력하세요");

            uiManager.display("MainUI", null, null, null, null);

            mainAction();
        }
    }

    public boolean checkPrepayAvailability(Message msg_info) {
        // Vcode update 해줘야 댐!!!!
        int item_code = Integer.parseInt(msg_info.msg_content.get("item_code"));
        int item_num = Integer.parseInt(msg_info.msg_content.get("item_num"));

        return stock.checkStock(item_code, item_num);
    }

    public Item checkStock(Message msg_info) {
        int item_code = Integer.parseInt(msg_info.msg_content.get("item_code"));
        int item_num = Integer.parseInt(msg_info.msg_content.get("item_num"));

        Item item = stock.getItems(item_code);

        return new Item(item.name, item_code, item.quantity - item.prePayed_amount, 0);
    }

    public float calculateDistance(int coor_x, int coor_y) {
        return (float) Math.sqrt(Math.pow(coor_x - our_x, 2) + Math.pow(coor_y - our_y, 2));
    }

    public void minDistance() {
        float min = 10000000;
        for (OtherDVM dvm : otherDVMs) {
            float distance = calculateDistance(dvm.coor_x, dvm.coor_y);
            if (distance < min) {
                min = distance;
                minDVM = dvm;
            }
        }

        System.out.println("Nearest DVM is " + minDVM.name + " at " + minDVM.coor_x + ", " + minDVM.coor_y);
    }

    public void dispenseItems(int item_code, int item_num) {
        System.out.println("Item dispensed");
        Item dis = stock.getItems(item_code);
        item = new Item(dis.name, item_code, item_num, 0);
        uiManager.display("DispenseResultUI", stock.itemList(), item, null, null);

        String garbage = uiManager.waitForInputString();

        stock.updateStock(item_code, item_num, false, null);

        uiManager.display("MainUI", null, null, null, null);

        mainAction();

    }

}
