package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.web.user.UserInfoProvider;

import javax.servlet.http.HttpServletResponse;

@Component
public class BasicAuthFilterImpl extends BasicAuthFilter {

	@Autowired
	public BasicAuthFilterImpl(UserInfoProvider uip) {
		super(uip);
	}

	@Override
	void doLogic(HttpServletResponse response) {
		// todo
	}
}
