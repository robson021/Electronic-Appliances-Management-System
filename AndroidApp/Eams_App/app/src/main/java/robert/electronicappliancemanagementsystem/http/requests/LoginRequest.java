package robert.electronicappliancemanagementsystem.http.requests;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

import robert.electronicappliancemanagementsystem.http.HttpConnector;

public class LoginRequest extends JsonObjectRequest {

    private static final String uri = "/login/";

    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";

    public LoginRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Request.Method.POST, setUrl(), new JSONObject(paramsMap), listener, null);
    }

    private static String setUrl() {
        return HttpConnector.getServerUrl() + uri;
    }
}
