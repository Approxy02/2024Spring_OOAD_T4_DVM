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

    public void startClient() throws Exception {

        socket = new Socket(host, port);
        socket.setSoTimeout(3000);
        service = new JsonSocketService(socket);
        service.start();

    }

    public Message sendMessage(Message msg) {
        System.out.println("Send Message: ");
        service.sendMessage(msg);
        return service.receiveMessage(Message.class);
    }

    public void stopClient() {
        service.stop();
    }

}
