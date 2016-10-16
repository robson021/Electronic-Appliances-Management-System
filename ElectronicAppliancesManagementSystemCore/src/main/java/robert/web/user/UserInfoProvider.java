package robert.web.user;

public interface UserInfoProvider {

	void setEmail(String email);

	String getEmail();

	void enableAdminPrivileges();

	boolean isAdmin();

}
