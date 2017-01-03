package robert.electronicappliancemanagementsystem.http.requests;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import robert.electronicappliancemanagementsystem.http.HttpConnector;

public class LoginRequest extends StringRequest {

    private static final String uri = "/login/%s/%s/";

    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";

    public LoginRequest(Map<String, String> paramsMap, Response.Listener<String> listener) {
        super(Request.Method.POST, setUrl(paramsMap), listener, null);
    }

    private static String setUrl(Map<String, String> paramsMap) {
        String email = paramsMap.get(EMAIL_PARAM);
        String password = paramsMap.get(PASSWORD_PARAM);
        return HttpConnector.getServerUrl() + String.format(uri, email, password);
    }
}
