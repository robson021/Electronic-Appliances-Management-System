package robert.web.svc.rest.responses.asm;

import robert.db.entity.Reservation;
import robert.web.svc.rest.responses.data.ReservationDR;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationAssembler {

	private static final String ERROR_APPLIANCE_LOCATION = "???";

	public static List<ReservationDR> convertToReservationDR(Collection<Reservation> reservations) {
		List<ReservationDR> listOfReservations = reservations.stream()
				.map(reservation -> {
					ReservationDR reservationDR = new ReservationDR();
					reservationDR.setId(reservation.getId());
					reservationDR.setFrom(reservation.getValidFrom());
					reservationDR.setMinutes(
							convertToMinutes(reservation.getValidTill() - reservation.getValidFrom())
					);
					reservationDR.setWhere(getWhereIsTheReservation(reservation));
					reservationDR.setAppliance(reservation.getAppliance().getName());
					return reservationDR;
				}).collect(Collectors.toList());
		sortReservations(listOfReservations);
		return listOfReservations;
	}

	private static long convertToMinutes(long diff) {
		return (diff / 1000 / 60);
	}

	private static String getWhereIsTheReservation(Reservation reservation) {
		try {
			String where = reservation.getAppliance().getRoom().getBuilding().getName();
			where += " - ";
			where += reservation.getAppliance().getRoom().getNumber();
			return where;
		} catch (Throwable ignored) {
			return ERROR_APPLIANCE_LOCATION;
		}
	}

	private static void sortReservations(List<ReservationDR> list) {
		list.sort((o1, o2) -> o1.getFrom() < o2.getFrom() ? -1 : 1);
	}
}
