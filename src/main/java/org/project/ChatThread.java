package org.project;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ChatThread implements Runnable{
    private Socket socket;
    private Map<String, Socket> users;

    public ChatThread(Socket socket, Map<String, Socket> users) {
        this.socket = socket;
        this.users = users;
    }

    private void sendMessage(String message, String dest) throws IOException {
        Socket destSocket = users.get(dest);
        OutputStream os = destSocket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(message);
    }

    @Override
    public void run() {
        try {
            String userName = "user" + (users.size()+1);
            users.put(userName, socket);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while(socket.isConnected()) {
                String receivedMsg = br.readLine();
                String[] split = receivedMsg.split("=>");
                System.out.println(receivedMsg);
                //sendMessage(split[0], split[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
