package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import robert.electronicappliancemanagementsystem.R;
import robert.electronicappliancemanagementsystem.http.HttpConnector;
import robert.electronicappliancemanagementsystem.http.requests.LoginRequest;
import robert.electronicappliancemanagementsystem.utils.BasicUtils;

public class LoginActivity extends Activity {

    private EditText emailTextField;

    private EditText passwordTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new RegisterButtonAction());

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginButtonAction());

        emailTextField = (EditText) findViewById(R.id.emailField);
        passwordTextField = (EditText) findViewById(R.id.passwdField);
    }

    private class RegisterButtonAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }

    private class LoginButtonAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String email = emailTextField.getText().toString();
            String password = passwordTextField.getText().toString();

            Map<String, String> params = new HashMap<>();
            params.put(LoginRequest.PASSWORD_PARAM, password);
            params.put(LoginRequest.EMAIL_PARAM, email);

            HttpConnector.getInstance()
                    .sendRequest(getApplicationContext(), new LoginRequest(params, new LoginResponse()));
        }
    }

    private class LoginResponse implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            String text;
            try {
                text = response.getString("text");
            } catch (JSONException e) {
                return;
            }
            System.out.println(text);
            if ("OK".equals(text)) {
                Intent intent = new Intent(LoginActivity.this, UserPanelActivity.class);
                LoginActivity.this.startActivity(intent);
            } else {
                BasicUtils.showToast("Could not log in", getApplicationContext());
            }
        }
    }
}
