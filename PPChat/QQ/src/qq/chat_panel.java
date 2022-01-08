package qq;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.SwingConstants;

@SuppressWarnings({ "deprecation", "serial" })
public class chat_panel extends JPanel implements Observer{
	datacenter dc;
	int Height;
	ImageIcon background;
	JScrollPane scrollPane;
	/**
	 * Create the panel.
	 */
	public chat_panel(datacenter d, JScrollPane s) {
		//�۲���
		this.dc = d;
		scrollPane = s;
		dc.addObserver(this);
		
		setPreferredSize(new Dimension(600, 100));
		setLayout(new GridLayout(0, 2, 0, 0));
		
		background =new ImageIcon("chatbox.jpg");
	    background.setImage(
	  		  background.getImage().
	  		  getScaledInstance(background.getIconWidth(),background.getIconHeight(), Image.SCALE_REPLICATE));
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblNewLabel_2);		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblNewLabel_3);
		
	}
	
	//�����������
	public void update(Observable t, Object o) {
		if(dc.type == 2) {//chat or file or image	
			String str = dc.chatString.substring(dc.chatString.indexOf(':')+1); 
			String head = dc.chatString.substring(0, dc.chatString.indexOf(':'));
			String info = str.substring(str.indexOf("<html>") + "<html>".length(), 
									str.indexOf("<br>")); 
			String detail = str.substring(str.indexOf("<br>")+"<br>".length(),
									str.lastIndexOf("<html>"));
			
			String temp = dc.chatString.substring(dc.chatString.indexOf(';') + 1);
			temp = temp.substring(0, temp.indexOf(';'));
			long account_receive = Long.valueOf(temp);	
			
			Height+=60;
			setPreferredSize(new Dimension(600, Height));
			
			if(account_receive!=dc.account) {
				addInfo(str, head, detail, info, true);
			}else {		
				addInfo(str, head, detail, info, false);
			}
			JScrollBar vertical = scrollPane.getVerticalScrollBar();  
	        vertical.setValue( vertical.getMaximum());  
	        validate();
		}
	}
	//����������������Ϣ
	public void addInfo(String str, String head, String detail, String info, boolean left) {
		imageLabel lblNewLabel_1;
		JLabel lblNewLabel_0;
		if(head.contains("chat")) {//������Ϣ
			lblNewLabel_1 = new imageLabel(str,background.getImage());
			JLabel lblNewLabel = new JLabel("");
			if(left) {//���˵���Ϣ
				lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
				lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				add(lblNewLabel_1);
				add(lblNewLabel);
			}
			else {//�Լ�����Ϣ
				lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
				lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
				add(lblNewLabel);
				add(lblNewLabel_1);
			}
		}else {//����������Ϣ�����ļ���ͼƬ
			lblNewLabel_0 = new JLabel(info);
			JLabel lblNewLabel = new JLabel("");
			if(left) {//���˵���Ϣ
				lblNewLabel_0.setHorizontalAlignment(SwingConstants.LEFT);
				lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				add(lblNewLabel_0);
				add(lblNewLabel);
			}
			else {//�Լ�����Ϣ
				lblNewLabel_0.setHorizontalAlignment(SwingConstants.RIGHT);
				lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
				add(lblNewLabel);
				add(lblNewLabel_0);
			}
		}
		
		
		if(head.contains("file")) {//���������ļ�
			JButton btnButton;
			JLabel lblNewLabel2 = new JLabel("");
			JLabel lblNewLabel3 = new JLabel("");
			//�ļ�����/filename\��ס
			String filename = str.substring(str.indexOf('/')+1, str.indexOf('\\'));
			//���ļ�btn
			btnButton = new JButton("open file: "+filename);
			btnButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					File file = new File(filename);
					try {
						// �������ڱ���������ע��Ĺ���Ӧ�ó��򣬴��ļ��ļ�file
						Desktop.getDesktop().open(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}
			});					
			if(left) {//���˵���Ϣ
				//�����ļ�
				File file = new File(filename);
				DataOutputStream ps;
				try {
					ps = new DataOutputStream(new FileOutputStream(file));			
					ps.write(dc.file);
					ps.close();
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				btnButton.setHorizontalAlignment(SwingConstants.LEFT);
				btnButton.setFont(new Font("����", Font.BOLD, 15));
				lblNewLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
				add(btnButton);
				add(lblNewLabel2);
			}
			else {//�Լ�����Ϣ
				lblNewLabel3.setText(filename+"�ѷ���");
				lblNewLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
				lblNewLabel2.setHorizontalAlignment(SwingConstants.LEFT);
				add(lblNewLabel2);
				add(lblNewLabel3);
			}			
		}
		validate();
	}
}

@SuppressWarnings("serial")
class imageLabel extends JLabel{
	Image img;
	String str;
	public imageLabel(String str, Image image) {
		super(str);
		this.str = str;
		img = image;
	}
	
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
		String info = str.substring(str.indexOf("<html>")+"<html>".length(), 
				str.indexOf("<br>"));
		String detail = str.substring(str.indexOf("<br>")+"<br>".length(), 
				str.lastIndexOf("<html>"));
		if(detail.equals(""))
			g.drawString(info, 35, 30);
		else {
			g.drawString(info, 35, 21);
			g.drawString(detail, 35, 41);
		}
	}
}