package robert.web.svc.rest.responses.asm;

import robert.db.entity.Reservation;
import robert.web.svc.rest.responses.data.ReservationDR;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationAssembler {

	public static List<ReservationDR> convertToReservationDR(Collection<Reservation> reservations) {
		return reservations.stream()
				.map(reservation -> {
					ReservationDR reservationDR = new ReservationDR();
					reservationDR.setId(reservation.getId());
					reservationDR.setFrom(reservation.getValidFrom());
					reservationDR.setHours(
							reservation.getValidTill() - reservation.getValidFrom()
					);
					return reservationDR;
				}).collect(Collectors.toList());
	}
}
