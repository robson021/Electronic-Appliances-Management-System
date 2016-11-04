package robert.db.dao;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import robert.db.entity.Appliance;
import robert.db.entity.Reservation;
import robert.db.entity.User;
import utils.SpringTest;
import utils.TestUtils;

import java.util.Date;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDaoTest extends SpringTest {

    private static final String EMAIL = "test@user.pl";

    private User user;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApplianceBuildingRoomManagementDao abrmDao;

    @Override
    public void setup() throws Exception {
    }

    @Before
    public void saveUser() throws Exception {
        user = createUser();
        userDao.saveUser(user);
    }

    @Test
    public void deleteUser() throws Exception {
        userDao.deleteUser(user);
        Assertions.assertThat(userDao.findUserByEmail(EMAIL))
                .isNull();
    }

    @Test
    public void deleteUserById() throws Exception {
        userDao.deleteUser(user.getId());
        Assertions.assertThat(userDao.findUserById(user.getId()))
                .isNull();
    }

    @Test
    public void findUserByEmail() throws Exception {
        User user = userDao.findUserByEmail(EMAIL);
        Assertions.assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", EMAIL);
    }

    @Test
    public void findUserById() throws Exception {
        User user = userDao.findUserById(this.user.getId());
        Assertions.assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", EMAIL);
    }

    @Test
    public void addReservationToUser() throws Exception {
        User user = TestUtils.generateRandomActiveUser();
        user = userDao.saveUser(user);

        Appliance appliance = TestUtils.generateRandomAppliance();
        appliance = abrmDao.saveAppliance(appliance);

        userDao.makeReservationForAppliance(user.getEmail(), appliance.getId(), new Date(), 2);

        user = userDao.findUserByEmail(user.getEmail());
        Assertions.assertThat(user)
                .isNotNull();

        Assertions.assertThat(user.getReservations())
                .isNotNull();

        Reservation reservation = user.getReservations()
                .iterator()
                .next();
        Assertions.assertThat(abrmDao.findAllReservations())
                .isNotNull()
                .contains(reservation);

        Assertions.assertThat(reservation.getValidTill())
                .isGreaterThan(reservation.getValidFrom());
    }

    private User createUser() throws Exception {
        User user = new User();
        user.setEmail(EMAIL);
        user.setName("Test");
        user.setSurname("User");
        user.setPassword("passwdpasswD.123");

        return user;
    }

}