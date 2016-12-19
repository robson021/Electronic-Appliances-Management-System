package robert.web.svc.rest.responses.json;

public class BasicDTO {

    private String text;

    public BasicDTO() {
    }

    public BasicDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
