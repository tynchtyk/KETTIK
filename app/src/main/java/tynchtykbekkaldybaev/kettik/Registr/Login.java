package tynchtykbekkaldybaev.kettik.Registr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tynchtykbekkaldybaev.kettik.Drivers.Driver;
import tynchtykbekkaldybaev.kettik.Drivers.DriverListAdapter;
import tynchtykbekkaldybaev.kettik.Drivers.Driver_Info;
import tynchtykbekkaldybaev.kettik.Drivers.Driver_Trip_Add;
import tynchtykbekkaldybaev.kettik.Drivers.Fragment_SearchDriver;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.R;

public class Login extends AppCompatActivity {
    private ImageButton back;
    private Button login, signup;
    private EditText number, password;

    private String name, surname, cartype, carnumber, birthdate, gender;



    public String submitURL;
    public requestThread task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.back_button);
        number = findViewById(R.id.number);
        password = findViewById(R.id.password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()) {
                    if (submitURL == null) {
                        submitURL = "http://81.214.24.77:7777/api/users/params?countryCode=%2B996";
                        submitURL += "&phoneNumber=" + number.getText().toString() + "&password=" + password.getText().toString();
                    }
                    Log.e("SUBMITLOGIN", submitURL);
                    task = new requestThread();
                    task.execute(submitURL);
                }
                else {
                    Toast.makeText(Login.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup = findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public boolean check(){
        if(number.getText().toString().equals("")
                || password.getText().toString().equals("")
                )
            return false;
        return true;
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;



        @Override
        protected String doInBackground(String... strings) {

            String JsonResponse = getData();





            return JsonResponse;


        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(result.equals("")) {
                Toast.makeText(Login.this, "Неверный номер или пароль",Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Login.this.recreate();
                    }
                }, 1000);
                return ;
            }

            try {
                parce_data(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // this is expecting a response code to be sent from your server upon receiving the POST data

        }
        public void parce_data(String JsonResponse) throws JSONException {
            JSONObject info = new JSONObject(JsonResponse);
            int Id = info.getInt("id");
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
            String vehicleModel = info.getString("vehicleModel");
            String vehicleNumber = info.getString("vehicleNumber");
            String driverLicence = info.getString("driverLicense");


            SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
            userInfo.edit()
                    .putInt("Id", Id)
                    .putString("name", name)
                    .putString("surname", surname)
                    .putBoolean("islogin", true)
                    .commit();
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
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
            ConnectivityManager cm = (ConnectivityManager) Login.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
