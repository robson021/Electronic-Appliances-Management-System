package robert.web.svc.rest.responses.data;

import java.util.List;

public class ApplianceDR {

	private long id;

	private String name;

	private List<ReservationDR> reservations;

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

	public List<ReservationDR> getReservations() {
		return reservations;
	}

	public void setReservations(List<ReservationDR> reservations) {
		this.reservations = reservations;
	}
}
