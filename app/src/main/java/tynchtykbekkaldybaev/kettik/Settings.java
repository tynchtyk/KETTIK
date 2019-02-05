package tynchtykbekkaldybaev.kettik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import tynchtykbekkaldybaev.kettik.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void languageSettings(View view) {
        Intent intent = new Intent(this, LanguageSelection.class);
        startActivity(intent);
    }

    public void locationSettings(View view) {
    }

    public void myTrips(View view) {
    }

    public void logOut(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void Back(View view) {
        finish();
    }
}
