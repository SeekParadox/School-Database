package miniprojects.schooldb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee extends Person {

    Employee(String firstName, String lastName, LocalDate dob,Gender gender)  {
        super(firstName, lastName, dob, gender);
    }
}