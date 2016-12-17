package robert.web.svc.rest.responses.json;

import java.util.List;

public class ApplianceDTO {

	private long id;

	private String name;

	private List<ReservationDTO> reservations;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReservationDTO> getReservations() {
		return reservations;
	}

	public void setReservations(List<ReservationDTO> reservations) {
		this.reservations = reservations;
	}
}
