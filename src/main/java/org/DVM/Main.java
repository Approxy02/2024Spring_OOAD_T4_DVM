package org.DVM;


import org.DVM.Control.Controller;
import org.DVM.Control.Verification.VerificationManager;
import org.DVM.Stock.Item;
import org.DVM.Stock.Stock;
import org.DVM.UI.UIManager;

public class Main {
    public static void main(String[] args) {

        Controller controller = new Controller();
        controller.start();

//        VerificationManager verificationManager = new VerificationManager();
//        String s = verificationManager.createVerificationCode();
//        System.out.println(s);
//        verificationManager.saveVCode(s, 1, 1);
    }
}