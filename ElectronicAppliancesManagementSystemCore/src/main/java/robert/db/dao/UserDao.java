package robert.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.User;
import robert.db.repository.ReservationRepository;
import robert.db.repository.UserRepository;

@Component
@Transactional
public class UserDao {

    private final UserRepository userRepository;

	private final ReservationRepository reservationRepository;

    @Autowired
	public UserDao(UserRepository userRepository, ReservationRepository reservationRepository) {
		this.userRepository = userRepository;
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

	public void makeReservationForAppliance() {

	}
}
