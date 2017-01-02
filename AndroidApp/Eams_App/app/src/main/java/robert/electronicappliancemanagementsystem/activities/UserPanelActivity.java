package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import robert.electronicappliancemanagementsystem.R;
import robert.electronicappliancemanagementsystem.http.HttpConnector;
import robert.electronicappliancemanagementsystem.http.dto.ReservationDTO;
import robert.electronicappliancemanagementsystem.http.requests.MyReservationsRequest;


public class UserPanelActivity extends Activity {

    private List<ReservationDTO> reservations = null;

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
            Type listType = new TypeToken<LinkedList<ReservationDTO>>() {
            }.getType();
            reservations = new Gson()
                    .fromJson((String) response, listType);
            loadReservations();
        }
    }

    private void loadReservations() {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations");
            return;
        }

        TableLayout table = (TableLayout) findViewById(R.id.tableOfReservations);

        for (ReservationDTO reservation : reservations) {
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView where = getNewColumn(reservation.getWhere());
            TextView appliance = getNewColumn(reservation.getAppliance());
            TextView availableFrom = getNewColumn(String.valueOf(reservation.getFrom()));
            TextView minutes = getNewColumn(String.valueOf(reservation.getMinutes()));

            row.addView(where);
            row.addView(appliance);
            row.addView(availableFrom);
            row.addView(minutes);

            table.addView(row);
        }
    }

    private TextView getNewColumn(String text) {
        TextView view = new TextView(getApplicationContext());
        view.setText(text);
        view.setTextColor(Color.BLACK);
        view.setTextSize(16f);
        return view;
    }
}
