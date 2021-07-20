package model;
/**
 * @author José Márquez
 *Check @josemarq github repositories for complete code
 */

import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    //Email validator Regex modified from lesson for validate correctly domain extension.
    private final String emailRegex = "^(.+)@(.+)\\.(.+)$";
    private final Pattern pattern = Pattern.compile(emailRegex);

    public Customer(String firstName, String lastName, String email) {
        this.emailValidation(email, pattern);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private void emailValidation(final String email, Pattern pattern) {
        if(!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return "\nClient Information:\n-------------------\nName: " + firstName + " " + lastName + "\nemail: " + email;
    }

}
