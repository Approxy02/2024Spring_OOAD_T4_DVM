package org.DVM.Control.Communication;

import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JsonClient {
    private String host;
    private int port;

    private SocketService service;
    private Socket socket;

    public JsonClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() {
        try {
            socket = new Socket(host, port);
            service = new JsonSocketService(socket);
            service.start();
        } catch (Exception e) {
            System.out.println("Client Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Message sendMessage(Message msg) {
        service.sendMessage(msg);
        return service.receiveMessage(Message.class);
    }

    public void stopClient() {
        service.stop();
    }

}
