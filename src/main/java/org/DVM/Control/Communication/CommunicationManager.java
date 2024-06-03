package org.DVM.Control.Communication;

import java.util.function.Consumer;

public class CommunicationManager {

    private JsonServer server = new JsonServer(1234);
    private JsonClient client = new JsonClient("localhost", 1234);

    public CommunicationManager() {

    }

    public void startServer(MessageCallback callback) {
        server.startServer(callback);
    }

    public Message createMessage(Message msg_info) {
//        Gson message = new Gson();
//        String s = message.toJson(msg_info);
        return msg_info;

    }

    public void sendMessageToClient(Message message) { //상대 client 가 보낸 요청에 대한 응답을 보낼때

        System.out.println("sendMessageToClient ");
        server.sendMessage(message);
    }

    public void sendMessageToServer(Message message) { //상대 server 에게 요청을 보낼때

        System.out.println("sendMessageToServer");
        client.sendMessage(message);
    }

    private Message processMessage(String JSON) {
        return null;
    }

    public void requesPrepayToDVM(Message msg_info) {
//        String message = createMessage(msg_info);
//        sendMessage(message);
    }

    public Message requestCheckStockToDVM(Message msg_info) {

        sendMessageToServer(msg_info);

        return null;
    }

}
