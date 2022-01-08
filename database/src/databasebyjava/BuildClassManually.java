package databasebyjava;

//it use for test automatically generated class. 2020.12.20
public class BuildClassManually {
	
	public static void main(String[] args) {
//		 	People people = new People();
//		    people.setId(3);
//		    people.setName("wangzan");
//		    people.setAge(26);
//		    people.setAddress("beijing");
//		    int Id, Age;
//		    String name,address;
//		    Id = people.getId();
//		    name = people.getName();
//		    Age = people.getAge();
//		    address =people.getAddress();
//		    System.out.println(Id+name+Age+address);
//		    t_user usr =  new t_user(1);
//		    System.out.println(usr.ADDRESS+"name = "+usr.NAME+usr.UUID+usr.AGE);
		people p1 = new people(1);
		p1.add(19,"'weiu'",20,"'shier'");//p1.add(19,"weiu",20,"shier"); will have error : “WEIU” not in any table FROM list
		System.out.println(p1);
		t_user usr = new t_user(1);
		usr.add("'uiwe'","\'ljy\'",21,"'hagnzhou'");// notice : we need add '' to input string.
		}
}
