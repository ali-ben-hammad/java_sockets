package org.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            Map<String, Socket> clients = new HashMap<>();

            while(true){
                Socket s = serverSocket.accept();
                new Thread(new ChatThread(s, clients)).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
