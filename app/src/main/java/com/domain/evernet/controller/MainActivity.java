package com.domain.evernet.controller;

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
import android.widget.Toast;

import com.domain.evernet.R;
import com.domain.evernet.model.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private TextView phone;
    private TextView pseudo;
    private TextView password;
    private Button loginButton;
    private Client client;
    private String phoneInput;
    private String pseudoInput;
    private String passwordInput;

    public static final String PREF_PSEUDO = "PREF_PSEUDO";
    public static  String PHONE_NUMBER;

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


                boolean isPseudoTrue = false;
                boolean isPhonenumberTrue = false;

                boolean pseudoLengthTrue = true;
                boolean pseudoContainsTrue = true;

                boolean numberLengthTrue = true;
                boolean numbercontains = true;


                if (pseudoInput.length() > 9) {
                    Toast.makeText(getBaseContext(), "Taille max du pseudo 9 charactéres !", Toast.LENGTH_LONG).show();
                    pseudoLengthTrue = false;

                }
                if (pseudoInput.contains("*")) {
                    Toast.makeText(getBaseContext(), "Le pseudo ne peut pas contenir des  '*' !", Toast.LENGTH_LONG).show();
                    pseudoContainsTrue = false;

                }

                if ((pseudoLengthTrue && pseudoContainsTrue) == true) {
                    isPseudoTrue = true;
                }


                try {
                    Integer i = Integer.parseInt(phoneInput);
                } catch (NumberFormatException e) {
                    Toast.makeText(getBaseContext(), "Le numéro de téléphone ne peut contenir que des chiffres (0-9) !", Toast.LENGTH_LONG).show();
                    numbercontains = false;


                }
                if (!(phoneInput.length() == 10)) {
                    Toast.makeText(getBaseContext(), "Numéro de téléphone trop court ou trop long !", Toast.LENGTH_LONG).show();
                    numberLengthTrue = false;

                }

                if ((numbercontains && numberLengthTrue) == true) {
                    isPhonenumberTrue = true;
                }


                if (isPhonenumberTrue && isPseudoTrue) {

                    Intent dashboardActivityIntent = new Intent(MainActivity.this, DashboardActivity.class);

                    Client c = new Client("pdp-evernet.ddns.net", 50000);
                    c.openSocket();
                    try {
                        c.receiveDataFromServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        String phoneInput = phone.getText().toString().trim();
                        String pseudoInput = pseudo.getText().toString().trim();
                        String passwordInput = password.getText().toString().trim();
                        String s = c.signIn(pseudoInput, passwordInput, phoneInput, "martin");
                        if (s != null) {
                            startActivity(dashboardActivityIntent);
                            setDefaults(PREF_PSEUDO, pseudo.getText().toString(), getApplicationContext());
                            setDefaults(PHONE_NUMBER, phone.getText().toString(), getApplicationContext());
                            finish();
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Pseudo ou numéro de téléphone déjà utilisé !", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            phoneInput = phone.getText().toString().trim();
            pseudoInput = pseudo.getText().toString().trim();
            passwordInput = password.getText().toString().trim();

            loginButton.setEnabled(!phoneInput.isEmpty() && !pseudoInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
