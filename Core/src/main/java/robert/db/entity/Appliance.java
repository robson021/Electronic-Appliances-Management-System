package robert.db.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APPLIANCE")
public class Appliance {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROOM_ID")
	private Room room;

	// FIXME: unique causes test errors
	@Column(name = "UNIQUE_CODE", nullable = false/*, unique = true*/)
	private String uniqueCode;

	@OneToMany(mappedBy = "appliance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Reservation> reservations = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getName() {
		return name;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public void addReservation(Reservation reservation) {
		if (this.reservations == null) {
			this.reservations = new HashSet<>(1);
		}
		this.reservations.add(reservation);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		Appliance a = (Appliance) obj;
		return this.id.equals(a.id) || new EqualsBuilder()
				.append(this.room, a.room)
				.append(this.name, a.name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.id)
				.append(this.room)
				.append(this.name)
				.toHashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}
}
