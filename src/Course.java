package miniprojects.schooldb;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class that creates a course
 * @author Michael Waller
 */
public class Course implements Comparable<Course>{
    private String name;
    private int numOfCredits, courseNumber;
    private Department department;
    private List<DBClass> classes;

    /**
     * constructor
     * @param name takes in the of the course
     */
    Course(String name, int courseNumber) {
        this.name = name;
        String[] arr = name.split(" ");
        this.courseNumber = courseNumber;
        classes = new ArrayList<>();
    }

    /**
     * constructor
     * @param name takes in the of the course
     * @param numOfCredits takes in the number of credits the course will be
     */
    Course(String name, int courseNumber, int numOfCredits) {
        this(name,courseNumber);
        this.numOfCredits = numOfCredits;
    }

    /**
     * constructor
     * @param name takes in the of the course
     * @param numOfCredits takes in the number of credits the course will be
     * @param department takes in the department of the course
     */
   Course(String name, int numOfCredits, Department department){
        this(name,numOfCredits);
        this.department = department;
        department.addCourse(this);
    }

    /**
     * This method adds classes for this course
     * @param DBClassToAdd takes in the class to add
     */
    void addClass(DBClass DBClassToAdd) {
        classes.add(DBClassToAdd);
    }

    /**
     * adds an array of classes to the Course
     * @param classesToAdd
     */
    void addClasses(DBClass[] classesToAdd) {
        classes.addAll(Arrays.asList(classesToAdd));
    }

    /**
     *
     * @param classesToAdd takes in the Generic Object that extends the Collection api
     * @param <E> An Object that extends the Collection api
     * @see Collection
     */
    <E extends Collection<DBClass>> void addClasses(E classesToAdd) {
        classes.addAll(classesToAdd);
    }

    /**
     *
     * @return returns a copy of the current list of classes for this Course
     */
    public List<DBClass> listOfClasses() {
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
        return "Course{" +
                "name='" + name + '\'' +
                ", courseNumber=" + courseNumber +
                ", numOfCredits=" + numOfCredits +
                ", department=" + department +
                ", classes=" + classes +
                '}';
    }

    /**
 * @author Michael Waller
 * A class for an already existing course
 */
final class DBClass implements Comparable<DBClass> {
    private String session;
    private int capacity, seatsTaken = 0;
    private boolean hasTeacher;
    private Instructor instructor;
    private Course course;
    private Student[] students;
    private Queue<Student> studentQueue;

    /**
     * constructor
     * @param session takes in the of the course
     */
    DBClass(String session) {
        this.session = session;
        course = Course.this;
        capacity = 30;
        students = new Student[capacity];
        studentQueue = new LinkedBlockingQueue<>(30);
    }

    /**
     * constructor
     * @param session takes in the of the course
     * @param instructor takes in the name of the instructor of the course
     */
    DBClass(String session, Instructor instructor) {
        this(session);
        this.instructor = instructor;
        hasTeacher = true;
    }

    /**
     * constructor
     * @param session takes in the of the course
     * @param instructor takes in the name of the instructor of the course
     * @param capacity takes in the capacity of students this class can hold
     */
    DBClass(String session, Instructor instructor, int capacity) {
        this(session, instructor);
        this.capacity = capacity;
        students = new Student[capacity];
    }

    /**
     * constructor
     * @param session takes in the of the course
     * @param instructor takes in the name of the instructor of the course
     * @param capacity takes in the capacity of students this class can hold
     * @param waitlistCapacity takes in the capacity of wait-listed students
     */
    DBClass(String session, Instructor instructor, int capacity, int waitlistCapacity) {
        this(session, instructor, capacity);
        studentQueue = new LinkedBlockingQueue<>(waitlistCapacity);
        students = new Student[capacity];
    }

    /**
     * adds a student to the class
     * @param student takes in the student to add
     */
    public void addStudent(Student student) {
        int length = students.length;

        //adds the students from the queue while there is space in the class
        while(seatsTaken < length && !studentQueue.isEmpty())
            students[seatsTaken++] = studentQueue.poll();

        //adds student to the class or queue
        if(seatsTaken < length) students[seatsTaken++] = student;
        else studentQueue.add(student);
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
        if(!studentQueue.isEmpty()) students[seatsTaken++] = studentQueue.poll();
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
    public String getSession() {
        return session;
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
        return "Class{" +
                "course=" + course.getName() + " " + course.getCourseNumber() +
                ", session='" + session + '\'' +
                ", instructor=" + instructor +
                ", capacity=" + capacity +
                ", seatsTaken=" + seatsTaken +
                ", students=\n" + Arrays.toString(Arrays.stream(students)
                .limit(seatsTaken)
                .map(s -> s.getFirstName() + " " + s.getLastName())
                .toArray());
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
        DBClass aDBClass = (DBClass) o;
        return capacity == aDBClass.capacity && seatsTaken == aDBClass.seatsTaken
                && hasTeacher == aDBClass.hasTeacher && Objects.equals(session, aDBClass.session)
                && Objects.equals(instructor, aDBClass.instructor) && Objects.equals(course, aDBClass.course)
                && Arrays.equals(students, aDBClass.students) && Objects.equals(studentQueue, aDBClass.studentQueue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(session, capacity, seatsTaken, hasTeacher, instructor, course, studentQueue);
        result = 31 * result + Arrays.hashCode(students);
        return result;
    }

        @Override
        public int compareTo(DBClass o) {
            return this.session.compareTo(o.session);
        }
    }

    public static void main(String[] args) {
        List<Course> courseList = new ArrayList<>(List.of(
                new Course("CMP", 433),
                new Course("ANT", 141 ),
                new Course("CMP", 232),
                new Course("BIO", 323)
        ));

        System.out.println("Unsorted List");
        courseList.forEach(System.out::println);

        Collections.sort(courseList);

        System.out.println("Sorted List");
        courseList.forEach(System.out::println);

    }

}
