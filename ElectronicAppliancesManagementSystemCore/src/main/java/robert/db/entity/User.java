package robert.db.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.AccessType;
import robert.enums.Validation;
import robert.exceptions.InvalidEmailPatternException;
import robert.exceptions.InvalidPasswordException;

import javax.persistence.*;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws InvalidEmailPatternException {
		email = email.trim();
		if (!validateEmail(email)) {
			throw new InvalidEmailPatternException(email);
		}
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws InvalidPasswordException {
		password = password.trim();
		if (!validatePassword(password)) {
			throw new InvalidPasswordException();
		}
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

	private boolean validateEmail(String emailStr) {
		return Validation.VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr).find();
	}

	private boolean validatePassword(String password) {
		return Validation.VALID_PASSWORD_REGEX.matcher(password).find();
	}
}
