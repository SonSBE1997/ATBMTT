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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SBE
 */
public class Client {

    private Socket server;

    public Socket getServer() {
        return server;
    }

    /**
     * kết nối đến server
     *
     * @param ipAddress địa chỉ ip của server
     * @param port cổng kết nối
     * @return
     */
    public boolean connectToServer(String ipAddress, int port) {
        try {
            server = new Socket(ipAddress, port);
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Connect to server failed!");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void send(String message) {
        try {
            OutputStream os = server.getOutputStream();
            DataOutputStream dout = new DataOutputStream(os);
            // Chuyển từ xâu thành mảng byte và gửi lên server.
            byte[] bytes = message.getBytes();
            dout.write(bytes, 0, bytes.length);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receive data from server
     */
    public void receive() {
        while (true) {
            try {
                /* Khái báo các luồng vào dữ liệu*/
                InputStream is = server.getInputStream();
                DataInputStream din = new DataInputStream(is);
                /* -----------------------------------------------
                * Nhận mảng byte dữ liệu từ server gửi về
                * và chuyển sang chuỗi và hiển thị lên màn hình
                * -----------------------------------------------*/
                int bufferSize = server.getReceiveBufferSize();
                byte[] bytes = new byte[bufferSize];
                if (din.read(bytes) >= 0) {
                    String message = new String(bytes);
                    if (message.trim().compareTo("") == 0) {
                        return;
                    }
                    JOptionPane.showMessageDialog(null, message);
                }

            } catch (IOException ex) {
                closeConnection();
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Đóng kết nối tới server
     */
    public void closeConnection() {
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
