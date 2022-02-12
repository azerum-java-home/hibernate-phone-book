package phonebook.entity;

import javax.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {
    public static final int MAX_NAME_PART_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = MAX_NAME_PART_LENGTH)
    private String firstName;

    @Column(nullable = false, length = MAX_NAME_PART_LENGTH)
    private String lastName;

    @OneToMany(mappedBy = "person")
    private final Set<PhoneNumber> phoneNumbers = new HashSet<>();

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person() {}

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @throws IllegalArgumentException
     * if <code>number</code> is already linked to other <code>Person</code>
     */
    public void addPhoneNumber(PhoneNumber number) {
        throwIfPhoneNumberIsLinkedToOtherPerson(number);

        phoneNumbers.add(number);
        number.setPerson(this);
    }

    /**
     * @throws IllegalArgumentException
     * if <code>number</code> is linked to other <code>Person</code>
     */
    public void removePhoneNumber(PhoneNumber number) {
        throwIfPhoneNumberIsLinkedToOtherPerson(number);
        phoneNumbers.remove(number);
    }

    private void throwIfPhoneNumberIsLinkedToOtherPerson(PhoneNumber p) {
        Person person = p.getPerson();

        if (person != null && person != this) {
            throw new IllegalArgumentException(
                "Given PhoneNumber object is linked to other Person"
            );
        }
    }

    /**
     * @return _unmodifiable_ view of all PhoneNumbers
     */
    public Set<PhoneNumber> getPhoneNumbers() {
        return Collections.unmodifiableSet(phoneNumbers);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}