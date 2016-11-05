package robert.db.dao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.*;
import robert.db.repository.ApplianceRepository;
import robert.db.repository.BuildingRepository;
import robert.db.repository.RoomRepository;
import robert.db.repository.UserRepository;
import robert.enums.Validation;
import robert.exceptions.ApplianceException;
import robert.utils.api.AppLogger;

import java.util.Date;
import java.util.Optional;

@Component
@Transactional
public class UserDao {

	private final AppLogger log;

	private final UserRepository userRepository;

	private final ApplianceRepository applianceRepository;

	private final BuildingRepository buildingRepository;

	private final RoomRepository roomRepository;

	@Autowired
	public UserDao(AppLogger log, UserRepository userRepository, ApplianceRepository applianceRepository, BuildingRepository buildingRepository, RoomRepository roomRepository) {
		this.log = log;
		this.userRepository = userRepository;
		this.applianceRepository = applianceRepository;
		this.buildingRepository = buildingRepository;
		this.roomRepository = roomRepository;
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

	public void makeReservationForAppliance(String email, long applianceId, Date from, int hours) throws ApplianceException {

		if (hours <= 0 || hours > Validation.MAX_RESERVATION_TIME_IN_HOURS) {
			throw new ApplianceException("Invalid reservation time: " + hours);
		}

		User user = userRepository.findOneByEmail(email);
		Appliance appliance = applianceRepository.findOne(applianceId);

		Reservation reservation = new Reservation();
		reservation.setAppliance(appliance);
		reservation.setUser(user);
		Date currentDate = new Date();
		if (from.before(currentDate)) {
			from = new DateTime(currentDate)
					.plusHours(1)
					.toDate();
		}
		reservation.setValidFrom(from.getTime());
		reservation.setValidTill(new DateTime(from)
				.plusHours(hours)
				.toDate().getTime());

		validReservationTime(appliance, reservation);

		//appliance.addReservation(reservation); // fixme: duplicates reservation in Set
		user.addReservation(reservation);
		reservation.setAppliance(appliance);
		reservation.setUser(user);
		userRepository.save(user);
		log.info("New reservation made for:", user.getEmail());
	}

	public void deleteBuilding(String buildingNumber) {
		this.buildingRepository.deleteOneByName(buildingNumber.trim().toLowerCase());
	}

	public void renameBuilding(String buildingNumber, String newValue) {
		Building building = this.buildingRepository.findOneByName(
				buildingNumber.trim().toLowerCase());
		building.setName(newValue);
		buildingRepository.save(building);
	}

	public void deleteRoom(long roomId) {
		roomRepository.delete(roomId);
	}

	public void renameRoom(long roomId, String newNumber) {
		Room room = roomRepository.findOne(roomId);
		room.setNumber(newNumber);
		roomRepository.save(room);
	}

	public void deleteAppliance(long applianceId) {
		applianceRepository.delete(applianceId);
	}

	public void renameAppliance(long applianceId, String newName) {
		Appliance appliance = applianceRepository.findOne(applianceId);
		appliance.setName(newName);
		applianceRepository.save(appliance);
	}

	private void validReservationTime(Appliance appliance, Reservation reservation) throws ApplianceException {
		Optional<Reservation> any = appliance.getReservations().stream()
				.filter(r -> (
						(reservation.getValidFrom() < r.getValidTill() &&
								reservation.getValidTill() > r.getValidTill())
								||
								(reservation.getValidFrom() < r.getValidFrom()
										&& reservation.getValidTill() > r.getValidFrom())
				))
				.findFirst();
		if (any.isPresent()) {
			throw new ApplianceException("Cannot make reservation between this hours.");
		}
	}
}
