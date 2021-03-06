package robert.db.dao;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Reservation;
import robert.db.entity.User;
import robert.db.repository.ReservationRepository;
import robert.enums.Validation;
import utils.SpringTest;
import utils.TestUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDaoTest extends SpringTest {

	private static final String EMAIL = "test@user.pl";

	private User user;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ApplianceBuildingRoomManagementDao abrmDao;

	@Autowired
	private ReservationRepository reservationRepository;

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
	public void addAndCancelReservation() throws Exception {
		User user = TestUtils.generateRandomActiveUser();
		user = userDao.saveUser(user);

		Appliance appliance = TestUtils.generateRandomAppliance();
		appliance = abrmDao.saveAppliance(appliance);

		final String email = user.getEmail();
		userDao.makeReservationForAppliance(email, appliance.getId(), new Date(), 2);

		user = userDao.findUserByEmail(email);
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

		Set<Reservation> usersReservations = userDao.getUsersReservations(email);
		Assertions.assertThat(usersReservations)
				.isNotEmpty()
				.hasSize(1);

		Collection<Reservation> allReservations = userDao.getAllReservationsForAppliance(appliance.getId());
		Assertions.assertThat(allReservations)
				.isNotEmpty();

		userDao.cancelReservation(email, usersReservations.iterator().next().getId());
		Assertions.assertThat(userDao.getUsersReservations(email))
				.isEmpty();
	}

	@Test
	public void changeApplianceAddress() throws Throwable {
		Appliance appliance = abrmDao.saveAppliance(TestUtils.generateRandomAppliance());

		final String newAddress = "TEST_ADDRESS.com:8080";
		userDao.setNewAddressForTheAppliance(appliance.getId(), newAddress);

		Assertions.assertThat(abrmDao.findApplianceById(appliance.getId()))
				.isNotNull()
				.hasFieldOrPropertyWithValue("address", newAddress);
	}

	@Test
	public void deleteBuilding() throws Exception {
		final int numOfRooms = 5;
		final String buildingName = abrmDao.saveBuilding(TestUtils.generateRandomBuildingWithRooms(numOfRooms))
				.getName();

		final int numBuildings = abrmDao.findAllBuildingNumbers()
				.size();

		Building building = abrmDao.findBuildingByName(buildingName);
		Assertions.assertThat(building.getRooms())
				.hasSize(numOfRooms);

		userDao.deleteBuilding(buildingName);
		building = abrmDao.findBuildingByName(buildingName);

		Assertions.assertThat(building)
				.isNull();

		final int newSize = abrmDao.findAllBuildingNumbers().size();
		Assertions.assertThat(numBuildings)
				.isEqualTo(newSize + 1);

	}

	@Test
	public void renameBuilding() throws Exception {
		String buildingName = abrmDao.saveBuilding(TestUtils.generateRandomBuildingWithRooms(3))
				.getName();

		final String newName = "DHKdasdEQ31";
		userDao.renameBuilding(buildingName.toUpperCase(), newName);

		Assertions.assertThat(abrmDao.findBuildingByName(newName))
				.hasFieldOrPropertyWithValue("name", newName.toLowerCase());
	}

	@Test
	public void deleteRoom() throws Exception {
		final long roomId = abrmDao.saveRoom(TestUtils.generateRandomRoom())
				.getId();

		userDao.deleteRoom(roomId);
		Assertions.assertThat(abrmDao.findRoomById(roomId))
				.isNull();
	}

	@Test
	public void renameRoom() throws Exception {
		final long roomId = abrmDao.saveRoom(TestUtils.generateRandomRoom())
				.getId();

		final String newVal = "731HDKAdasd";
		userDao.renameRoom(roomId, newVal);

		Assertions.assertThat(abrmDao.findRoomById(roomId))
				.hasFieldOrPropertyWithValue("number", newVal);
	}

	@Test
	public void deleteAppliance() throws Exception {
		final long id = abrmDao.saveAppliance(TestUtils.generateRandomAppliance())
				.getId();
		userDao.deleteAppliance(id);

		Assertions.assertThat(abrmDao.findApplianceById(id))
				.isNull();
	}

	@Test
	public void renameAppliance() throws Exception {
		final long id = abrmDao.saveAppliance(TestUtils.generateRandomAppliance())
				.getId();

		final String newName = "Hdhadeqiwuehq31";
		userDao.renameAppliance(id, newName);

		Assertions.assertThat(abrmDao.findApplianceById(id))
				.hasFieldOrPropertyWithValue("name", newName);
	}

	@Test
	public void cleanReservations() throws Exception {
		User user = TestUtils.generateRandomActiveUser();
		user = userDao.saveUser(user);

		Appliance appliance = TestUtils.generateRandomAppliance();
		appliance = abrmDao.saveAppliance(appliance);

		final String email = user.getEmail();
		userDao.makeReservationForAppliance(email, appliance.getId(), new Date(), 2);

		Reservation reservation = abrmDao.findAllReservations().iterator().next();
		long newTime = new DateTime()
				.minusDays(Validation.TIME_OF_KEEPING_OLD_RESERVATION_IN_DAYS + 1)
				.toDate()
				.getTime();
		reservation.setValidTill(newTime);
		reservationRepository.save(reservation);

		int cleaned = abrmDao.cleanOldReservations();
		Assertions.assertThat(cleaned)
				.isGreaterThan(0);
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