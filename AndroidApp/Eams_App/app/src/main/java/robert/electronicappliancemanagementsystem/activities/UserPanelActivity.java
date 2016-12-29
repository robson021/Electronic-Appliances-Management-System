package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import robert.electronicappliancemanagementsystem.R;
import robert.electronicappliancemanagementsystem.http.HttpConnector;
import robert.electronicappliancemanagementsystem.http.dto.ReservationDTO;
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
            Type listType = new TypeToken<ArrayList<ReservationDTO>>() {
            }.getType();
            List<ReservationDTO> listOfDTO = new Gson().fromJson((String) response, listType);
            System.out.println(listOfDTO.toString());
        }
    }
}
