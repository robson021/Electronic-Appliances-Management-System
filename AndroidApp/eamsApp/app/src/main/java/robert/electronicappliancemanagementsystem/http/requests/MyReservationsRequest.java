package robert.electronicappliancemanagementsystem.http.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import robert.electronicappliancemanagementsystem.http.HttpConnector;

public class MyReservationsRequest extends StringRequest {

    private static final String uri = "/user-service/my-reservations/";

    public MyReservationsRequest(Response.Listener<String> listener) {
        super(Method.GET, HttpConnector.getServerUrl() + uri, listener, null);
    }

}
