package miniprojects.schooldb;

import miniprojects.schooldb.Course.DBClass;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * A class that creates an Instructor for the school
 */
public class Instructor extends Employee {
    private Department department;
    private DBClass aClass;
    private final DBClass [] classes;
    private int size = 0;

    /**
     * a constructor
     * @param firstName takes in the first name of the Instructor.
     * @param lastName takes in the last name of the Instructor.
     * @param dob takes in the Date of Birth of the Instructor.
     * @param gender takes in the Gender of the Instructor.
     */
    Instructor(String firstName, String lastName, LocalDate dob, Gender gender) {
        super(firstName, lastName, dob, gender);
        classes = new DBClass[10];
    }

    /**
     * a constructor
     * @param firstName takes in the First name of the Instructor.
     * @param lastName takes in the Last name of the Instructor.
     * @param dob takes in the Date of Birth of the Instructor.
     * @param gender takes in the Gender of the Instructor.
     * @param department takes in the department of the Instructor.
     */
    public Instructor(String firstName, String lastName, LocalDate dob, Gender gender, Department department) {
        this(firstName, lastName, dob , gender);
        this.department = department;
        department.addInstructor(this);
        //this.courses = courses;
    }

    /**
     * this method adds a class to this Instructors class array.
     * @param c takes in the Class to be added to the instructors taught classes.
     */
    public void addClass(DBClass c) {
        if(!teachesClass(c) && size < classes.length) {
            classes[size++] = c;
            c.setInstructor(this);
        }
        else
            System.out.println("Unable to add course!");
    }

    /**
     *
     * @return returns the number of classes taught by this Instructor.
     */
    public int numOfClassTaught() {
        return size;
    }

    /**
     *
     * @return returns an array of classes taught by this Instructor.
     */
    public DBClass[] getClasses() {
        if(size == 0) throw new NullPointerException("Array is empty");
        return Arrays.stream(classes).limit(size).toArray(DBClass[]::new);
    }

    /**
     * A method that gives the class at the chosen index
     * @param index takes in the index of the class in the class array
     * @return returns the class at the given index
     * @throws IndexOutOfBoundsException throws NullPointerException when the index is out of Bounds
     */
    public DBClass getClassAtIndex(int index) {
        if(index >= 0 && index < size)
            return classes[index];
        throw new IndexOutOfBoundsException(getFirstName() + " " + getLastName() + " does not teach this class.");
    }

    /**
     * A method that sets the department of this Instructor.
     * @param department takes in the department to be added to this Instructor.
     */
    public void setDepartment(Department department) {
        this.department = department;
        department.addInstructor(this);
    }

    /**
     * A method that returns if the class is taught by this Instructor.
     * @param e takes in the class.
     * @return returns a boolean value if whether the class is taught by this Instructor.
     */
    public boolean teachesClass(DBClass e) {
        return size != 0 && Arrays.asList(classes).contains(e);
    }

    /**
     * A method that returns a String representing this Instructor.
     * @return returns the String value of this Instructor.
     */
    @Override
    public String toString() {
        return String.format("%s",getFirstName());
//         "Instructor{" +
//                "department=" + department +
//                ", courses=" + Arrays.toString(courses) +
//                ", course=" + course +
//                ", current=" + current +
//                '}';
    }

}
