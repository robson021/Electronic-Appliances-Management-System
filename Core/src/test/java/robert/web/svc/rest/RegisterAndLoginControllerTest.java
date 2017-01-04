package robert.web.svc.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import robert.db.dao.UserDao;
import robert.db.entity.User;
import robert.web.svc.rest.responses.dto.UserCredentialsDTO;
import utils.SpringWebMvcTest;
import utils.TestUtils;

public class RegisterAndLoginControllerTest extends SpringWebMvcTest {

    private static final String EMAIL = "test@usr.pl";

    private static final String PASSWORD = "passwD.123qwe";

    private static final String REGISTER_URL = "/register/";

    private static final String LOGIN_URL = "/login/";

    @Autowired
    private UserDao userDao;

    @Override
    @Before
    public void setup() throws Exception {
        super.initMockMvc();
    }

    @Test
    public void registerNewUser() throws Exception {
        UserCredentialsDTO user = new UserCredentialsDTO();
        user.setName("Random");
        user.setSurname("Dude");
        user.setPassword("Passwd1234.sw");
        user.setEmail("test.dude@t.com");
        mockMvc.perform(put(REGISTER_URL).content(TestUtils.asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void loginUser() throws Exception {
        User user = saveTestUser();
        UserCredentialsDTO u = new UserCredentialsDTO();
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        mockMvc.perform(post(LOGIN_URL).content(TestUtils.asJsonString(u))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private User saveTestUser() throws Exception {
        User user = new User();
        user.setId(9999L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setName("Test");
        user.setSurname("User");
        return userDao.saveUser(user);
    }

}