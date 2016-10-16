package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.web.user.UserInfoProvider;

import javax.servlet.http.HttpServletResponse;

@Component
public class BasicAuthFilterImpl extends BasicAuthFilter {

	private final UserInfoProvider userInfoProvider;

	@Autowired
	public BasicAuthFilterImpl(UserInfoProvider userInfoProvider) {
		this.userInfoProvider = userInfoProvider;
	}


	@Override
	void doLogic(HttpServletResponse response) {
		System.out.println("Request from: " + userInfoProvider.getEmail());
		// todo
	}
}
