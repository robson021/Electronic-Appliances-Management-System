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
import utils.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminPanelControllerTest extends SpringWebMvcTest {

	@Autowired
	private UserDao userDao;

	private String email1;

	private String email2;

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
		User user = TestUtils.generateExampleInactiveUser();
		User user2 = TestUtils.generateExampleInactiveUser();
		email1 = user.getEmail();
		email2 = user2.getEmail();
		userDao.saveUser(user);
		userDao.saveUser(user2);
	}

}