package org.DVM.Control.Verification;

import org.DVM.Stock.Item;

import java.util.ArrayList;
import java.security.SecureRandom;

public class VerificationManager {
    private ArrayList<String> vCodes;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 10;

    public VerificationManager(){
        vCodes = new ArrayList<>();
        vCodes.add("1234567890 1 5");
    }

    public boolean verifyVCode(String vCode){
        System.out.println("arg " + vCode);
        for (String code : vCodes) {
            System.out.println("code " + code);
            if(code.contains(vCode)) {
                return true;
            }
        }
        return false;
    }

    public void removeVCode(String vCode){
        vCodes.removeIf(code -> code.contains(vCode));
    }

    public Item getItems(String vCode){
        for (String code : vCodes) {
            if (code.contains(vCode)) {
                vCode = code;
            }
        }
        String[] vCodeSplit = vCode.split(" ");
        int item_code = Integer.parseInt(vCodeSplit[1]);
        int item_num = Integer.parseInt(vCodeSplit[2]);
        return new Item(null, item_code, item_num, 0);
    }

    public boolean saveVCode(String vCode, int item_code, int item_num ){
        vCode += " " + item_code + " " + item_num;
        System.out.println(vCode);
        if(vCodes.contains(vCode)){
            return false;
        }
        vCodes.add(vCode);
        return true;
    }

    public String createVerificationCode(){
        SecureRandom random = new SecureRandom();
        StringBuilder vCode = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            vCode.append(CHARACTERS.charAt(index));
        }

        return vCode.toString();
    }
}
