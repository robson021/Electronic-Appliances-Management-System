package robert.db.entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "APPLIANCE_ID")
	private Appliance appliance;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	// TODO: make them save as java.util.Date
	@Column(name = "VALID_FROM")
	private Long validFrom;

	@Column(name = "VALID_TILL")
	private Long validTill;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Appliance getAppliance() {
		return appliance;
	}

	public void setAppliance(Appliance appliance) {
		this.appliance = appliance;
	}

	public Long getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Long validFrom) {
		this.validFrom = validFrom;
	}

	public Long getValidTill() {
		return validTill;
	}

	public void setValidTill(Long validTill) {
		this.validTill = validTill;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Reservation r = (Reservation) o;
		return this.id.equals(r.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.id)
				.append(appliance)
				.toHashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
