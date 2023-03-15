package com.example.projectpmdm.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.projectpmdm.R;

public class PreferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}