package robert.db.dao;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import robert.db.entity.User;
import utils.SpringTest;
import utils.TestUtils;

public class AdminDaoTest extends SpringTest {

	@Autowired
	private UserDao userDao;

	@Autowired
	private AdminDao adminDao;

	@Override
	public void setup() throws Exception {
	}

	@Test
	public void getAllInactiveUsers() throws Exception {
		int initSize = adminDao.getAllInactiveUsers().size();
		userDao.saveUser(TestUtils.generateExampleInactiveUser());

		Assertions.assertThat(adminDao.getAllInactiveUsers().size())
				.isEqualTo(initSize + 1);
	}

	@Test
	public void getAllActivatedUsers() throws Exception {
		int initSize = adminDao.getAllActiveUsers().size();
		userDao.saveUser(TestUtils.generateRandomActiveUser());

		Assertions.assertThat(adminDao.getAllActiveUsers().size())
				.isEqualTo(initSize + 1);
	}

	@Test
	public void activateUserAccount() throws Exception {
		final String email = userDao.saveUser(TestUtils.generateExampleInactiveUser()).getEmail();
		adminDao.activateUserAccount(email);
		User user = userDao.findUserByEmail(email);

		Assertions.assertThat(user)
				.isNotNull()
				.hasFieldOrPropertyWithValue("activated", true);

	}
}