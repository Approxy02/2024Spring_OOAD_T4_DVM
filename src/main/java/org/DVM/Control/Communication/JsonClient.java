package org.DVM.Control.Communication;

import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JsonClient {
    private String host;
    private int port;

    private SocketService service;

    public JsonClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() {
        try (Socket socket = new Socket(host, port)) {
            service = new JsonSocketService(socket);
            service.start();
        } catch (Exception e) {
            System.out.println("Client Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        service.sendMessage(message);
    }

    public void stopClient() {
        service.stop();
    }

}
