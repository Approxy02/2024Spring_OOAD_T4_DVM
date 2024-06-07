package org.DVM.Control.Communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class CommunicationManager {

    private JsonServer server = new JsonServer(1234);
    private HashMap<String, JsonClient> clients = new HashMap<String, JsonClient>();

    public CommunicationManager() {
        //new JsonClient("192.168.181.226", 1234)
        clients.put("Team1", null);
        clients.put("Team2", null);
        clients.put("Team3", null);
        clients.put("Team4", null);
        clients.put("Team5", null);
        clients.put("Team6", new JsonClient("turtle-hwan.iptime.org", 30303));
        clients.put("Team7", null);
        clients.put("Team8", null);
        clients.put("Team9", null);
    }

    public void startServer(MessageCallback callback) {
        server.startServer(callback);
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

        JsonClient client = clients.get(message.dst_id);

        Message result = null;

        if (client == null) {
            System.out.println("Client Not Found");
            return null;
        }

        try {
            client.startClient();

            result = client.sendMessage(message);

            client.stopClient();
        } catch (Exception e) {
            System.out.println("Client Exception at " + message.dst_id + " : " + e.getMessage());
        }

        return result;
    }

    public Message requestCheckStockToDVM(Message msg_info) {

        return sendMessageToServer(msg_info);
    }

    public Message requestPrepayToDVM(Message msg_info) {

        return sendMessageToServer(msg_info);
    }

}
