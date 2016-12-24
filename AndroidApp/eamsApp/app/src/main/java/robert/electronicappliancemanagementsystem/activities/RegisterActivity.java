package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import robert.electronicappliancemanagementsystem.R;
import robert.electronicappliancemanagementsystem.http.HttpConnector;
import robert.electronicappliancemanagementsystem.http.requests.RegisterRequest;
import robert.electronicappliancemanagementsystem.utils.BasicUtils;

public class RegisterActivity extends Activity {

    private static final short MIN_PASSWORD_LENGTH = 8;

    private Context ctx;

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ctx = getApplicationContext();

        Button registerButton = (Button) findViewById(R.id.registerButton);
        name = (EditText) findViewById(R.id.nameField);
        surname = (EditText) findViewById(R.id.surnameField);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        repassword = (EditText) findViewById(R.id.rePasswordField);

        registerButton.setOnClickListener(new registerButtonAction());

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class registerButtonAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!validatePasswords()) {
                BasicUtils.showToast("Passwords do not match.", ctx);
            } else {
                RegisterRequest registerRequest = new RegisterRequest(getParamsMap(), new RegisterResponseListener());
                HttpConnector.getInstance()
                        .sendRequest(ctx, registerRequest);
            }

        }
    }

    private class RegisterResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            BasicUtils.showToast(response, ctx);
        }
    }

    private Map<String, String> getParamsMap() {
        Map<String, String> map = new HashMap<>(4);
        map.put(RegisterRequest.NAME_PARAM, name.getText().toString());
        map.put(RegisterRequest.SURNAME_PARAM, surname.getText().toString());
        map.put(RegisterRequest.EMAIL_PARAM, email.getText().toString());
        map.put(RegisterRequest.PASSWORD_PARAM, password.getText().toString());
        return map;
    }

    private boolean validatePasswords() {
        String passwd = password.getText().toString();
        if (passwd.length() < MIN_PASSWORD_LENGTH) return false;
        String repasswd = repassword.getText().toString();
        return repasswd.equals(passwd);
    }
}
