package utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

public abstract class SpringWebMvcTest extends SpringTest {

	protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext wac;

	protected MockMvc mockMvc = null;

	protected void initMockMvc() throws Exception {
		if (mockMvc == null) {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		}
	}

}
