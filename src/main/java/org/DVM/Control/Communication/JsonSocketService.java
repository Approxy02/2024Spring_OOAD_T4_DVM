package org.DVM.Control.Communication;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class JsonSocketService implements SocketService {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private final Gson gson = new Gson();

    public JsonSocketService(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void start() {
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("Error initializing streams", e);
        }
    }

    @Override
    public void stop() {
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException("Error closing streams", e);
        }
    }

    @Override
    public void sendMessage(Object message) {
        writer.println(gson.toJson(message));
    }

    @Override
    public <T> T receiveMessage(Class<T> clazz) {
        try {
            return gson.fromJson(reader.readLine(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error receiving message", e);
        }
    }
}
