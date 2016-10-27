package utils;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import robert.db.entity.Appliance;
import robert.db.entity.User;

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

	public static Appliance geenrateRandomAppliance() {
		Appliance appliance = new Appliance();
		appliance.setName("test_appliance_" + UUID.randomUUID().toString());
		appliance.setUniqueCode(UUID.randomUUID().toString());
		return appliance;
	}

}
