package qq;

import java.io.DataOutputStream;
import java.util.Observable;

public //通过观察和通知来实现交互
@SuppressWarnings("deprecation")
class datacenter extends Observable{
	int visible = 0;//0--login visible 1--register 2--chat
	long account = -1;
	String name = "XXX";
	String chatString = null;
	boolean login_ok = false;
	int type = 0;//0--visible 1--account 2--chat 3--login
	byte[] file = null;
	//由于dc在众多需要输出的类内传播，因此将大家都需要的remoteOut放在这里
	DataOutputStream remoteOut = null;
	
	public void setvisible(int v) {
		visible = v;
		type = 0;
		setChanged();
		notifyObservers();
	}
	//注册顺便更新帐号名
	public void setacc(long account2, String name2) {
		account = account2;
		name = name2;
		type = 1;
		login_ok=true;
		setChanged();
		notifyObservers();
	}
	public void setchat(String v) {
		chatString = v;
		type = 2;
		setChanged();
		notifyObservers();
	}
	public void setchat(String v,byte[] b) {
		chatString = v;
		file = b;
		type = 2;
		setChanged();
		notifyObservers();
	}
	//登陆顺便更新帐号名
	public void setlogin(Boolean v, String name2, String account3) {
		login_ok = v;
		name = name2;
		account = Long.valueOf(account3);
		type = 3;
		setChanged();
		notifyObservers();
	}
}
