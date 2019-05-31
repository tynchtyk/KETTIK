package tynchtykbekkaldybaev.kettik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import tynchtykbekkaldybaev.kettik.Description.Description;
import tynchtykbekkaldybaev.kettik.Registr.Login;
import tynchtykbekkaldybaev.kettik.Registr.Profile_Registration;

/**
 * Created by tynchtykbekkaldybaev on 20/12/2018.
 */

public class Choose_Language extends AppCompatActivity {
    private Button kg,ru,en;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language);


        kg = findViewById(R.id.kyrgyz);
        ru = findViewById(R.id.russian);
        en = findViewById(R.id.english);
        kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choose_Language.this, Description.class);

                Locale myLocale = new Locale("kg");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                userInfo.edit()
                        .putString("language", "kg")
                        .commit();
                startActivityForResult(intent, 1);

                /*Locale locale = new Locale("kg");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                startActivityForResult(intent, 1);*/
                //startActivity(intent);
            }
        });

        ru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choose_Language.this, Description.class);
                Locale locale = new Locale("ru");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                userInfo.edit()
                        .putString("language", "ru")
                        .commit();
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choose_Language.this, Description.class);
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                userInfo.edit()
                        .putString("language", "en")
                        .commit();
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });


        //Intent intent1 = new Intent();
        //setResult(RESULT_OK, intent1);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Description
            Intent intent1 = new Intent();
            setResult(RESULT_OK, intent1);
            finish();
        }

    }

}
