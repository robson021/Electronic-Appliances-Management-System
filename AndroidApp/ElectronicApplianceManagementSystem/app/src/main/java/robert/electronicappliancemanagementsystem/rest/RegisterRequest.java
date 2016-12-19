package robert.electronicappliancemanagementsystem.rest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

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
        String address = HttpConnector.getServerUrl() + formattedUri;
        System.out.println(address);
        return address;
    }
}
