package org.DVM.Control.Communication;

import java.util.function.Consumer;

public class CommunicationManager {

    private JsonServer server = new JsonServer(1234);
    private JsonClient client = new JsonClient("192.168.219.111", 1234);

    public CommunicationManager() {
        startClient();
    }

    public void startServer(MessageCallback callback) {
        server.startServer(callback);
    }

    public void startClient() {
        client.startClient();
    }

    public Message createMessage(Message msg_info) {
        return msg_info;
    }

    public void sendMessageToClient(Message message) { //상대 client 가 보낸 요청에 대한 응답을 보낼때

        System.out.println("sendMessageToClient ");
        server.sendMessage(message);
    }

    public Message sendMessageToServer(Message message) { //상대 server 에게 요청을 보낼때

        System.out.println("sendMessageToServer");
        return client.sendMessage(message);
    }

    public Message requestCheckStockToDVM(Message msg_info) {

        return sendMessageToServer(msg_info);
    }

}
