package org.DVM.Control.Communication;

import com.google.gson.Gson;

import java.util.HashMap;

public class CommunicationManager {

    private JsonServer server = new JsonServer(1234);

    public CommunicationManager(){
        server.callback = (Message message) -> {
            //processMessage(message)

            switch(message.msg_type){
                case req_stock -> {

                }
                case resp_stock -> {
                    //processMessage(message);
                    //boolean availability = controller.checkStock();
                    //Message message = createMessage();
                    //sendMessage(message);

                }
                case req_prepay -> {

                }
                case resp_prepay -> {
                    //processMessage(message);
                    //boolean availability = controller.requestPrepayToDVM(Message);
                    //Message message = createMessage();
                    //sendMessage(message);
                }
            }
        };

        new Thread(() -> {
            server.startServer();
        }).start();
    }

    private String createMessage(Message msg_info) {
        return null;
    }

    private void sendMessage(String message) {
        JsonClient client = new JsonClient("",1234);

        //client.sendMessage();
    }

    private Message processMessage(String JSON) {
        return null;
    }

    public boolean requestPrepayToDVM(Message msg_info) {
//        String message = createMessage(msg_info);
        //sendMessage(msg_info);
        return true;
    }

    public Message requestCheckStockToDVM(Message msg_info) {
        return null;
    }

}
