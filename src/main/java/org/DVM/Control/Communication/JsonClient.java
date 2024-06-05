package org.DVM.Control.Communication;

import java.net.Socket;

public class JsonClient {
    private String host;
    private int port;

    private SocketService service;

    public JsonClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        try {

            Socket socket = new Socket(host, port); socket.setSoTimeout(3000);

            service = new JsonSocketService(socket);

            service = new JsonSocketService(new Socket(host, port));

            service.start();
        } catch (Exception e) {
            throw e;
        }
    }

    public Message sendMessage(Message msg) {
        service.sendMessage(msg);

        return service.receiveMessage(Message.class);
    }

    public void stop() {
        service.stop();
    }

}
