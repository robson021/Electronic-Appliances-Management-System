package robert.electronicappliancemanagementsystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import robert.electronicappliancemanagementsystem.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new RegisterButtonAction());
    }

    private class RegisterButtonAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }
}
