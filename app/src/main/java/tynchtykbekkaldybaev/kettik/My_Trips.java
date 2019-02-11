package tynchtykbekkaldybaev.kettik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tynchtykbekkaldybaev.kettik.IamPassenger.Driver;
import tynchtykbekkaldybaev.kettik.IamPassenger.DriverListAdapter;
import tynchtykbekkaldybaev.kettik.IamPassenger.Driver_Info;

public class My_Trips extends AppCompatActivity {

    private ArrayList<Driver> driver = new ArrayList<>();
    private ArrayList<Driver_Info> driver_info = new ArrayList<>();

    private RecyclerView driverRecyclerView;
    private DriverListAdapter driverAdapter;

    public String submitURL;
    public requestThread task;

    public int Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);

        driverRecyclerView = findViewById(R.id.recyclerviewTrips);

        Intent intent = getIntent();
        Id = intent.getIntExtra("Id", -1);

           if(submitURL == null)
            submitURL = "http://81.214.24.77:7777/api/trips?userId=" + String.valueOf(Id);
        task = new requestThread();
        task.execute(submitURL);
    }

    public void Back(View view) {
        finish();
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;



        @Override
        protected String doInBackground(String... strings) {

            String JsonResponse = getData();

            try {
                parce_data(JsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return JsonResponse;


        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(My_Trips.this);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            driver.clear();
            driver_info.clear();

        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(My_Trips.this);
            driverRecyclerView.setLayoutManager(mLinearLayoutManager);


            driverAdapter = new DriverListAdapter(My_Trips.this, driver, driver_info);
            driverRecyclerView.setAdapter(driverAdapter);

            Log.e("RESULT", result);
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }
        public void parce_data(String JsonResponse) throws JSONException {
            JSONArray jsonArray = new JSONArray(JsonResponse);
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String from, to, tripDate, tripTime;
                int price, seats;
                from = jsonObject.getString("from");
                to = jsonObject.getString("to");
                tripDate= jsonObject.getString("tripDate");
                tripTime = jsonObject.getString("tripTime");
                price = jsonObject.getInt("price");
                seats = jsonObject.getInt("seats");

                JSONObject info = jsonObject.getJSONObject("user");

                int Id = info.getInt("id");
                String name = info.getString("name");
                String surname = info.getString("surname");
                String birthDate = info.getString("birthDate");
                String gender = info.getString("gender");

                String vehicleModel = info.getString("vehicleModel");
                String vehicleNumber = info.getString("vehicleNumber");
                String phoneNumber = info.getString("phoneNumber");

                driver.add(new Driver(from, to, tripDate + ", " + tripTime, seats, 5));

                driver_info.add(new Driver_Info(name, surname, birthDate, gender, String.valueOf(price), vehicleModel, vehicleNumber, phoneNumber, Id));


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
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
