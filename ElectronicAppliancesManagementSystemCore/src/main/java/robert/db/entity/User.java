package robert.db.entity;

import org.springframework.data.annotation.AccessType;
import robert.exception.InvalidEmailPatternException;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "USER")
@AccessType(AccessType.Type.PROPERTY)
public class User {

    @Transient
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = //
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    private boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidEmailPatternException {
        if (!validateEmail(email)) {
            throw new InvalidEmailPatternException(email);
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
