package com.domain.evernet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView phone;
    private TextView pseudo;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = (TextView) findViewById(R.id.phone);
        pseudo = (TextView) findViewById(R.id.pseudo);
        loginButton = (Button) findViewById(R.id.login);

        phone.addTextChangedListener(loginTextWatcher);
        pseudo.addTextChangedListener(loginTextWatcher);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboardActivityIntent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(dashboardActivityIntent);
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String usernameInput = phone.getText().toString().trim();
            String passwordInput = pseudo.getText().toString().trim();

            loginButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}