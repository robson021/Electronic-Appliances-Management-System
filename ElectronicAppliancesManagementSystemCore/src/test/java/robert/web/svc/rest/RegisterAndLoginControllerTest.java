package robert.web.svc.rest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import robert.db.dao.UserDao;
import robert.db.entity.User;
import utils.SpringWebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegisterAndLoginControllerTest extends SpringWebMvcTest {

	private static final String EMAIL = "test@usr.pl";

	private static final String PASSWORD = "passwD.123qwe";

	private static final String REGISTER_URL = "/register/a" + EMAIL + "a/" + PASSWORD + "/Jane/Doe/";

	private static final String LOGIN_URL = "/login/" + EMAIL + "/" + PASSWORD + "/";

	@Autowired
	private UserDao userDao;

	@Override
	@Before
	public void setup() throws Exception {
		super.initMockMvc();
	}

	@Test
	public void registerNewUser() throws Exception {
		mockMvc.perform(put(REGISTER_URL))
				.andExpect(status().isOk());
	}

	@Test
	public void loginUser() throws Exception {
		this.saveTestUser();
		mockMvc.perform(post(LOGIN_URL))
				.andExpect(status().isOk());
	}

	private void saveTestUser() throws Exception {
		User user = new User();
		user.setId(9999L);
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		user.setName("Test");
		user.setSurname("User");
		userDao.saveUser(user);
	}

}