package tynchtykbekkaldybaev.kettik.Registr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tynchtykbekkaldybaev.kettik.Description.Description;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.R;

public class Phone_Registration extends AppCompatActivity {
    private ImageButton back;
    private Button register;
    private EditText number;
    private String name, surname, cartype, carnumber, birthdate, password, gender;
    private String submitURL;

    public int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_registration);

        Intent intent = getIntent();
        Id = intent.getIntExtra("Id",-1);

        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        //carnumber = intent.getStringExtra("carnumber");
        //cartype = intent.getStringExtra("cartype");
        birthdate = intent.getStringExtra("birthdate");
        password = intent.getStringExtra("password");
        gender = intent.getStringExtra("gender");

        submitURL = "http://kettik.kundoluk.kg/api/users";
        if(Id != -1)
            submitURL += "/" + String.valueOf(Id);


        back = findViewById(R.id.back_button);
        number = findViewById(R.id.number);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent;
                intent = new Intent(Registration.this,Confirmation.class);
                startActivity(intent);*/

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
        data.put("name", name);
        data.put("surname", surname);
        data.put("birthDate", birthdate);
        data.put("gender", gender);
        data.put("profilePicture", "url");
        data.put("country", "kyrgyzstan");
        data.put("countryCode", "+996");
        data.put("phoneNumber", number.getText().toString());
        data.put("password", password);
        data.put("driverFlag", false);
        data.put("vehicleModel", "nan");
        data.put("vehicleNumber", "nan");
        data.put( "driverLicense","url");

        Log.e("SURNAME", surname);

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
            progressDialog = new ProgressDialog(Phone_Registration.this);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(result.equals("")) {
                Toast.makeText(Phone_Registration.this, R.string.proizowlaowibka,Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Phone_Registration.this.recreate();
                    }
                }, 1000);
            }
            else {

                try {
                    parce_data(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }

        public String sendData(String data){
            if ( checkConnection() ) {
                String JsonResponse = null;
                Log.e("Submiturl", submitURL);
                Log.e("SENDPHONEREGISTR", data);
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(submitURL);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    if(Id == -1)
                        urlConnection.setRequestMethod("POST");
                    else
                        urlConnection.setRequestMethod("PUT");
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
                        sb.append(line + "\n");
                    }
                    JsonResponse = sb.toString();

                    Log.e("PHONEREGISTROTVET", sb.toString());
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
    public void parce_data(String JsonResponse) throws JSONException {
        JSONObject info = new JSONObject(JsonResponse);
        int Id = info.getInt("id");
        if(Id == -1) {
            Toast.makeText(Phone_Registration.this, "USER ALREADY EXIST",Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Phone_Registration.this.recreate();
                }
            }, 1000);
        }
        String name = info.getString("name");
        String surname = info.getString("surname");
        String birthDate = info.getString("birthDate");
        String gender = info.getString("gender");
        String profilePicture = info.getString("profilePicture");

        //country
        //countrycode

        String phoneNumber = info.getString("phoneNumber");
        String getPassword = info.getString("password");
        boolean driverFlag = info.getBoolean("driverFlag");
        boolean activeFlag = info.getBoolean("activeFlag");
        String vehicleModel = info.getString("vehicleModel");
        String vehicleNumber = info.getString("vehicleNumber");
        String driverLicence = info.getString("driverLicense");


        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
        userInfo.edit()
                .putInt("Id", Id)
                .putString("name", name)
                .putString("surname", surname)
                .putBoolean("islogin", false)
                .putBoolean("driverFlag", driverFlag)
                .putBoolean("activeFlag", activeFlag)
                .putString("cartype", vehicleModel)
                .putString("carnumber", vehicleNumber)
                .putString("driverLicense", driverLicence)
                .putString("birthdate", birthDate)
                .putString("gender", gender)
                .putString("profilePicture", profilePicture)
                .putString("phoneNumber", phoneNumber)
                .commit();

        Intent intent;
        intent = new Intent(Phone_Registration.this,Confirmation.class);
        startActivityForResult(intent,1);

    }
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode == 1) { // Confirmation
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            else if(resultCode == RESULT_FIRST_USER){


            }
            else if(resultCode == RESULT_CANCELED) {
                //Intent intent = new Intent();
                //setResult(RESULT_CANCELED, intent);
                //finish();
            }
        }
    }

    public boolean checkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) Phone_Registration.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
