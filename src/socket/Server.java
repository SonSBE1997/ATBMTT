/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SBE
 */
public class Server {

    ServerSocket listener; //socket lắng nghe các kết nối
    String port; //cổng kết nối
    List<Socket> lsClient; //list chứa các client kết nối tới server

    public void setPort(String port) {
        this.port = port;
    }

    public Server() {
        lsClient = new ArrayList<>();
    }

    public boolean createServer() {
        try {
            listener = new ServerSocket(Integer.parseInt(this.port));
            JOptionPane.showMessageDialog(null, "Create server successfull");
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Create server failed.\nYou must input port is a number!!");
            return false;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Create server failed.\nHave some issues, it may conflict port");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void closeServer() {
        try {
            listener.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        try {
            while (true) {
                Socket client = listener.accept();
                System.out.println(client.getInetAddress().getHostAddress() + " connected");
//                JOptionPane.showMessageDialog(null, client.getInetAddress().getHostAddress() + " connected");
                lsClient.add(client);
                new Thread(() -> {
                    receiveDataFromClientAndSendToOther(client);
                }).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void receiveDataFromClientAndSendToOther(Socket client) {

        try {
            while (true) {
                /* Khai báo luồng đọc, ghi dữ liệu */
                InputStream is = client.getInputStream();
                DataInputStream din = new DataInputStream(is); //luồng vào
                /* Thực hiện đọc từng mảng byte do client gửi đến và chuyển sang xâu kí tự */
                int bufferSize = client.getReceiveBufferSize();
                byte[] bytes = new byte[bufferSize];
                if (din.read(bytes) >= 0) {
                    for (Socket s : lsClient) {
                        if (s == null) {
                            lsClient.remove(s);
                        }
                        if (s != null && s != client) {
                            OutputStream os = s.getOutputStream();
                            DataOutputStream dout = new DataOutputStream(os);
                            dout.write(bytes, 0, bytes.length);
                            dout.flush();
                        }
                    }
                    Arrays.fill(bytes, (byte) 0);// chuyển tất cả mảng byte về mảng chứa 0
                }
            }
        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(null, "User disconnected");
            lsClient.remove(client);
            System.out.println("User disconnected");
        }
    }

    public void closeAllClient() {
        lsClient.forEach((socket) -> {
            try {
                if (socket != null) {
                    socket.close();
                } else {
                    lsClient.remove(socket);
                }
            } catch (IOException ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        lsClient.clear();
    }
}
