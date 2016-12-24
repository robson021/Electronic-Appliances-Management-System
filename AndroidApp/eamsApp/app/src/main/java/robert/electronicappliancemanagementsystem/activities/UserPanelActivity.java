package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;

import robert.electronicappliancemanagementsystem.R;
import robert.electronicappliancemanagementsystem.http.HttpConnector;
import robert.electronicappliancemanagementsystem.http.requests.MyReservationsRequest;

public class UserPanelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        Button myReservationsButton = (Button) findViewById(R.id.myReservationsButton);

        myReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpConnector.getInstance()
                        .sendRequest(getApplicationContext(), new MyReservationsRequest(new MyReservationsResponseListener()));
            }
        });
    }

    private class MyReservationsResponseListener implements Response.Listener {
        @Override
        public void onResponse(Object response) {
            System.out.println(response);
        }
    }
}
