package robert.web.svc.rest.responses.asm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import robert.db.entity.User;
import robert.web.svc.rest.responses.json.UserDTO;

public class UserAssembler {

	public static List<UserDTO> convertToUserDTO(Collection<User> users) {
		return users.stream()
				.map(user -> {
					UserDTO u = new UserDTO();
					u.setName(user.getName());
					u.setSurname(user.getSurname());
					u.setEmail(user.getEmail());
					u.setActivated(user.getActivated());
					return u;
				}).collect(Collectors.toList());
	}
}
