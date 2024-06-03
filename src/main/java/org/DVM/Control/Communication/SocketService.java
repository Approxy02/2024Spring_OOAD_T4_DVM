package org.DVM.Control.Communication;

import java.net.Socket;

public interface SocketService {
    void start();
    void stop();
    void sendMessage(Object message);
    void handleClient(Socket clientSocket, MessageCallback callback);
    <T> T receiveMessage(Class<T> clazz);
}
