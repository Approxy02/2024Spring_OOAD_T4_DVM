package org.DVM.Control.Communication;

public interface JsonSocketService {
    void start();
    void stop();
    void sendMessage(Object message);
    <T> T receiveMessage(Class<T> clazz);
}
