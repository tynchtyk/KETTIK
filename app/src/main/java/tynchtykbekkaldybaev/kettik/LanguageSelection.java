package tynchtykbekkaldybaev.kettik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import java.util.Locale;

import tynchtykbekkaldybaev.kettik.Description.Description;

public class LanguageSelection extends AppCompatActivity {
    public AlertDialog.Builder builder;
    public ImageView kyrgyz, russian, english;
    public Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        kyrgyz = (ImageView) findViewById(R.id.kyrgyz_check);
        russian = (ImageView) findViewById(R.id.russian_check);
        english = (ImageView) findViewById(R.id.english_check);
        builder = new AlertDialog.Builder(this);

        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
        String lan = userInfo.getString("language", "ru");


        //builder.create();

    }

    public void Russian(View view) {
        builder.setMessage("Вы уверены что хотите Выбрать этот язык?")
                .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Locale myLocale = new Locale("ru");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                        userInfo.edit()
                                .putString("language", "ru")
                                .commit();
                        intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                })
                .setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
      /*  russian.setVisibility(View.VISIBLE);
        kyrgyz.setVisibility(View.INVISIBLE);
        english.setVisibility(View.INVISIBLE);*/
    }

    public void Kyrgyz(View view) {
        builder.setMessage("Вы уверены что хотите Выбрать этот язык?")
                .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
                        intent = new Intent();
                        setResult(RESULT_OK,  intent);
                        finish();
                    }
                })
                .setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
      /*  kyrgyz.setVisibility(View.VISIBLE);
        russian.setVisibility(View.INVISIBLE);
        english.setVisibility(View.INVISIBLE);*/
    }

    public void English(View view) {
        builder.setMessage("Вы уверены что хотите Выбрать этот язык?")
                .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Locale myLocale = new Locale("en");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                        userInfo.edit()
                                .putString("language", "en")
                                .commit();
                        intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
       /* english.setVisibility(View.VISIBLE);
        russian.setVisibility(View.INVISIBLE);
        kyrgyz.setVisibility(View.INVISIBLE);*/
    }

    public void Back(View view) {
        finish();
    }
}
