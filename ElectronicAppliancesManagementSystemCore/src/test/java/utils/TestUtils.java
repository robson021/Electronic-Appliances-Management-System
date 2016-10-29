package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Room;
import robert.db.entity.User;

import java.util.UUID;

public class TestUtils {

	public static final String ADMIN_USER_EMAIL = "testusr@test.com";

	private static final ObjectMapper mapper = new ObjectMapper();

	public static User createAdminUser() throws Exception {
		User user = new User();
		user.setActivated(true);
		user.setAdminPrivileges(true);
		user.setPassword("admin.123Qwe");
		user.setAdminPrivileges(true);
		user.setEmail(ADMIN_USER_EMAIL);

		return user;
	}

	public static User generateExampleInactiveUser() throws Exception {
		String random = RandomStringUtils.random(10, RandomStringUtils.random(25, true, false));
		User inactiveUser = new User();
		inactiveUser.setEmail(random + ".test@ttt.pl");
		inactiveUser.setName("Inactive");
		inactiveUser.setSurname("User");
		inactiveUser.setPassword("abC.1234qwe");
		return inactiveUser;
	}

	public static User generateRandomActiveUser() throws Exception {
		User user = generateExampleInactiveUser();
		user.setActivated(true);
		user.setName("Active");
		return user;
	}

	public static String asJsonString(final Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Appliance generateRandomAppliance() {
		Appliance appliance = new Appliance();
		appliance.setName("test_appliance_" + UUID.randomUUID().toString());
		appliance.setUniqueCode(UUID.randomUUID().toString());
		return appliance;
	}

	public static Building generateRandomBuildingWithRooms(int numOfRooms) {
		Building building = generateRandomBuilding();
		for (int i = 0; i < numOfRooms; i++) {
			Room room = generateRandomRoom();
			building.addRoom(room);
			room.setBuilding(building);
		}
		return building;
	}

	public static Building generateRandomBuilding() {
		Building building = new Building();
		building.setName(getRandomNameWithNumber());
		return building;
	}

	public static Room generateRandomRoom() {
		Room room = new Room();
		room.setNumber(getRandomNameWithNumber());
		return room;
	}

	private static String getRandomNameWithNumber() {
		return RandomUtils.nextInt(1, 999) + RandomStringUtils.random(3, "qwertyuiop");
	}

}
