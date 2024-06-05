package org.DVM.Control.Communication;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CommunicationManager {

    private JsonServer server = new JsonServer(1234);
    private ArrayList<JsonClient> clients = new ArrayList<>();
//    private JsonClient client = new JsonClient("localhost", 1234);

    public CommunicationManager() {
        JsonClient client1 = new JsonClient("192.168.182.99", 1234);
        JsonClient client2 = new JsonClient("192.168.181.226", 1234);
        JsonClient client3 = new JsonClient("localhost", 1234);
        JsonClient client4 = null;
        clients.add(client1);
        clients.add(client2);
        clients.add(client3);
        clients.add(client4);
        startClient();
    }

    public void startServer(MessageCallback callback) {
        server.startServer(callback);
    }

    public void startClient() {
        for (JsonClient client : clients) {
            client.startClient();
        }
    }

    public Message createMessage(Message msg_info) {
        return msg_info;
    }

    public void sendMessageToClient(Message message) { //상대 client 가 보낸 요청에 대한 응답을 보낼때

        System.out.println("Respond Message To " + message.dst_id + " : " + message.msg_type + " : " + message.msg_content);
        server.sendMessage(message);
    }

    public Message sendMessageToServer(Message message) { //상대 server 에게 요청을 보낼때

        System.out.println("Request Message To Server");
        String dst_id = message.dst_id;
        int id = Integer.parseInt(dst_id.charAt(dst_id.length() - 1) + "");

        JsonClient client = clients.get(id - 1);

        if(client != null)
            return client.sendMessage(message);

        return null;
    }

    public Message requestCheckStockToDVM(Message msg_info) {

        return sendMessageToServer(msg_info);
    }

    public Message requestPrepayToDVM(Message msg_info) {

        return sendMessageToServer(msg_info);
    }

}
