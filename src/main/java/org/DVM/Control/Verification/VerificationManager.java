package org.DVM.Control.Verification;

import java.util.ArrayList;

public class VerificationManager {
    private ArrayList<String> vCodes;

    public VerificationManager(){
        vCodes = new ArrayList<>();
    }

    public boolean verifyVCode(String vCode){
        return vCodes.contains(vCode);
    }

    public boolean saveVCode(String vCode){
        if(vCodes.contains(vCode)){
            return false;
        }
        vCodes.add(vCode);
        return true;
    }

    public String createVerificationCode(){
        String vCode = "";

        return vCode;
    }
}
