package robert.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import robert.db.entity.User;
import robert.db.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Component
@Transactional
public class AdminDao {

	private final UserRepository userRepository;

	@Autowired
	public AdminDao(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllInactiveUsers() {
		List<User> users = this.userRepository.findByActivated(false);
		return CollectionUtils.isEmpty(users) ? Collections.emptyList() : users;
	}

	public List<User> getAllActiveUsers() {
		List<User> users = this.userRepository.findByActivated(true);
		return CollectionUtils.isEmpty(users) ? Collections.emptyList() : users;
	}
}
