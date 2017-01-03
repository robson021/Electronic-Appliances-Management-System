package robert.electronicappliancemanagementsystem.http.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import robert.electronicappliancemanagementsystem.http.HttpConnector;

public class ConnectToTheApplianceRequest extends StringRequest {

    private static final String uri = "/user-service/connect-to-appliance/%s/";

    public ConnectToTheApplianceRequest(Response.Listener<String> listener, long id) {
        super(Method.POST, HttpConnector.getServerUrl() + String.format(uri, id), listener, null);
    }
}
