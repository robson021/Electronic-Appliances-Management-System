package robert.db.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.AccessType;
import robert.enums.RegularExpressions;
import robert.exceptions.InvalidEmailPatternException;

import javax.persistence.*;
import java.util.regex.Matcher;

@Entity
@Table(name = "USER")
@AccessType(AccessType.Type.PROPERTY)
public class User {

    @Id
    @GeneratedValue
	@Column(name = "ID")
	private Long id;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "ADMIN_PRIVILEGES")
    private Boolean adminPrivileges = false;

    private boolean validateEmail(String emailStr) {
        Matcher matcher = RegularExpressions.VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
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

    public Boolean getAdminPrivileges() {
        return adminPrivileges;
    }

    public void setAdminPrivileges(Boolean adminPrivileges) {
        this.adminPrivileges = adminPrivileges;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        User u = (User) obj;
        return new EqualsBuilder()
                .append(id, u.id)
                .append(email, u.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(email)
                .append(name)
                .append(surname)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
