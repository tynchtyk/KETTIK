package tynchtykbekkaldybaev.kettik;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tynchtykbekkaldybaev.kettik.Drivers.Driver;
import tynchtykbekkaldybaev.kettik.Drivers.DriverListAdapter;
import tynchtykbekkaldybaev.kettik.Drivers.Driver_Info;
import tynchtykbekkaldybaev.kettik.Drivers.Driver_Trip_Add;
import tynchtykbekkaldybaev.kettik.Drivers.Pop_Up_Search_Driver;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_My_Trips extends Fragment {
    private ArrayList<Driver> driver = new ArrayList<>();
    private ArrayList<Driver_Info> driver_info = new ArrayList<>();

    private RecyclerView driverRecyclerView;
    private DriverListAdapter driverAdapter;

    public String submitURL;
    public requestThread task;

    LinearLayout addition;
    boolean isUp;
    Button add_trip;
    TextView all_drivers,or;


    public Fragment_My_Trips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");


        View rootview = inflater.inflate(R.layout.fragment_my_trips, container, false);

        final MainActivity tmp = (MainActivity) getActivity();
        driverRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewTrips);
        addition = rootview.findViewById(R.id.addition_layout);
        all_drivers = rootview.findViewById(R.id.my_trips);


        View cView = getLayoutInflater().inflate(R.layout.actionbar_header, null);
        TextView search_in_action_bar = (TextView)  cView.findViewById(R.id.search);
        search_in_action_bar.setText("Мои поездки");

        tmp.actionBar.setCustomView(cView);


//        slideUp(addition);

        all_drivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // slideDown(addition);
            }
        });
        isUp = false;

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        driverRecyclerView.setLayoutManager(mLinearLayoutManager);
        if(submitURL == null)
            submitURL = "http://81.214.24.77:7777/api/trips?id==7";
        task = new requestThread();
        task.execute(submitURL);



        return rootview;
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
            progressDialog = new ProgressDialog(getActivity());
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

            driverAdapter = new DriverListAdapter(getActivity(), driver, driver_info);
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
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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