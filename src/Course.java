package miniprojects.schooldb;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that creates a course
 * @author Michael Waller
 */
public class Course implements Comparable<Course>{
    private String name;
    private int numOfCredits;
    private final int courseNumber;
    private Department department;
    private List<Clazz> classes;
    private int classCount = 0;

    /**
     * constructor
     * @param name takes in the of the course
     */
    Course(String name, int courseNumber, int numOfCredits, Department department ) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,4}$");
        Matcher matcher = p.matcher(name);

        if(!matcher.matches()) throw new RuntimeException("Invalid Course Name");

        p = Pattern.compile("^[1-8][0-9]{2,3}$");
        matcher = p.matcher(String.valueOf(courseNumber));

        if(!matcher.matches()) throw new RuntimeException("Invalid Course Number");

        this.name = name.toUpperCase();
        this.courseNumber = courseNumber;
        this.numOfCredits = numOfCredits;
        this.department = department;

        if(department != null) department.addCourse(this);
        classes = new ArrayList<>();
    }

    /**
     * This method adds classes for this course
     * @param ClazzToAdd takes in the class to add
     */
    private void addClass(Clazz ClazzToAdd) {
        classes.add(ClazzToAdd);
        classCount++;
    }

    /**
     * adds an array of classes to the Course
     * @param classesToAdd
     */
    private void addClasses(Clazz[] classesToAdd) {
        for(Clazz c : classesToAdd) {
            addClass(c);
        }
    }

    /**
     *
     * @param classesToAdd takes in the Generic Object that extends the Collection api
     * @param <E> An Object that extends the Collection api
     * @see Collection
     */
    private <E extends Collection<Clazz>> void addClasses(E classesToAdd) {
        classes.addAll(classesToAdd);
    }

    /**
     *
     * @return returns a copy of the current list of classes for this Course
     */
    public List<Clazz> listOfClasses() {
        return List.copyOf(classes);
    }

    /**
     * sets the name of the Course. This is useful for changing the name of the course at a later date.
     * @param name takes in the name of the course
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return returns the name of this instance of 'Course'.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return returns the number of credits for this instance of 'Course'
     */
    public int getNumOfCredits() {
        return numOfCredits;
    }

    /**
     * Sets the number of credits for this course. Useful for revising the curriculum.
     * @param numOfCredits takes in the number of Credits for this course.
     */
    void setNumOfCredits(int numOfCredits) {
        this.numOfCredits = numOfCredits;
    }

    /**
     * This method sets the department of the course. This is useful for revising the curriculum.
     * @param department takes in the department for this course.
     */
    void setDepartment(Department department) {
        this.department = department;
        department.addCourse(this);
    }

    /**
     *
     * @return returns the department of this instance of course.
     */
    public Department getDepartment() {
        return department;
    }

    /**
     *
     * @return returns the course number
     */
    public int getCourseNumber() {
        return courseNumber;
    }

    /**
     *
     * @param o the object to be compared.
     * @return returns the index of the compared Object
     */
    @Override
    public int compareTo(Course o) {
        int compare = this.name.compareTo(o.name);
        if(compare != 0) return compare;
        return this.courseNumber - o.courseNumber ;
    }

    @Override
    public String toString() {
        return  name + " " + courseNumber +
                " Number of Credits = " + numOfCredits +
                ", department = "  + department +
                ", classes offered = " + classCount;
    }

    /**
 * @author Michael Waller
 * A class for an already existing course
 */
final class Clazz implements Comparable<Clazz> {
    private final String section;
    private int capacity, seatsTaken = 0;
    private boolean hasTeacher;
    private Instructor instructor;
    private final Course course;
    private Student[] students;
    private Queue<Student> studentQueue;

    /**
     * constructor
     * @param section takes in the of the course
     */
    Clazz(String section) {
        this.section = section;
        course = Course.this;
        Course.this.addClass(this);
        capacity = 30;
        students = new Student[capacity];
        studentQueue = new LinkedBlockingQueue<>(30);
    }

    /**
     * constructor
     * @param section takes in the of the course
     * @param instructor takes in the name of the instructor of the course
     */
    Clazz(String section, Instructor instructor) {
        this(section);
        this.instructor = instructor;
        hasTeacher = true;
    }

    /**
     * constructor
     * @param section takes in the of the course
     * @param instructor takes in the name of the instructor of the course
     * @param capacity takes in the capacity of students this class can hold
     */
    Clazz(String section, Instructor instructor, int capacity) {
        this(section, instructor);
        this.capacity = capacity;
        students = new Student[capacity];
    }

