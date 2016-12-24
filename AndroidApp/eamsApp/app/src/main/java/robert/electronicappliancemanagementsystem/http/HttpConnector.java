package robert.electronicappliancemanagementsystem.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class HttpConnector {

    private static final HttpConnector instance = new HttpConnector();

    private static String serverUrl;

    private final Map<Context, RequestQueue> requestsQueuesMap = new HashMap<>(1);

    private HttpConnector() {
        setDefaultServerUrl();

        CookieManager manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }

    public synchronized void sendRequest(Context ctx, Request request) {
        RequestQueue queue = requestsQueuesMap.get(ctx);
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx);
            requestsQueuesMap.put(ctx, queue);
            System.out.println("New queue created for: " + ctx.getPackageName());
        }
        queue.add(request);
    }

    public static void setDefaultServerUrl() {
        //serverUrl = "http://localhost:8080";
        serverUrl = "http://192.168.1.7:8080";
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static HttpConnector getInstance() {
        return instance;
    }
}
