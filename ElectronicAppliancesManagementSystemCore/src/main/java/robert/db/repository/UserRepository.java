package robert.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import robert.db.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByEmail(String email);

    User findOneById(Long id);

    List<User> findByActivated(Boolean activated);

}
