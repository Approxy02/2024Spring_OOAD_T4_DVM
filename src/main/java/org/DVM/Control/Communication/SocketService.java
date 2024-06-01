package org.DVM.Control.Communication;

public interface SocketService {
    void start();
    void stop();
    void sendMessage(Object message);
    <T> T receiveMessage(Class<T> clazz);
}
