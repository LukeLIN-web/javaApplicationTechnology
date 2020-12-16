package databasebyjava;
import dto.People;

public class BuildClassManually {
	
	public static void main(String[] args) {
		 People people = new People();
		    people.setId(3);
		    people.setName("wangzan");
		    people.setAge(26);
		    people.setAddress("beijing");
		    int Id, Age;
		    String name,address;
		    Id = people.getId();
		    name = people.getName();
		    Age = people.getAge();
		    address =people.getAddress();
		    System.out.println(Id+name+Age+address);
		}
	
}
