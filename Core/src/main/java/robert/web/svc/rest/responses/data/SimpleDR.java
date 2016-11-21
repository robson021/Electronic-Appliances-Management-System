package robert.web.svc.rest.responses.data;

public class SimpleDR {

    private String text;

    public SimpleDR() {
    }

    public SimpleDR(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
