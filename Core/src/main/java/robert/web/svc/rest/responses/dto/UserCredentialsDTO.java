package robert.web.svc.rest.responses.dto;

public class UserCredentialsDTO extends UserDTO {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
