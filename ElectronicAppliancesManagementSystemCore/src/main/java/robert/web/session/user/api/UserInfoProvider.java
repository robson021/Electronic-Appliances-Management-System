package robert.web.session.user.api;

public interface UserInfoProvider {

	void setEmail(String email);

	String getEmail();

	void enableAdminPrivileges();

	boolean isAdmin();

}
