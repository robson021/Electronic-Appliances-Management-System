package robert.db.dao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import robert.db.entity.*;
import robert.db.repository.*;
import robert.enums.Validation;
import robert.exceptions.ApplianceException;
import robert.utils.api.AppLogger;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class UserDao {

	private final AppLogger log;

	private final UserRepository userRepository;

	private final ApplianceRepository applianceRepository;

	private final BuildingRepository buildingRepository;

	private final RoomRepository roomRepository;

	private final ReservationRepository reservationRepository;

	@Autowired
	public UserDao(AppLogger log, UserRepository userRepository, ApplianceRepository applianceRepository, BuildingRepository buildingRepository,
				   RoomRepository roomRepository, ReservationRepository reservationRepository) {
		this.log = log;
		this.userRepository = userRepository;
		this.applianceRepository = applianceRepository;
		this.buildingRepository = buildingRepository;
		this.roomRepository = roomRepository;
		this.reservationRepository = reservationRepository;
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	public void deleteUser(Long id) {
		userRepository.delete(id);
	}

	public User findUserByEmail(String email) {
		return userRepository.findOneByEmail(email.trim());
	}

	public User findUserById(long id) {
		return userRepository.findOneById(id);
	}

	public Iterable<User> findAllUsers() {
		return userRepository.findAll();
	}

	public void makeReservationForAppliance(String email, long applianceId, Date from, int minutes) throws ApplianceException {

		if (minutes <= 0 || minutes > Validation.MAX_RESERVATION_TIME_IN_MINUTES) {
			throw new ApplianceException("Invalid reservation time: " + minutes);
		}

		User user = userRepository.findOneByEmail(email);
		Appliance appliance = applianceRepository.findOne(applianceId);

		Reservation reservation = new Reservation();
		reservation.setAppliance(appliance);
		reservation.setUser(user);
		Date currentDate = new Date();
		if (from.before(currentDate)) {
			from = new DateTime(currentDate).plusMinutes(15)
					.toDate();
		}
		reservation.setValidFrom(from.getTime());
		reservation.setValidTill(new DateTime(from)
				.plusMinutes(minutes)
				.toDate()
				.getTime());

		validReservationTime(appliance, reservation);

		//appliance.addReservation(reservation); // fixme: duplicates reservation in Set
		user.addReservation(reservation);
		reservation.setAppliance(appliance);
		reservation.setUser(user);
		userRepository.save(user);
		log.info("New reservation made for:", user.getEmail());
	}

	public Set<Reservation> getUsersReservations(String email) {
		User user = userRepository.findOneByEmail(email);
		if (user == null || user.getReservations().isEmpty()) {
			return Collections.emptySet();
		}
		return user.getReservations();
	}

	public Set<Reservation> getAllReservationsForAppliance(long applianceId) {
		Appliance appliance = applianceRepository.findOne(applianceId);
		if (appliance == null || CollectionUtils.isEmpty(appliance.getReservations())) {
			return Collections.emptySet();
		}
		return appliance.getReservations();
	}

	public void deleteBuilding(String buildingNumber) {
		this.buildingRepository.deleteOneByName(buildingNumber.trim()
				.toLowerCase());
	}

	public void renameBuilding(String buildingNumber, String newValue) {
		Building building = this.buildingRepository.findOneByName(buildingNumber.trim()
				.toLowerCase());
		building.setName(newValue);
		buildingRepository.save(building);
	}

	public void deleteRoom(long roomId) {
		Room room = roomRepository.findOne(roomId);
		try {
			Building building = room.getBuilding();
			building.getRooms()
					.remove(room);
			buildingRepository.save(building);
		} catch (Throwable ignored) {
		} finally {
			roomRepository.delete(room);
		}
	}

	public void renameRoom(long roomId, String newNumber) {
		Room room = roomRepository.findOne(roomId);
		room.setNumber(newNumber);
		roomRepository.save(room);
	}

	public void deleteAppliance(long applianceId) {
		Appliance appliance = applianceRepository.findOne(applianceId);
		try {
			Room room = appliance.getRoom();
			room.getAppliances()
					.remove(appliance);
			appliance.setRoom(null);
			roomRepository.save(room);
		} catch (Throwable ignored) {
		} finally {
			applianceRepository.delete(applianceId);
		}
	}

	public void renameAppliance(long applianceId, String newName) {
		Appliance appliance = applianceRepository.findOne(applianceId);
		appliance.setName(newName);
		applianceRepository.save(appliance);
	}

	private void validReservationTime(Appliance appliance, Reservation reservation) throws ApplianceException {
		Optional<Reservation> any = appliance.getReservations()
				.stream()
				.filter(r -> ((reservation.getValidFrom() < r.getValidTill() && reservation.getValidTill() > r.getValidTill()) || (
						reservation.getValidFrom() < r.getValidFrom() && reservation.getValidTill() > r.getValidFrom())))
				.findFirst();
		if (any.isPresent()) {
			throw new ApplianceException("Cannot make reservation between these hours.");
		}
	}
}
