package CustomGeneric;

public class Tester {

    public static void main(String[] args) {
        Person p = new Teacher("Manmeet", "Singh");
        Student s = new Student("Manmeet", "Singh");
        p.doJob();
        s.doJob();
        System.out.println(s.isGraduated());
         s.setAge(21);
    }
}
