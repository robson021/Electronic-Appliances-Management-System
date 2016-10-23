package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import robert.db.entity.Appliance;
import robert.db.entity.User;

import java.util.UUID;

public class TestUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

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
