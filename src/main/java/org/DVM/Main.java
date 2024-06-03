package org.DVM;


import org.DVM.Control.Communication.JsonClient;
import org.DVM.Control.Communication.Message;
import org.DVM.Control.Communication.MessageType;
import org.DVM.Control.Controller;
import org.DVM.Control.Verification.VerificationManager;
import org.DVM.Stock.Item;
import org.DVM.Stock.Stock;
import org.DVM.UI.UIManager;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            Controller controller = new Controller();
            controller.start();
        }).start();

        sleep(1000);
        JsonClient client = new JsonClient("localhost", 1234);
        client.startClient();
        Message resp = client.sendMessage(new Message(MessageType.resp_stock, "Team4", "Team4", null));
        System.out.println(resp.msg_content);

//        VerificationManager verificationManager = new VerificationManager();
//        String s = verificationManager.createVerificationCode();
//        System.out.println(s);
//        verificationManager.saveVCode(s, 1, 1);
    }
}