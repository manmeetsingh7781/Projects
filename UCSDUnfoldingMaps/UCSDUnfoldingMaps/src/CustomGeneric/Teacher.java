package CustomGeneric;

public class Teacher extends Person {



    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }


    @Override
    public void doJob(){
        System.out.println("My Role is: " + this.getRole());
    }


}
