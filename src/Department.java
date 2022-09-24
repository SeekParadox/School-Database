package miniprojects.schooldb;
import java.time.LocalDate;
import java.util.*;

/**
 * This class creates a Department of the School
 * @author Michael Waller
 * @since 7/31/2022
 */
public class Department {
    protected String name;
    private static Map<Level, TreeSet<Course>> courseCatalog;
    private static List<Instructor> instructors = new ArrayList<>();

    /**
     * constructor
     * @param name takes in the name of the department
     */
    public Department(String name) {
        this.name = name;
        courseCatalog = new HashMap<>();
        Arrays.stream(Level.values()).forEach(level -> courseCatalog.put(level, new TreeSet<>()));
    }

    /**
     * this method adds a course to the course catalog for this department
     * @param course takes in the course to be added
     */
    public void addCourse(Course course) {
        //      for(LinkedHashSet<Course> courses : courseCatalog.values()) {if(courses.contains(course)) return;}
      if(courseCatalog.values().stream().anyMatch(values -> values.contains(course))) return;

      if(course.getCourseNumber() < 500) courseCatalog.get(Level.Undergrad).add(course);
        else if(course.getCourseNumber() >= 500) courseCatalog.get(Level.Grad).add(course);
        else courseCatalog.get(Level.Non_Degree).add(course);

    }

    public void addInstructor(Instructor instructor) {
        instructors.add(instructor);
    }


    public enum Level {
        Undergrad, Grad, Non_Degree
    }
    public void printCourseLevels() {
        Arrays.stream(Level.values()).forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) throws InvalidAgeException {
        Department firstDeparment = new Department("Math");
        System.out.println(firstDeparment);
        firstDeparment.printCourseLevels();
        Course course = new Course("ABC", 232,3, null);
        Course.Clazz f = course.new Clazz("CAS");
//        System.out.println(course.listOfClasses());
        for(int i = 0; i < 100; i++) {
            try {
                f.addStudent(new Student("Michael", "Joe", LocalDate.of(2004, 3, 21), Person.Gender.MALE));
            }catch (IllegalStateException illegalStateException) {
                System.out.println(illegalStateException.getMessage());
                break;
            }
        }
//        course.addClass(course.new Clazz("33rsd"));
//        f.setInstructor();
        System.out.println(f);
        f.printQueueSize();
        Student[] students = f.getStudents();
        System.out.println(students[0]);
        f.removeStudent(students[0]);
        f.printQueueSize();
    }

}
