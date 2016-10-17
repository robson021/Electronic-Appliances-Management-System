package utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class SpringWebMvcTest extends SpringTest {

	@Autowired
	private WebApplicationContext wac;

	protected MockMvc mockMvc;

	protected void initMockMvc() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

}
