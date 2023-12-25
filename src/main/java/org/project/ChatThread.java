package org.project;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ChatThread implements Runnable{
    private Socket socket;
    private Map<String, Socket> users;

    public ChatThread(Socket socket, Map<String, Socket> users) {
        this.socket = socket;
        this.users = users;
    }

    private void sendMessage(String src, String message, String dest) throws IOException {
        Socket destSocket = users.get(dest);
        if(destSocket == null) return;
        OutputStream os = destSocket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(src+ ": "+ message);
    }

    @Override
    public void run() {
        try {
            String userName = "user" + (users.size());
            users.put(userName, socket);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while(true) {
                // send user list
                StringBuilder usersList = new StringBuilder();
                for (String user : users.keySet()) {
                    usersList.append(user).append(",");
                }
                System.out.println(usersList);
                // send users as an string object using object output stream not printwriter
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(usersList.toString());
                oos.flush();
                // send message
                String receivedMsg = br.readLine();
                String[] split = receivedMsg.split("=>");
                System.out.println(receivedMsg);
                if(userName.equals(split[1])) continue;
                sendMessage(userName, split[0], split[1]);



            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
