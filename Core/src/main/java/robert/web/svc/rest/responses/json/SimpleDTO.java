package robert.web.svc.rest.responses.json;

public class SimpleDTO {

    private String text;

	public SimpleDTO() {
	}

	public SimpleDTO(String text) {
		this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
