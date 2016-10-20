package robert.web.svc.rest.responses.asm;

import robert.db.entity.User;
import robert.web.svc.rest.responses.data.UserDR;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {

	public static List<UserDR> convertToUserDR(List<User> users) {
		return users.stream()
				.map(user -> {
					UserDR u = new UserDR();
					u.setName(user.getName());
					u.setSurname(user.getSurname());
					u.setEmail(user.getEmail());
					u.setActivated(user.getActivated());
					return u;
				}).collect(Collectors.toList());
	}
}
