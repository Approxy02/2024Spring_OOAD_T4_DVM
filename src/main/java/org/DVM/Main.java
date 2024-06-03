package org.DVM;


import org.DVM.Control.Communication.JsonClient;
import org.DVM.Control.Communication.Message;
import org.DVM.Control.Communication.MessageType;
import org.DVM.Control.Controller;
import org.DVM.Control.Verification.VerificationManager;
import org.DVM.Stock.Item;
import org.DVM.Stock.Stock;
import org.DVM.UI.UIManager;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            Controller controller = new Controller();
            controller.start();
        }).start();

//        sleep(1000);
//        JsonClient client = new JsonClient("localhost", 1234);
//        client.startClient();
//
//        HashMap<String, String> msg_content = new HashMap<>();
//
//        msg_content.put("item_code", String.valueOf(1));
//        msg_content.put("item_num", String.valueOf(5));
//        msg_content.put("coor_x", String.valueOf(10));
//        msg_content.put("coor_y", String.valueOf(10));
//        Message resp = client.sendMessage(new Message(MessageType.req_stock, "Team4", "Team4", msg_content));
//        System.out.println(resp.msg_content.get("item_code"));

//        VerificationManager verificationManager = new VerificationManager();
//        String s = verificationManager.createVerificationCode();
//        System.out.println(s);
//        verificationManager.saveVCode(s, 1, 1);
    }
}