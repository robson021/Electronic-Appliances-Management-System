package robert.web.svc.rest;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import robert.db.dao.UserDao;
import robert.db.entity.User;
import robert.web.svc.rest.ctrl.api.AdminPanelCtrl;
import utils.SpringWebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminPanelControllerTest extends SpringWebMvcTest {

	@Autowired
	private UserDao userDao;

	private final String email1 = "inactive@user.pl";

	private final String email2 = "inactive@user2.pl";

	@Override
	@Before
	public void setup() throws Exception {
		super.initMockMvc();
	}

	@Test
	public void getAllInactiveAccounts() throws Exception {
		saveExampleUsers();

		MvcResult result = mockMvc.perform(get(AdminPanelCtrl.GET_ALL_INACTIVE_ACCOUNTS_URL))
				.andExpect(status().isOk())
				.andReturn();

		Assertions.assertThat(result.getResponse().getContentType())
				.isEqualToIgnoringCase(CONTENT_TYPE);

		String contentAsString = result.getResponse().getContentAsString();
		Assertions.assertThat(contentAsString)
				.containsIgnoringCase(email1)
				.containsIgnoringCase(email2);
	}

	private void saveExampleUsers() throws Exception {
		User inactiveUser = new User();
		inactiveUser.setEmail(email1);
		inactiveUser.setName("Inactive");
		inactiveUser.setSurname("User");
		inactiveUser.setPassword("abC.1234qwe");

		User inactiveUser2 = new User();
		inactiveUser2.setEmail(email2);
		inactiveUser2.setName("Inactive");
		inactiveUser2.setSurname("User2");
		inactiveUser2.setPassword("abC.1234qwe");

		userDao.saveUser(inactiveUser);
		userDao.saveUser(inactiveUser2);
	}

}