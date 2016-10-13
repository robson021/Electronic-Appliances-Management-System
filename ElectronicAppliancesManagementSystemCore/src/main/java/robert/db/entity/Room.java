package robert.db.entity;

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
}
