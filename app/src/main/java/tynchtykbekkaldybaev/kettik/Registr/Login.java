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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tynchtykbekkaldybaev.kettik.R;

public class Login extends AppCompatActivity {
    private ImageButton back;
    private Button login, signup;
    private EditText number, password;
    private TextView country_code;

    private String name, surname, cartype, carnumber, birthdate, gender;

    private Spinner spinner;

    public String submitURL;
    public requestThread task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        country_code = findViewById(R.id.country_code);
        spinner = findViewById(R.id.country_selection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Кыргызстан"))
                    country_code.setText("+996");
                else if(selectedItem.equals("Россия"))
                    country_code.setText("+7");
                else
                    country_code.setText("+7");

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        back = findViewById(R.id.back_button);
        number = findViewById(R.id.number);
        password = findViewById(R.id.password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();

            }
        });

        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()) {
                    if (submitURL == null) {
                        submitURL = "http://kettik.kundoluk.kg/api/users/params?countryCode=%2B996";
                        submitURL += "&phoneNumber=" + number.getText().toString() + "&password=" + password.getText().toString();
                    }
                    Log.e("SUBMITLOGIN", submitURL);
                    task = new requestThread();
                    task.execute(submitURL);
                }
                else {
                    Toast.makeText(Login.this, R.string.zapolnitevsepolya, Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_FIRST_USER, intent);
                finish();
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
                Toast.makeText(Login.this, R.string.neverniyloginiliparol,Toast.LENGTH_SHORT).show();
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
            Log.e("LOGINJSONRESULT", JsonResponse);
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
            Log.e("DRIVERFLAG", String .valueOf(driverFlag));
            String vehicleModel = info.getString("vehicleModel");
            String vehicleNumber = info.getString("vehicleNumber");
            String driverLicence = info.getString("driverLicense");


            SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
            userInfo.edit()
                    .putInt("Id", Id)
                    .putString("name", name)
                    .putString("surname", surname)
                    .putBoolean("islogin", true)
                    .putBoolean("driverFlag", driverFlag)
                    .putString("cartype", vehicleModel)
                    .putString("carnumber", vehicleNumber)
                    .putString("driverLicense", driverLicence)
                    .putString("birthdate", birthDate)
                    .putString("gender", gender)
                    .putString("profilePicture", profilePicture)
                    .putString("phoneNumber", phoneNumber)
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
