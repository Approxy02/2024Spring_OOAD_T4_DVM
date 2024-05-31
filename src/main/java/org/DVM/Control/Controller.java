package org.DVM.Control;

import org.DVM.Control.Communication.CommunicationManager;
import org.DVM.Control.Communication.OtherDVM;
import org.DVM.Control.Payment.CardReader;
import org.DVM.Control.Payment.PaymentManager;
import org.DVM.Control.Verification.VerificationManager;

import java.util.ArrayList;

public class Controller {
    private VerificationManager verificationManager;
    private CommunicationManager communicationManager;
    private PaymentManager paymentManager;
    private CardReader cardReader;
    private ArrayList<OtherDVM> otherDVMs;

    public Controller(){
        verificationManager = new VerificationManager();
    }

}
