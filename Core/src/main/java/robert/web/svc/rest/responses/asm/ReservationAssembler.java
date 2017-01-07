package robert.web.svc.rest.responses.asm;

import robert.db.entity.Reservation;
import robert.utils.TimeConverter;
import robert.web.svc.rest.responses.dto.ReservationDTO;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationAssembler {

	private static final Comparator<ReservationDTO> comparatorOfReservationTime = //
			(o1, o2) -> o1.getFrom() < o2.getFrom() ? -1 : 1;

	public static List<ReservationDTO> convertToReservationDTO(Collection<Reservation> reservations) {
		long currentTime = new Date().getTime();
		List<ReservationDTO> listOfReservations = reservations.stream()
				.filter(reservation -> reservation.getValidTill() > currentTime)
				.map(reservation -> {
					ReservationDTO reservationDTO = new ReservationDTO();
					reservationDTO.setId(reservation.getId());
					reservationDTO.setFrom(reservation.getValidFrom());
					long timeDiff = reservation.getValidTill() - reservation.getValidFrom();
					reservationDTO.setMinutes(TimeConverter.convertMillisToMinutes(timeDiff));
					String name = reservation.getUser()
							.getName();
					String surname = reservation.getUser()
							.getSurname();
					reservationDTO.setOwner(name + " " + surname);
					reservationDTO.setWhere(getWhereIsTheReservation(reservation));
					reservationDTO.setAppliance(reservation.getAppliance()
							.getName());
					return reservationDTO;
				})
				.collect(Collectors.toList());
		listOfReservations.sort(comparatorOfReservationTime);
		return listOfReservations;
	}

	private static String getWhereIsTheReservation(Reservation reservation) {
		try {
			String where = reservation.getAppliance()
					.getRoom()
					.getBuilding()
					.getName();
			where += " - ";
			where += reservation.getAppliance()
					.getRoom()
					.getNumber();
			return where;
		} catch (Throwable ignored) {
			return "???";
		}
	}
}
