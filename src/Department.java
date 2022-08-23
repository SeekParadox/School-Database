package miniprojects.schooldb;
import java.util.*;

/**
 * This class creates a Department of the School
 * @author Michael Waller
 * @since 7/31/2022
 */
public class Department {
    protected String name;
    private Map<Level, LinkedHashSet<Course>> courseCatalog;
    private List<Instructor> instructors = new ArrayList<>();

    /**
     * constructor
     * @param name takes in the name of the department
     */
    public Department(String name) {
        this.name = name;
        courseCatalog = new HashMap<>();
        Arrays.stream(Level.values()).forEach(level -> courseCatalog.put(level, new LinkedHashSet<>()));
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
        else courseCatalog.get(Level.Undecided).add(course);

    }

    public void addInstructor(Instructor instructor) {
        instructors.add(instructor);
    }


    public enum Level {
        Undergrad, Grad, Undecided
    }

}
