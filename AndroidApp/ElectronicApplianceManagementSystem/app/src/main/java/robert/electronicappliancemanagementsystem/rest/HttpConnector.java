package robert.electronicappliancemanagementsystem.rest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HttpConnector {

    private static final HttpConnector instance = new HttpConnector();

    private static String serverUrl;

    private final Map<Context, RequestQueue> requestsQueuesMap = new HashMap<>(1);


    private HttpConnector() {
        setDefaultServerUrl();
    }

    public synchronized void sendRequest(Context ctx, Request request) {
        RequestQueue queue = requestsQueuesMap.get(ctx);
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx);
            requestsQueuesMap.put(ctx, queue);
        }
        queue.add(request);
    }

    public static void setDefaultServerUrl() {
        serverUrl = "http://localhost:8080";
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static HttpConnector getInstance() {
        return instance;
    }
}
