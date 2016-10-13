package robert.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.User;
import robert.db.repository.UserRepository;

@Component
@Transactional
public class UserDao {

    private final UserRepository userRepository;

    @Autowired
    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.findOneByEmail(email);
    }

    public User findUserById(long id) {
        return userRepository.findOneById(id);
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }
}
