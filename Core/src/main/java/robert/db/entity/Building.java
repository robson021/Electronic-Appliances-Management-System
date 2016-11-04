package robert.db.entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BUILDING")
public class Building {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME", unique = true)
	private String name;

	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Room> rooms = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim().toLowerCase();
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public void addRoom(Room room) {
		if (this.rooms == null) {
			rooms = new HashSet<>();
		} else if (rooms.contains(room)) {
			return;
		}
		this.rooms.add(room);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Building building = (Building) o;
		return building.getName().equals(this.name);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.id)
				.append(this.name)
				.toHashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
