package robert.electronicappliancemanagementsystem.http.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

import robert.electronicappliancemanagementsystem.http.HttpConnector;

public class RegisterRequest extends JsonObjectRequest {

    private static final String uri = "/register/";

    public static final String NAME_PARAM = "name";
    public static final String SURNAME_PARAM = "surname";
    public static final String PASSWORD_PARAM = "password";
    public static final String EMAIL_PARAM = "email";

    public RegisterRequest(Map<String, String> params, Response.Listener<JSONObject> listener) {
        super(Method.PUT, setUrl(), getJsonRequest(params), listener, null);
    }

    private static JSONObject getJsonRequest(Map<String, String> params) {
        return new JSONObject(params);
    }

    private static String setUrl() {
        return HttpConnector.getServerUrl() + uri;
    }
}
