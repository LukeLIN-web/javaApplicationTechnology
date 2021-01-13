package qq;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings({ "deprecation", "serial" })
public class register_frame extends JFrame implements Observer{

	private JTextField passport;
	private JTextField account_name;
	JLabel account = new JLabel("");
	JLabel account_detail = new JLabel("");
	JButton btnNewButton = null;
	JButton btn_regist = null;
	private datacenter dc;

	/**
	 * Create the frame.
	 */
	public register_frame(datacenter d) {
		//观察者
		dc = d;
		dc.addObserver(this);
		
		setTitle("\u6CE8\u518C--PP\u804A\u5929\u5BA4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 500, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{169, 74, 83, 0};
		gridBagLayout.rowHeights = new int[]{99, 0, 37, 125, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_account = new GridBagConstraints();
		gbc_account.anchor = GridBagConstraints.SOUTHEAST;
		gbc_account.insets = new Insets(0, 0, 5, 5);
		gbc_account.gridx = 0;
		gbc_account.gridy = 0;
		getContentPane().add(account, gbc_account);
		
		GridBagConstraints gbc_account_detail = new GridBagConstraints();
		gbc_account_detail.anchor = GridBagConstraints.SOUTHWEST;
		gbc_account_detail.insets = new Insets(0, 0, 5, 5);
		gbc_account_detail.gridx = 1;
		gbc_account_detail.gridy = 0;
		getContentPane().add(account_detail, gbc_account_detail);
		
		JLabel lblNewLabel_1 = new JLabel("Name: ");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		account_name = new JTextField();
		GridBagConstraints gbc_account_name = new GridBagConstraints();
		gbc_account_name.insets = new Insets(0, 0, 5, 5);
		gbc_account_name.fill = GridBagConstraints.HORIZONTAL;
		gbc_account_name.gridx = 1;
		gbc_account_name.gridy = 1;
		getContentPane().add(account_name, gbc_account_name);
		account_name.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Set Passport: ");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		passport = new JTextField();
		GridBagConstraints gbc_passport = new GridBagConstraints();
		gbc_passport.insets = new Insets(0, 0, 5, 5);
		gbc_passport.fill = GridBagConstraints.HORIZONTAL;
		gbc_passport.gridx = 1;
		gbc_passport.gridy = 2;
		getContentPane().add(passport, gbc_passport);
		passport.setColumns(25);
		
		btn_regist = new JButton("regster");
		btn_regist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: 发送姓名，密码去服务器
				try {
					dc.remoteOut.writeUTF(
					"register:"+account_name.getText()+"&"+passport.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		GridBagConstraints gbc_btn_regist = new GridBagConstraints();
		gbc_btn_regist.anchor = GridBagConstraints.NORTHWEST;
		gbc_btn_regist.insets = new Insets(0, 0, 0, 5);
		gbc_btn_regist.gridx = 1;
		gbc_btn_regist.gridy = 3;
		getContentPane().add(btn_regist, gbc_btn_regist);
		
		btnNewButton = new JButton("continue");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 3;
		btnNewButton.setVisible(false);
		getContentPane().add(btnNewButton, gbc_btnNewButton);
	}
	
	public void update(Observable t, Object o) {
		if(dc.type == 1) {//获取服务器自动分配的帐号，并显示
			account.setText("Account: ");
			account_detail.setText(dc.account+"");
			btnNewButton.setVisible(true);
			btn_regist.setVisible(false);
			btnNewButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dc.setvisible(2);//chat
				}
			});
		}
	}
}
