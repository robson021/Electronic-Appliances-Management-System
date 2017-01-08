package robert.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import robert.enums.Validation;
import robert.utils.TimeConverter;

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

	@Column(name = "VALID_FROM")
	private Long validFrom;

	@Column(name = "VALID_TILL")
	private Long validTill;

	@Column(name = "ACCESS_TOKEN", nullable = false)
	private String accessToken = Validation.MOCK_APPLIANCE_UNIQUE_CODE;

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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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

    public int getDurationOfAccess() {
		return (int) TimeConverter.convertMillisToMinutes(validTill - validFrom);
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
