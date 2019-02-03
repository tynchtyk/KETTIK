package tynchtykbekkaldybaev.kettik.Registr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

import tynchtykbekkaldybaev.kettik.Drivers.Driver_Trip_Add;
import tynchtykbekkaldybaev.kettik.R;

public class Registration extends AppCompatActivity {
    private ImageButton back;
    private Button register;
    private EditText number;
    private String name, surname, cartype, carnumber, birthdate, password, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        carnumber = intent.getStringExtra("carnumber");
        cartype = intent.getStringExtra("cartype");
        birthdate = intent.getStringExtra("birthdate");
        password = intent.getStringExtra("password");
        gender = intent.getStringExtra("gender");


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
        data.put("driverFlag", true);
        data.put("vehicleModel", cartype);
        data.put("vehicleNumber", carnumber);
        data.put( "driverLicense","url");

        Log.e("SURNAME", surname);

        requestThread task = new requestThread();
        task.execute(String.valueOf(data.toString()));
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        private String submitURL =
                "http://81.214.24.77:7777/api/users";

        @Override
        protected String doInBackground(String... strings) {

            return sendData(strings[0]);
//            return getData();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Registration.this);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e("RESULT", result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            finish();
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }

        public String sendData(String data){
            if ( checkConnection() ) {
                String JsonResponse = null;
                Log.e("SEND", data);
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
                        sb.append(line + "\n");
                    }

                    Log.e("OTVET", sb.toString());
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
        public String getData(){
            if ( checkConnection() ) {
                String JsonResponse = null;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(submitURL);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.connect();

                    InputStream is = urlConnection.getInputStream();
                    BufferedReader breader = new BufferedReader(new InputStreamReader(is, "UTF-8"));


                    String line = null;

                    // Read Server Response
                    while ((line = breader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line + "\n");
                    }

                    urlConnection.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }

                return sb.toString();
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
            ConnectivityManager cm = (ConnectivityManager) Registration.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
