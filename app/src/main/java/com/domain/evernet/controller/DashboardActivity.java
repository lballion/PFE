package com.domain.evernet.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.domain.evernet.R;

import static com.domain.evernet.controller.MainActivity.PREF_PSEUDO;
import static com.domain.evernet.controller.MainActivity.getDefaults;

public class DashboardActivity extends AppCompatActivity {

    private String pseudo;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pseudo = getDefaults(PREF_PSEUDO, getApplicationContext());

        if (pseudo == null) {
            Intent dashboardActivityIntent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(dashboardActivityIntent);
        } else {
            // sinon on dans la DashboardActivity
        }
    }
}