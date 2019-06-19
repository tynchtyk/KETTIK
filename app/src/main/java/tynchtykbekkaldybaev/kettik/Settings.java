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
    public Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void languageSettings(View view) {
        Intent intent = new Intent(this, LanguageSelection.class);
        startActivityForResult(intent, 1);
    }

    public void locationSettings(View view) {
    }

    public void myTrips(View view) {
    }


    public void Back(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) { //select language
            if(resultCode == RESULT_OK) {
                intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

}
