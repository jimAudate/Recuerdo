package com.example.recuerdo;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

private final static String F="FIRSTTIME";
private final static String P="PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirstTimePref();
        NextTimePref();
        finish();

    }

    private void NextTimePref() {
       SharedPreferences mPrefs = getSharedPreferences("datos", MODE_PRIVATE);
        //String pass = mPrefs.getString("Password", "");

        if (!mPrefs.contains("Password")) {
            Intent reg = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(reg);
            } else {
                Intent log = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(log);
            }
    }


    // Fetch the stored data in onResume()
    // Because this is what will be called
    // when the app opens again
    @Override
    protected void onResume() {
        super.onResume();

    }
    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored
    @Override
    protected void onPause() {
        super.onPause();

    }
        //grabamos password en datos/SharePreferences
    private void FirstTimePref() {
        // verify if user exist si no redirect to register
        SharedPreferences mPrefs = getSharedPreferences("datos",0);
            SharedPreferences.Editor editorSP;
            editorSP = mPrefs.edit();
            editorSP.putString(F, "YES");
            editorSP.apply();


    }






}