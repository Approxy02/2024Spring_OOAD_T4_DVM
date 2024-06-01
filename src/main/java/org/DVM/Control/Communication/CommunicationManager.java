package org.DVM.Control.Communication;

import com.google.gson.Gson;

import java.util.function.Consumer;

public class CommunicationManager {

    private JsonServer server = new JsonServer(1234);

    public CommunicationManager() {

    }

    public void startServer(Consumer<Message> callback) {
        server.callback = callback;

        server.startServer();
    }

    private String createMessage(Message msg_info) {
        Gson message = new Gson();
        String s = message.toJson(msg_info);
        return s;

    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    private Message processMessage(String JSON) {
        return null;
    }

    public boolean requesPrepayToDVM(Message msg_info) {
//        String message = createMessage(msg_info);
//        sendMessage(message);
        return true;
    }

    public Message requestCheckStockToDVM(Message msg_info) {
        return null;
    }

}
