package robert.web.svc.rest.responses.asm;

import robert.db.entity.Reservation;
import robert.web.svc.rest.responses.data.ReservationDR;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationAssembler {

	public static List<ReservationDR> convertToReservationDR(Collection<Reservation> reservations) {
		List<ReservationDR> list = reservations.stream()
				.map(reservation -> {
					ReservationDR reservationDR = new ReservationDR();
					reservationDR.setId(reservation.getId());
					reservationDR.setFrom(reservation.getValidFrom());
					reservationDR.setMinutes(
							convertToMinutes(reservation.getValidTill() - reservation.getValidFrom())
					);
					return reservationDR;
				}).collect(Collectors.toList());
		sortReservations(list);
		return list;
	}

	private static long convertToMinutes(long diff) {
		return (diff / 1000 / 60);
	}

	private static void sortReservations(List<ReservationDR> list) {
		list.sort((o1, o2) -> o1.getFrom() < o2.getFrom() ? -1 : 1);
	}
}
