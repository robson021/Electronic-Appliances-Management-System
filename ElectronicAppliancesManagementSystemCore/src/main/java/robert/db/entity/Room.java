package robert.db.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROOM")
public class Room {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "BUILDING")
	private String building;

	@Column(name = "NUMBER")
	private String number;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROOM_ID")
	private Set<Appliance> appliances = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Set<Appliance> getAppliances() {
		return appliances;
	}

	public void setAppliances(Set<Appliance> appliances) {
		this.appliances = appliances;
	}

	public void addNewAppliance(Appliance appliance) {
		if (this.appliances == null) {
			this.appliances = new HashSet<>();
		}
		this.appliances.add(appliance);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		Room r = (Room) obj;
		if (this.id.equals(r.id)) {
			return true;
		}

		return new EqualsBuilder()
				.append(this.number, r.number)
				.append(this.building, r.building)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(building)
				.append(number)
				.toHashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
