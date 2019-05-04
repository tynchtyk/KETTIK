package tynchtykbekkaldybaev.kettik.Registr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tynchtykbekkaldybaev.kettik.R;

public class Confirmation extends AppCompatActivity {
    private ImageButton back;
    private Button register;
    private String submitURL;
    private String submitURL2;
    private EditText code;
    private Boolean CODE;
    public int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);



        back = findViewById(R.id.back_button);
        register = findViewById(R.id.register);
        code = findViewById(R.id.code);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
        Id = userInfo.getInt("Id", -1);
        submitURL = "http://kettik.kundoluk.kg/api/users/" + String.valueOf(Id) + "/activate";
        submitURL2 = "http://kettik.kundoluk.kg/api/users/" + String.valueOf(Id) + "/code";

        requestThread2 task2 = new requestThread2();
        task2.execute();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    collect_data();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void collect_data() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("code", Integer.valueOf(code.getText().toString()));



        requestThread task = new requestThread();
        task.execute(String.valueOf(data.toString()));
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... strings) {

            return sendData(strings[0]);
//            return getData();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Confirmation.this);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(result.equals("")) {
                Toast.makeText(Confirmation.this, R.string.proizowlaowibka,Toast.LENGTH_SHORT).show();

            }
            else {

                Intent intent = new Intent();
                Log.e("CODE", String.valueOf(CODE));
                if(CODE) {
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Log.e("CODEFALSE", String.valueOf(RESULT_FIRST_USER));
                    //setResult(RESULT_FIRST_USER, intent);
                    Toast.makeText(Confirmation.this,"CODE IS NOT TRUE, RESEND",Toast.LENGTH_SHORT).show();
                }

            }
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }

        public String sendData(String data){
            if ( checkConnection() ) {
                String JsonResponse = null;
                Log.e("Submiturl", submitURL);
                Log.e("SENDPHONECONFIRMATION", data);
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(submitURL);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(data);
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line);
                    }
                    JsonResponse = sb.toString();

                    CODE = Boolean.parseBoolean(sb.toString());

                    Log.e("PHONECONFIRMATIONOTVET", ":" + sb.toString() + ":");
                    Log.e("PHONECONFIRMATIONCODE", String.valueOf(CODE));

                    writer.close();
                    os.close();
                    urlConnection.connect();

                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }

                return JsonResponse;
            } else {
                return "";
            }
        }
    }

    //sen code thread
    public class requestThread2 extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... strings) {

            return sendData();
//            return getData();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {


        }

        public String sendData(){
            if ( checkConnection() ) {
                String JsonResponse = null;
                Log.e("Submiturl", submitURL2);

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(submitURL2);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write("");
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                    JsonResponse = sb.toString();

                    writer.close();
                    os.close();
                    urlConnection.connect();

                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }

                return JsonResponse;
            } else {
                return "";
            }
        }
    }
    public boolean checkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) Confirmation.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (haveConnectedWifi || haveConnectedMobile);
    }

}
