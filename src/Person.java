package miniprojects.schooldb;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static miniprojects.schooldb.Person.Gender.*;

/**
 * @author Michael Waller
 *
 * <br>
 * an abstract class Person class to be inherited from
 */
public abstract class Person implements Comparable<Person> {
    protected String firstName;
    protected String lastName;
    private final Gender gender;
    private final static Set<String> ids = new HashSet<>();
    private final LocalDate dob;
    private final String id;

    /**
     * a constructor that creates a Person
     * @param firstName takes in the first name of the person
     * @param lastName takes in the last name of the person
     * @param dob takes in the person's date of birth
     */
    Person(String firstName, String lastName, LocalDate dob, Gender gender) {
        try {
            checkAge(dob);
        } catch (InvalidAgeException e) {
            System.out.println("Invalid Age!");
            throw new RuntimeException(e);
        }

        if(!gender.equals(MALE) && !gender.equals(FEMALE)) gender = OTHER;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        id = createId();
    }

    /**
     * This method checks whether the date of birth the user gave is valid.
     * @param dob takes in the date of birth of the user
     * @throws InvalidAgeException when the age is invalid
     */
    final void checkAge(LocalDate dob) throws InvalidAgeException {
        if(!dob.isBefore(LocalDate.now())) throw new InvalidAgeException("Person is too young");
    }

    /**
     * gets this persons id
     * @return returns the id
     */
    String getId() {
        return id;
    }

    /**
     * sets the date of birth of the person
     * @param dob takes in the date of birth
     */
    public void setDob(LocalDate dob) throws InvalidAgeException {
        checkAge(dob);
    }

    /**
     *
     * @return returns the first name of the person
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the first name of the person
     * @param firstName takes in the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return last name of the person
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *  sets the person's last name
     * @param lastName takes in the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return returns the date of birth of the person
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * This method creates a unique id for every person using the first two letters of their last name followed by
     * a 7-digit number ending with their last initial. This method is only called with a new instance
     * of person is created
     * @return returns the unique id of this person
     */
    private String createId() {
        Random random = new Random();

        while(true) {
            int numb = random.nextInt(9_000_000) + 1_000_000;
            String uniqueId = getFirstName().substring(0,2) + "" + numb + "" + getLastName().charAt(0);
            if(!ids.contains(uniqueId)) {
                ids.add(uniqueId);
                return uniqueId;
            }
        }
    }

    /**
     * this method checks whether the object taken in has values of this instance
     * @param o takes in the object to check for equality
     * @return returns true or false based on the equality of the two objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) && gender == person.gender &&
                Objects.equals(dob, person.dob) && Objects.equals(id, person.id);
    }

    /**
     *
     * @return returns the hashcode of this instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, dob, id);
    }

    /**
     *
     * @param o the object to be compared.
     * @return the person in alphabetical order
     */
    @Override
    public int compareTo(Person o) {
        return this.lastName.compareTo(o.lastName);
    }

    /**
     * Three Genders can be chosen from this enum. MALE, FEMALE, OTHER
     */
    enum Gender {
        MALE, FEMALE, OTHER
    }
}


