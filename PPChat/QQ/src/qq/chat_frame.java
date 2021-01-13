package qq;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Date;
import java.awt.*;

@SuppressWarnings("serial")
public class chat_frame extends JFrame{

	private JPanel contentPane;
	private JPanel plane_input;
	JButton btnNewButton;
	JScrollPane scrollPane;
	String info;//消息状态
	String detail;//消息内容
	datacenter dc;

	/**
	 * Create the frame.
	 */
	public chat_frame(datacenter d) {
		dc = d;
		setTitle("PP聊天室");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 30, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_info = new JPanel();
		contentPane.add(panel_info, BorderLayout.NORTH);
		
		plane_input = new JPanel();
		plane_input.setPreferredSize(new Dimension(800, 100));
		contentPane.add(plane_input, BorderLayout.SOUTH);
		plane_input.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel = new JPanel();
		plane_input.add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(3, 0, 0, 0));
		plane_input.add(panel_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setColumns(70);
		textArea.setRows(5);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		panel.add(textArea, gbc_textArea);
		
		JButton btn_send = new JButton("Send");
		btn_send.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: send information to Server
				info = dc.name+";"+dc.account+";"+new Date().toString();
				detail = textArea.getText();
				String output = "chat:"+
				"<html>"+info+"<br>"+detail+"<html>";
				textArea.setText("");
				validate();
				try {
					dc.remoteOut.writeUTF(output);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btn_send = new GridBagConstraints();
		gbc_btn_send.insets = new Insets(0, 0, 5, 0);
		gbc_btn_send.fill = GridBagConstraints.BOTH;
		gbc_btn_send.gridx = 1;
		gbc_btn_send.gridy = 0;
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		panel_1.add(btn_send);
		
		btnNewButton = new JButton("File");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("resource")
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: send information to Server
				info = dc.name+";"+dc.account+";"+new Date().toString();
				String filename;
				//打开文件夹选择文件：
				JFileChooser fd = new JFileChooser();
				//fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fd.showOpenDialog(null);
				File f = fd.getSelectedFile();
				if(f != null){
					filename = f.getName();
					try {
						DataInputStream fileIn = new DataInputStream(new FileInputStream(f));
						
						//get data
						byte[] b = null;
						b = fileIn.readAllBytes();
						
						//send head
						String output = "file:"+"/"+filename+"\\"
								+"<size>"+b.length+"</size>"+
								"<html>"+info+"<br><html>";
						dc.remoteOut.writeUTF(output);
						btnNewButton.setText("Wait");
						//send data
						dc.remoteOut.write(b);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Error", "文件打开失败", 
					JOptionPane.ERROR_MESSAGE);
				}	
			}
		});
		btnNewButton.setFont(new Font("宋体", Font.BOLD, 15));
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.EAST);
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, 400));
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		chat_panel panel_3 = new chat_panel(dc, scrollPane);
		scrollPane.setViewportView(panel_3);
	}
}
