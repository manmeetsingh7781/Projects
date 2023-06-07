package CustomGeneric;

import javax.xml.soap.Text;

public abstract class Person{

    private String firstName, lastName;
    private int age;
    private String role;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        setRole();
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    private void setRole() {
        this.role = this.getClass().getName().split("\\.")[1];
    }

    public abstract void doJob();
}
