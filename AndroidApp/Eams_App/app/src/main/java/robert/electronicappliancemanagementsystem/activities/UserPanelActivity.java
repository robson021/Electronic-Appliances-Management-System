package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import robert.electronicappliancemanagementsystem.R;
import robert.electronicappliancemanagementsystem.http.HttpConnector;
import robert.electronicappliancemanagementsystem.http.dto.ReservationDTO;
import robert.electronicappliancemanagementsystem.http.requests.MyReservationsRequest;


public class UserPanelActivity extends Activity {

    private final MyReservationsRequest getUserReservationsRequest = new MyReservationsRequest(new ReservationsResponseListener());

    private List<ReservationDTO> reservations = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        findViewById(R.id.myReservationsButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getReservations();
                    }
                });
        getReservations();
    }

    private class ReservationsResponseListener implements Response.Listener {
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

    private void getReservations() {
        HttpConnector.getInstance()
                .sendRequest(getApplicationContext(), getUserReservationsRequest);
    }

    private void loadReservations() {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations");
            return;
        }
        TableLayout table = (TableLayout) findViewById(R.id.tableOfReservations);
        clearTableRows(table);
        table.setColumnShrinkable(2, true);
        for (ReservationDTO reservation : reservations) {
            TableRow row = getNewRow();
            TextView where = getNewColumn(reservation.getWhere());
            TextView appliance = getNewColumn(reservation.getAppliance());
            TextView availableFrom = getNewColumn(new Date(reservation.getFrom()).toGMTString());
            TextView minutes = getNewColumn(String.valueOf(reservation.getMinutes()) + " minutes");

            row.addView(where);
            row.addView(getSeparator());

            row.addView(appliance);
            row.addView(getSeparator());

            row.addView(availableFrom);
            row.addView(getSeparator());

            row.addView(minutes);
            row.addView(getSeparator());

            Button button = new Button(getApplicationContext());
            button.setText("Connect");
            button.setOnClickListener(new ConnectButtonAction(reservation.getId()));
            row.addView(button);

            table.addView(row);
        }
    }

    private TableRow getNewRow() {
        TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        return row;
    }

    private void clearTableRows(TableLayout table) {
        int childCount = table.getChildCount();
        if (childCount > 0) {
            table.removeViews(0, childCount);
        }
        generateColumnNames(table);
        //generateEmptyRow(table);
    }

    private void generateColumnNames(TableLayout table) {
        TableRow row = getNewRow();
        TextView where = getNewHeaderColumn("Where");
        TextView appliance = getNewHeaderColumn("Appliance");
        TextView availableFrom = getNewHeaderColumn("Available from");
        TextView time = getNewHeaderColumn("Time");
        row.addView(where);
        row.addView(getSeparator());
        row.addView(appliance);
        row.addView(getSeparator());
        row.addView(availableFrom);
        row.addView(getSeparator());
        row.addView(time);
        table.addView(row);
    }

    private TextView getNewHeaderColumn(String text) {
        TextView column = getNewColumn(text);
        column.setTextColor(Color.MAGENTA);
        return column;
    }

    private void generateEmptyRow(TableLayout table) {
        TableRow row = getNewRow();
        for (int i = 0; i < 4; i++) {
            row.addView(getNewColumn(" "));
        }
        table.addView(row);
    }

    private TextView getNewColumn(String text) {
        TextView view = new TextView(getApplicationContext());
        view.setText(text);
        view.setTextColor(Color.BLACK);
        view.setTextSize(16f);
        view.setTypeface(Typeface.SANS_SERIF);
        return view;
    }

    private TextView getSeparator() {
        return getNewColumn(" | ");
    }

    private class ConnectButtonAction implements View.OnClickListener {
        private final long id;

        ConnectButtonAction(long id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            System.out.println("click click " + id);
        }
    }
}
