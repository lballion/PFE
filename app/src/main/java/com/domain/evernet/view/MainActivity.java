package com.domain.evernet.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.domain.evernet.R;
import com.domain.evernet.controller.Client;
import com.domain.evernet.controller.ReadWriteFile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView phone;
    private TextView pseudo;
    private TextView password;
    private Button loginButton;
    private Client client;

    public static final String PREF_PSEUDO = "PREF_PSEUDO";

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_main);

        phone = (TextView) findViewById(R.id.phone);
        pseudo = (TextView) findViewById(R.id.pseudo);
        password = (TextView) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);

        phone.addTextChangedListener(loginTextWatcher);
        pseudo.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Intent dashboardActivityIntent = new Intent(MainActivity.this, DashboardActivity.class);

                InetAddress i = null;
                try {
                    i = InetAddress.getByName("109.215.55.162");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                Client c = new Client(i, 50000);
                c.openSocket();

                try {
                    String phoneInput = phone.getText().toString().trim();
                    String pseudoInput = pseudo.getText().toString().trim();
                    String passwordInput = password.getText().toString().trim();
                    c.signIn(pseudoInput, passwordInput, phoneInput, "martin");
                    c.closeSocket();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startActivity(dashboardActivityIntent);
                setDefaults(PREF_PSEUDO, pseudo.getText().toString(), getApplicationContext());
                finish();
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String phoneInput = phone.getText().toString().trim();
            String pseudoInput = pseudo.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            loginButton.setEnabled(!phoneInput.isEmpty() && !pseudoInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}