    /**
     * constructor
     * @param section takes in the of the course
     * @param instructor takes in the name of the instructor of the course
     * @param capacity takes in the capacity of students this class can hold
     * @param waitlistCapacity takes in the capacity of wait-listed students
     */
    Clazz(String section, Instructor instructor, int capacity, int waitlistCapacity) {
        this(section, instructor, capacity);
        studentQueue = new LinkedBlockingQueue<>(waitlistCapacity);
        students = new Student[capacity];
    }

    /**
     * adds a student to the class
     * @param student takes in the student to add
     */
    public void addStudent(Student student) throws IllegalStateException {
        int length = students.length;

        //adds the students from the queue while there is space in the class

            while (seatsTaken < length && !studentQueue.isEmpty())
                students[seatsTaken++] = studentQueue.poll();

            //adds student to the class or queue

            if (seatsTaken < length) students[seatsTaken++] = student;

            else studentQueue.add(student);
    }

        public void printQueueSize() {
            System.out.println(studentQueue.size());
        }

        public Student[] getStudents() {
            return students;
        }

        /**
     * A method that adds a Collection of Students to the Class.
     * @param students takes in a Collection subclass of Students.
     * @param <E>
     */
    <E extends Collection<Student>> void addStudents(E students) {
        for (Student student: students) {
            addStudent(student);
        }
    }

    /**
     *
     * @param student takes in the student to remove from the class
     */
    void removeStudent(Student student) {
        List<Student> tempList = new ArrayList<>(Arrays.stream(students).toList());
        if(!tempList.contains(student)) return;
        tempList.remove(student);
        students = tempList.toArray(new Student[0]);
        seatsTaken--;
        if(!studentQueue.isEmpty()) students[++seatsTaken] = studentQueue.poll();
    }

    /**
     * A method that removes students from the class
     * @param students takes in students to be removed
     * @param <E> generic subclass of Collections
     */
    <E extends Collection<Student>> void removeStudents(E students) {
        students.forEach(this::removeStudent);
    }

    /**
     *
     * @param instructor takes in the instructor to be added to the class
     */
    void setInstructor(Instructor instructor) {
        this.instructor = instructor;
        hasTeacher = true;
    }

    /**
     *
     * @return the instructor of the class
     */
    public Instructor getInstructor() {
        return instructor;
    }

    /**
     *
     * @return the name of the course
     */
    public String getSection() {
        return section;
    }

    /**
     *
     * @return if the class has an instructor or not
     */
    public boolean hasTeacher() {
        return hasTeacher;
    }

    /**
     * A method that returns a String value of Class
     * @return returns a String value of this instance of Class.
     */
    @Override
    public String toString() {
        return String.format("Course = %s %d, Instructor = %s, Session = %s, Capacity = %d, Seats Taken = %d "
                , course.getName(), course.getCourseNumber(), instructor != null ? instructor : "Not Available"
                , getSection(), capacity,seatsTaken);
    }

    /**
     * A method that compares two Objects for equality based on their field variables.
     * @param o takes in the Object to validated for equality.
     * @return returns a boolean value based on the equality of this instance of class and the Object o's instance.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz aClazz = (Clazz) o;
        return capacity == aClazz.capacity && seatsTaken == aClazz.seatsTaken
                && hasTeacher == aClazz.hasTeacher && Objects.equals(section, aClazz.section)
                && Objects.equals(instructor, aClazz.instructor) && Objects.equals(course, aClazz.course)
                && Arrays.equals(students, aClazz.students) && Objects.equals(studentQueue, aClazz.studentQueue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(section, capacity, seatsTaken, hasTeacher, instructor, course, studentQueue);
        result = 31 * result + Arrays.hashCode(students);
        return result;
    }

        @Override
        public int compareTo(Clazz o) {
            return this.section.compareTo(o.section);
        }
    }

    public static void main(String[] args) {
        List<Course> courseList = new ArrayList<>(List.of(
                new Course("CMP", 133, 0, null),
                new Course("ANT", 141, 0, null),
                new Course("CMP", 232, 0, null),
                new Course("BIO", 321, 0, null)
        ));

        System.out.println("Unsorted List");
        courseList.forEach(System.out::println);

        Collections.sort(courseList);

        System.out.println("Sorted List");
        courseList.forEach(System.out::println);


    }

}
