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
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Label;

public class ClientFrame {
	private String fileName;
	private String pathFileSave = "/chat/";
	private final Integer SIZE_BYTE_ARRAYS = 1024 * 64;
	private int SOCKET_FILE_PORT = 4112;
	private static File fChoose; 
	private static Socket socket; 
	private static Socket socketFile;
	private boolean isConnected = false;
	private JFrame frame;
	private JTextField txtIp;
	private JTextField txtPort;
	private JTextField txtMsg;
	private JTextField txtUsername;
	private JTextPane txtContent;
	private JTextField txtSend;
	private JButton btnConnect;
	private JLabel lblFileChoose;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame window = new ClientFrame();
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
	public ClientFrame() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		/*----------------------------
		 * Tạo giao diện chương trình
		 *----------------------------*/
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 428);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("IPADDR");
		lblNewLabel.setBounds(25, 11, 89, 20);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblPort = new JLabel("PORT");
		lblPort.setBounds(25, 42, 89, 20);
		frame.getContentPane().add(lblPort);
		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setBounds(25, 73, 89, 20);
		frame.getContentPane().add(lblUsername);
		
		txtIp = new JTextField();
		txtIp.setText("localhost");
		txtIp.setHorizontalAlignment(SwingConstants.LEFT);
		txtIp.setBounds(124, 11, 124, 20);
		frame.getContentPane().add(txtIp);
		txtIp.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("1");
		txtPort.setColumns(10);
		txtPort.setBounds(124, 42, 124, 20);
		frame.getContentPane().add(txtPort);
		
		txtUsername = new JTextField();
		txtUsername.setText("a");
		txtUsername.setColumns(10);
		txtUsername.setBounds(124, 73, 124, 20);
		frame.getContentPane().add(txtUsername);
		
		txtMsg = new JTextField();
		txtMsg.setBounds(25, 283, 293, 20);
		frame.getContentPane().add(txtMsg);
		txtMsg.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSend.setBounds(328, 281, 73, 23);
		frame.getContentPane().add(btnSend);
		
		JLabel lblChnFile = new JLabel("FILE:");
		lblChnFile.setBounds(25, 309, 43, 19);
		frame.getContentPane().add(lblChnFile);
		
		JButton btnSendFile = new JButton("Send file");
		btnSendFile.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSendFile.setBounds(229, 307, 89, 23);
		frame.getContentPane().add(btnSendFile);
		
		JLabel lblauthorngTin = new JLabel("@Author: \u0110\u1EB7ng Ti\u1EBFn M\u1EA1nh IT2_K56_UTC");
		lblauthorngTin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblauthorngTin.setBounds(25, 357, 259, 20);
		frame.getContentPane().add(lblauthorngTin);
		
		btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConnect.setBounds(258, 11, 133, 37);
		
		frame.getContentPane().add(btnConnect);
		JButton btnChooseFile = new JButton("...");
		btnChooseFile.setBounds(65, 307, 43, 23);
		frame.getContentPane().add(btnChooseFile);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 104, 376, 167);
		frame.getContentPane().add(scrollPane);
		
		txtContent = new JTextPane();
		scrollPane.setViewportView(txtContent);
		
		Label label = new Label("Send (%)");
		label.setBounds(114, 309, 62, 19);
		frame.getContentPane().add(label);
		
		txtSend = new JTextField();
		txtSend.setBounds(179, 308, 40, 20);
		frame.getContentPane().add(txtSend);
		txtSend.setColumns(10);
		
		lblFileChoose = new JLabel("");
		lblFileChoose.setBounds(25, 334, 342, 22);
		frame.getContentPane().add(lblFileChoose);
		
		
		/*---------------------------
		 * Kết nối tới Server Socket
		 *---------------------------*/
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// Tạo một đối tượng socket để gửi văn bản và một đối tượng socket để gửi file ở client.
					socket = new Socket(txtIp.getText(), Integer.parseInt(txtPort.getText()));
					socketFile = new Socket(txtIp.getText(), SOCKET_FILE_PORT);
					//-
					txtIp.setEnabled(false);
					txtPort.setEnabled(false);
					txtUsername.setEnabled(false);
					
					if (isConnected) return;
					isConnected = true;
					/* Gửi thông tin kết nối tới server. */
					try {
						String sendMessage = "<----------  " + txtUsername.getText() + " is connected   ---------->";
						
						/* Khai báo luồng gửi dữ liệu tới server socket*/
						OutputStream os = socket.getOutputStream();
						DataOutputStream dout = new DataOutputStream(os);
						
						/* Chuyển từ chuỗi văn bản thành mảng byte và gửi lên server. */
						String tmp = new String(sendMessage);
						byte bytes[] = tmp.getBytes();
						dout.write(bytes, 0, bytes.length);
						dout.flush();

						/* Hiển thị tin nhắn lên màn hình */
						txtMsg.setText("");
						txtContent.setText(txtContent.getText() + sendMessage.trim() + "\n");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*-----------------------------------------------------------
					 * Tạo một luồng phục vụ việc nhận tin nhắn trả về từ Server
					 *-----------------------------------------------------------*/
					new Thread(new Runnable() {
						@Override
						public void run() {
							while (true) {
								try {
									/* Khái báo các luồng vào/ra dữ liệu*/
									InputStream is = socket.getInputStream();
									DataInputStream din = new DataInputStream(is);
									
									/* -----------------------------------------------
									 * Nhận mảng byte dữ liệu từ server gửi về 
									 * và chuyển sang chuỗi và hiển thị lên màn hình
									 * -----------------------------------------------*/
									int bufferSize = socket.getReceiveBufferSize();
									byte[] bytes = new byte[bufferSize];
									while (din.read(bytes) >= 0) {
										String message = new String(bytes);
										if(message.trim().compareTo("") == 0) continue;
										txtContent.setText(txtContent.getText() + message.trim() + "\n");
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
					
					/*-----------------------------------------------------------
					 * Tạo một luồng phục vu cho việc nhận file trả về từ Server
					 *-----------------------------------------------------------*/
					new Thread(() -> {
                                            while(true){
                                                try {
                                                    /* -----------------------------------------------
                                                    * Khai báo mảng byte và các luồng vào/ra dữ liệu
                                                    * phục vụ việc nhận các byte dữ liệu từ server
                                                    * -----------------------------------------------*/
                                                    byte[] bytearray = new byte[SIZE_BYTE_ARRAYS];
                                                    InputStream is = socketFile.getInputStream();
                                                    DataInputStream din = new DataInputStream(is);
                                                    fileName = din.readUTF();
                                                    FileOutputStream fos = new FileOutputStream(pathFileSave + fileName);
                                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                                    
                                                    /* Đọc từng mảng byte dữ liệu do server gửi đến và gộp vào file */
                                                    int count = 0;
                                                    while((count = is.read(bytearray)) >= 0){
                                                        bos.write(bytearray, 0, count);
                                                    }
                                                    bos.close();
                                                    isConnected = true;
                                                    btnConnect.doClick();
                                                } catch (NumberFormatException e) {
                                                    // TODO Auto-generated catch block
                                                } catch (IOException e) {
                                                    // TODO Auto-generated catch block
                                                }
                                            }
                                        }).start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
		/* Chọn file để gửi */
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChoose = new JFileChooser();
					fileChoose.showOpenDialog(null);
					fChoose = fileChoose.getSelectedFile();
					if (fileChoose != null){
						String fileName = fChoose.getAbsolutePath();
						lblFileChoose.setText(fileName);
					}
					
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		});
		

		/* Gửi tin nhắn văn bản tới servver. */
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String sendMessage = "[" + txtUsername.getText()+ "]" + ": " +  txtMsg.getText();
					
					OutputStream os = socket.getOutputStream();
					DataOutputStream dout = new DataOutputStream(os);
					
					// Chuyển từ xâu thành mảng byte và gửi lên server.
					String tmp = new String(sendMessage);
					byte bytes[] = tmp.getBytes();
					dout.write(bytes, 0, bytes.length);
					dout.flush();
					//-------------------------------------------------
					// Hiển thị tin nhắn lên màn hình
					txtContent.setText(txtContent.getText() + "[you]: " +  txtMsg.getText().trim() + "\n");
					txtMsg.setText("");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		/* Gửi file lên server. */
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int send;
					if (txtSend.getText().trim().compareTo("") == 0){
						send = 100;
					} else {
						send = Integer.parseInt(txtSend.getText());
					}
					sendFileToServer(txtIp.getText(), SOCKET_FILE_PORT, fChoose.getAbsolutePath(), send);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	/*
	 * Hàm phục vụ gửi file đến server.
	 * */
	private void sendFileToServer(String ip, int port, String fileLocation, int send) throws IOException{
		File file = new File(fileLocation);
		byte[] bytearray = new byte[(int) file.length()];
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		bis.read(bytearray, 0, bytearray.length);
		OutputStream os = socketFile.getOutputStream();
		DataOutputStream dout = new DataOutputStream(os);
		dout.writeUTF(file.getName());
		dout.flush();
		
		/* Gửi message thông báo đến server */
		String sendMessage = "[" + txtUsername.getText()+ "]" + ": đã gửi file [" + file.getName() + "]";
		
		OutputStream osSocket = socket.getOutputStream();
		DataOutputStream doutSocket = new DataOutputStream(osSocket);
		
		// Chuyển từ xâu thành mảng byte và gửi lên server.
		String tmp = new String(sendMessage);
		byte bytes[] = tmp.getBytes();
		doutSocket.write(bytes, 0, bytes.length);
		doutSocket.flush();
		//-------------------------------------------------
		// Hiển thị tin nhắn lên màn hình
		txtContent.setText(txtContent.getText() + "[you]: đã gửi file [" + file.getName() + "]\n");
		txtMsg.setText("");
		
		// Gửi byte dữ liệu đến server
		bis.close();
		os.write(bytearray, 0, (int)(bytearray.length * (float)(send*1.0/ 100)));
		os.close();
		socketFile.close();
		socketFile = new Socket(ip, port);
	}
}
