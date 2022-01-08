package fangT;


// 定义了程序中所有类共享的常量.
public interface FangTangConstants {
	public static int port = 9020;
	public static int CONNECTSUCCESS = 11;
	public static int CONNECTFAIL = 22;
	public static int STOPSEND =33;
	public static int CONTINUESEND = 44;
	public static int LOGIN = 55;
	public static int REGISTER = 66;
	//public static int SENDMESSAGE = 67;
	
//	class logrequest implements Serializable{
//		/**
//		 * 列化操作的时候系统会把当前类的serialVersionUID写入到序列化文件中，当反序列化时系统会去检测文件中的serialVersionUID，判断它是否与当前类的serialVersionUID一致，
//		 * 如果一致就说明序列化类的版本与当前类版本是一样的，可以反序列化成功，否则失败。
//		 */
//		private static final long serialVersionUID = 1L;//被static修饰的成员变量不能被序列化的，因为静态的变量不属于某个对象，而是整个类的，所以不需要随着对象的序列化而序列化。序列化的都是对象
//		int flag ;// flag =1 for login flag = 2 for register
//		int  id;//账号最多为2,147,483,647
//		String username;
//		String password; 
//		public logrequest(String name,String pwd,int flag) {
//			this.flag = flag;
//			this.username = name;
//			this.password = pwd;
//		}
//	}
}
