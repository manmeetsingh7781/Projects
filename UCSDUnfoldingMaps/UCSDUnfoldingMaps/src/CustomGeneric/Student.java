package CustomGeneric;

public class Student extends Person {

    private boolean graduated = false;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public boolean isGraduated() {
        return graduated;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    @Override
    public void doJob() {
        System.out.println("I am a " + getRole() + ", i am studying computer science");
    }
}
