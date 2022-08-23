package miniprojects.schooldb;

import java.time.LocalDate;

public class Student extends Person{
    private Course [] courses;

    Student(String firstName, String lastName, LocalDate dob, Gender gender) throws InvalidAgeException {
        super(firstName, lastName, dob,gender);
    }


    @Override
    public int compareTo(Person o) {
        if(o instanceof Student that) {
            return 0;
        } else return super.compareTo(o);
    }
}
