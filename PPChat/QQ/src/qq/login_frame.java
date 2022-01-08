package qq;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.awt.Font;

@SuppressWarnings({ "serial", "deprecation" })
public class login_frame extends JFrame implements Observer{

	private JPanel contentPane;
	private JTextField passport;
	private JTextField account;
	private JButton btn_login;
	private JButton btn_register;
	private JLabel lblNewLabel;
	private datacenter dc;
	/**
	 * Create the frame.
	 */
	public login_frame(datacenter d) {
		//观察者
		dc = d;
		dc.addObserver(this);
		
		setTitle("\u767B\u9646--PP\u804A\u5929\u5BA4");		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{123, 166, 0, 117, 0};
		gbl_contentPane.rowHeights = new int[]{31, 31, 31, 31, 31, 31, 31, 31, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblNewLabel = new JLabel("\u6B22\u8FCE\u4F7F\u7528PP\u804A\u5929\u5BA4");
		lblNewLabel.setFont(new Font("幼圆", Font.BOLD, 23));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel label_1 = new JLabel("Account: ");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 3;
		contentPane.add(label_1, gbc_label_1);
		
		account = new JTextField();
		GridBagConstraints gbc_account = new GridBagConstraints();
		gbc_account.fill = GridBagConstraints.BOTH;
		gbc_account.insets = new Insets(0, 0, 5, 5);
		gbc_account.gridx = 1;
		gbc_account.gridy = 3;
		contentPane.add(account, gbc_account);
		account.setColumns(25);
		
		JLabel label_2 = new JLabel("Passport: ");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.fill = GridBagConstraints.VERTICAL;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 4;
		contentPane.add(label_2, gbc_label_2);
		
		passport = new JTextField();
		GridBagConstraints gbc_passport = new GridBagConstraints();
		gbc_passport.fill = GridBagConstraints.BOTH;
		gbc_passport.insets = new Insets(0, 0, 5, 5);
		gbc_passport.gridx = 1;
		gbc_passport.gridy = 4;
		contentPane.add(passport, gbc_passport);
		passport.setColumns(25);
		
		btn_login = new JButton("     Login     ");
		btn_login.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: 向【服务器】发送帐号密码，获取响应决定【跳转chat】或弹【对话框：登陆失败】
				String account_number = account.getText();
				String passport_detail = passport.getText();
				String buf = "login:"+account_number+"&"+passport_detail;
				try {
					dc.remoteOut.writeUTF(buf);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btn_login = new GridBagConstraints();
		gbc_btn_login.fill = GridBagConstraints.BOTH;
		gbc_btn_login.insets = new Insets(0, 0, 5, 5);
		gbc_btn_login.gridx = 1;
		gbc_btn_login.gridy = 6;
		contentPane.add(btn_login, gbc_btn_login);
		
		btn_register = new JButton("register");
		btn_register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dc.setvisible(1);//register
			}
		});
		GridBagConstraints gbc_btn_register = new GridBagConstraints();
		gbc_btn_register.gridx = 3;
		gbc_btn_register.gridy = 7;
		contentPane.add(btn_register, gbc_btn_register);
	}
	
	public void update(Observable t, Object o) {
		if(dc.type == 3) {
			if(dc.login_ok)
				dc.setvisible(2);//chat
			else {
				JOptionPane.showMessageDialog(null, "Error", "密码错误或用户不存在", 
				JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
