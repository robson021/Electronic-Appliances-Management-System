package robert.electronicappliancemanagementsystem.http.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import robert.electronicappliancemanagementsystem.http.HttpConnector;

public class RegisterRequest extends StringRequest {

    private static final String uri = "/register/%s/%s/%s/%s/";

    public static final String NAME_PARAM = "name";
    public static final String SURNAME_PARAM = "name";
    public static final String PASSWORD_PARAM = "password";
    public static final String EMAIL_PARAM = "email";

    public RegisterRequest(Map<String, String> paramsMap, Response.Listener<String> listener) {
        super(Method.PUT, setUrl(paramsMap), listener, null);
    }

    private static String setUrl(Map<String, String> paramsMap) {
        String formattedUri = String.format(uri, //
                paramsMap.get(EMAIL_PARAM),
                paramsMap.get(PASSWORD_PARAM),
                paramsMap.get(NAME_PARAM),
                paramsMap.get(SURNAME_PARAM));
        return HttpConnector.getServerUrl() + formattedUri;
    }
}
