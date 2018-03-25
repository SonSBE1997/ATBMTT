package socket;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ServerFrame {

    final ArrayList<Socket> sockets = new ArrayList<Socket>(); //List: Chứa các socket đang kết nối tới server
    final ArrayList<Socket> socketsFile = new ArrayList<Socket>();
    private static ServerSocket serverSocket;
    private static ServerSocket serverSocketFile;
    private final Integer SIZE_BYTE_ARRAYS = 1024 * 64;
    private String pathFileSave = "E:\\";
    private final int PORT_SOCKET_FILE = 4112;
    private JFrame frame;
    private JTextField txtIp;
    private JTextField txtPort;
    private String fileName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerFrame window = new ServerFrame();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ServerFrame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        /*----------------
		 * Tạo giao diện
		 * --------------*/
        frame = new JFrame();
        frame.setBounds(100, 100, 284, 231);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("SERVER IP:");
        lblNewLabel.setBounds(39, 24, 76, 23);
        frame.getContentPane().add(lblNewLabel);

        try {
            txtIp = new JTextField(Inet4Address.getLocalHost().getHostAddress());
            txtIp.setEnabled(false);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        txtIp.setBounds(115, 24, 115, 20);
        frame.getContentPane().add(txtIp);
        txtIp.setColumns(10);

        JLabel lblPort = new JLabel("PORT");
        lblPort.setBounds(39, 53, 76, 23);
        frame.getContentPane().add(lblPort);

        txtPort = new JTextField();
        txtPort.setText("1");
        txtPort.setBounds(115, 53, 115, 20);
        frame.getContentPane().add(txtPort);
        txtPort.setColumns(10);

        JLabel lblauthorMnhItkutc = new JLabel("@Author: M\u1EA1nh IT2_K56_UTC");
        lblauthorMnhItkutc.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblauthorMnhItkutc.setBounds(39, 147, 203, 34);
        frame.getContentPane().add(lblauthorMnhItkutc);

        JButton btnStop = new JButton("Quit");
        btnStop.setBounds(36, 113, 194, 23);
        frame.getContentPane().add(btnStop);

        JButton btnCreateServer = new JButton("Create Server Socket");
        btnCreateServer.setBounds(36, 86, 194, 23);
        frame.getContentPane().add(btnCreateServer);

        /*-------------------------
		 * Khởi động Server Socket
		 * ------------------------*/
        btnCreateServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /* Tạo một server socket với cổng được nhập vào */
                            serverSocket = new ServerSocket(Integer.parseInt(txtPort.getText()));
                            serverSocketFile = new ServerSocket(PORT_SOCKET_FILE);
                            btnCreateServer.setText("Server is running");
                            btnCreateServer.setEnabled(false);
                            showDialog("Server Socket is created!");
                            while (true) {
                                /* Lắng nghe socket client kết nối tới server */
                                final Socket socket = serverSocket.accept();
                                /* Lắng nghe socket client nhận gửi file kết nối tới server */
                                final Socket socketFile = serverSocketFile.accept();
                                /* Thêm socket vào danh sách socket đang kết nối tới server */
                                sockets.add(socket);
                                socketsFile.add(socketFile);
                                showDialog(socket.getInetAddress().getHostAddress() + " is connected");

                                /*--------------------------------------------------------
								 * Tạo thêm một luồng phục vụ việc nhận file từ client gửi đến
								 * -------------------------------------------------------*/
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (true) {
                                            try {
                                                /* ------------------------------------------------------
												 * Nhận mảng byte từ client gửi đến 
												 * và gửi trả về tất cả client đang kết nối tới server 
												 * ------------------------------------------------------*/
                                                receiveAndSendFileFromClient(socketFile);
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                            }
                                        }
                                    }
                                }).start();

                                /*--------------------------------------------------------------------
								 * Tạo thêm một luồng phục vụ việc nhận tin nhắn văn bản từ client gửi đến 
								 * và gửi lại cho tất cả client đang kết nối với server
								 * -------------------------------------------------------------------*/
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            /* Khai báo luồng đọc, ghi dữ liệu */
                                            InputStream is = socket.getInputStream();
                                            DataInputStream din = new DataInputStream(is);

                                            /* Thực hiện đọc từng mảng byte do client gửi đến và chuyển sang xâu kí tự */
                                            int bufferSize = socket.getReceiveBufferSize();
                                            byte[] bytes = new byte[bufferSize];
                                            while (din.read(bytes) >= 0) {
                                                String message = new String(bytes);
                                                Arrays.fill(bytes, (byte) 0);

                                                /* Duyệt tất cả socket có kết nối tới server và gửi mảng byte dữ liệu cho socket client */
                                                for (Socket s : sockets) {
                                                    if (s == null) {
                                                        sockets.remove(s);
                                                    }
                                                    if (s != null && s != socket) {
                                                        OutputStream os = s.getOutputStream();
                                                        DataOutputStream dout = new DataOutputStream(os);

                                                        /* Gửi mảng byte dữ liệu đến client */
                                                        String tmp = message;
                                                        byte bytesSend[] = tmp.getBytes();
                                                        dout.write(bytesSend, 0, bytesSend.length);
                                                        dout.flush();
                                                    }

                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }
        });

        /*Thoát chương trình*/
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(1);
            }
        });
    }

    /*--------------------------------------------------------------------------
	 * Đọc các byte dữ liệu riêng rẽ do socket client gửi đến và gộp thành file 
	 * và gửi các byte dữ liệu đến các socket client đang kết nối với server
	 *--------------------------------------------------------------------------*/
    private void receiveAndSendFileFromClient(Socket socketFile) throws IOException {
        /*Nhận mảng từng mảng byte riêng rẽ nhau do client gửi đến*/
        byte[] bytes = new byte[SIZE_BYTE_ARRAYS];
        InputStream is = socketFile.getInputStream();
        DataInputStream din = new DataInputStream(is);
        fileName = din.readUTF();
        FileOutputStream fos = new FileOutputStream(pathFileSave + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        /*-----------------------------------------------------------------
	     * Đọc các byte riêng rẽ dữ liệu từ client gửi lên 
	     * và gộp vào file "pathFileSave + fileName" được khai báo ở trên.
	     * ----------------------------------------------------------------*/
        int count = 0;
        while ((count = is.read(bytes)) > 0) {
            bos.write(bytes, 0, count);
        }

        bos.close();

        /*---------------------------------------------------------------------
	     * Gửi các byte dữ liệu đến các socket client đang kết nối với server
	     *--------------------------------------------------------------------*/
        for (Socket s : socketsFile) {
            if (s != null && !s.equals(socketFile)) {
                File file = new File(pathFileSave + fileName);
                byte[] bytearr = new byte[(int) file.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytearr, 0, bytearr.length);
                OutputStream os = s.getOutputStream();

                DataOutputStream dout = new DataOutputStream(os);
                dout.writeUTF(file.getName());
                dout.flush();

                os.write(bytearr, 0, bytearr.length);

                bis.close();
                os.flush();
                os.close();
                s.close();

            }
        }
    }

    /* Hiển thị thông báo trong chương trình. */
    private void showDialog(String message) {
        JOptionPane pane = new JOptionPane(message);
        JDialog dialog = pane.createDialog("Notice");
        dialog.setLocation(frame.getX() + frame.getWidth() / 2 - dialog.getWidth() / 2, frame.getY() + frame.getHeight() / 2 - dialog.getHeight() / 2);
        dialog.setVisible(true);
    }
}
