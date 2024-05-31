package org.DVM.UI;

import org.DVM.Control.Communication.OtherDVM;
import org.DVM.Stock.Item;

import java.util.ArrayList;

public class UIManager {
    private String UItype;
    private String errorMsg;

    public UIManager(String UItype) {
        this.UItype = UItype;
    }

    public void display(String UItype, ArrayList<Item> items, Item item, OtherDVM dvm, String vCode) {
        switch (UItype) {
            case "MainUI":
                mainUIdisplay(items);
                break;
            case "PaymentUI-1":
                payUI_1(item);
                break;
            case "PaymentUI-2":
                payUI_2(item);
                break;
            case "PrepaymentUI-1":
                prepayUI_1(item);
                break;
            case "PrepaymentUI-2":
                prepayUI_2(item);
                break;
            case "LocationInfoUI":
                locationInfoUI(item);
                break;
            case "VerificationCodeDisplayUI":
                vCodeUI(item);
                break;
            case "DispenseResultUI":
                dispenseUI(item);
                break;
            default:
                break;
        }
    }


    private void mainUIdisplay(ArrayList<Item> items) {
        new Example();
    }

    private String payUI_1(Item item) {
        return "";
    }

    private void payUI_2(Item item) {
    }

    private String prepayUI_1(Item item) {
        return "";
    }

    private void prepayUI_2(Item item) {
    }

    private void locationInfoUI(Item item) {
    }

    private void vCodeUI(Item item) {
    }

    private void dispenseUI(Item item) {
    }
}
