package robert.db.dao;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import robert.SpringTest;
import robert.db.entity.User;

public class UserDaoTest extends SpringTest {

    private static final String email = "test@user.pl";

    private User user;

    @Autowired
    private UserDao userDao;

    @Before
    public void saveUser() throws Exception {
        user = createUser();
        userDao.saveUser(user);
    }

    @Test
    @DirtiesContext
    public void deleteUser() throws Exception {
        userDao.deleteUser(user);
        Assertions.assertThat(userDao.findUserByEmail(email))
                .isNull();
    }

    @Test
    @DirtiesContext
    public void deleteUserById() throws Exception {
        userDao.deleteUser(user.getId());
        Assertions.assertThat(userDao.findUserById(user.getId()))
                .isNull();
    }

    @Test
    @DirtiesContext
    public void findUserByEmail() throws Exception {
        User user = userDao.findUserByEmail(email);
        Assertions.assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", email);
    }

    @Test
    @DirtiesContext
    public void findUserById() throws Exception {
        User user = userDao.findUserById(this.user.getId());
        Assertions.assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", email);
    }

    private User createUser() throws Exception {
        User user = new User();
        user.setEmail(email);
        user.setName("Test");
        user.setSurname("User");
        user.setPassword("passwdpasswd");

        return user;
    }

}