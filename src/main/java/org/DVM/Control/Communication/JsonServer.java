package org.DVM.Control.Communication;

import java.net.ServerSocket;
import java.net.Socket;

public class JsonServer {
    private int port;

    private SocketService service;

    public JsonServer(int port) {
        this.port = port;
    }

    public void start(MessageCallback callback) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server Listening On Port : " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                service = new JsonSocketService(clientSocket);

                service.start();
                service.handleClient(clientSocket, callback);
                service.stop();
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());

            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        service.sendMessage(message);
    }
}
