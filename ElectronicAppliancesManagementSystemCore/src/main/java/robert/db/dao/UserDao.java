package robert.db.dao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.Appliance;
import robert.db.entity.Reservation;
import robert.db.entity.User;
import robert.db.repository.ApplianceRepository;
import robert.db.repository.ReservationRepository;
import robert.db.repository.UserRepository;
import robert.enums.Validation;
import robert.exceptions.ApplianceException;

import java.util.Date;

@Component
@Transactional
public class UserDao {

	private final UserRepository userRepository;

	private final ReservationRepository reservationRepository;

	private final ApplianceRepository applianceRepository;

	@Autowired
	public UserDao(UserRepository userRepository, ReservationRepository reservationRepository, ApplianceRepository applianceRepository) {
		this.userRepository = userRepository;
		this.reservationRepository = reservationRepository;
		this.applianceRepository = applianceRepository;
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
					.plusMinutes(15)
					.toDate();
		}
		reservation.setValidFrom(from);
		reservation.setValidTill(new DateTime(from)
				.plus(hours)
				.toDate());

		appliance.setReservation(reservation);

		user.addReservation(reservation);
		userRepository.save(user);
	}
}
