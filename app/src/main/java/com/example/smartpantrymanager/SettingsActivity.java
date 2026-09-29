package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "pantry_settings";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_METRIC_UNITS = "metric_units";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Switch switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        Switch switchMetricUnits = findViewById(R.id.switchMetricUnits);

        switchExpiryAlerts.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, false));
        switchMetricUnits.setChecked(prefs.getBoolean(KEY_METRIC_UNITS, true));

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());

        switchMetricUnits.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_METRIC_UNITS, isChecked).apply());
    }
}