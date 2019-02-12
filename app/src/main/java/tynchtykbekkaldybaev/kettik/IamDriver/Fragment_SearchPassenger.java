package tynchtykbekkaldybaev.kettik.IamDriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tynchtykbekkaldybaev.kettik.IamPassenger.Driver_Trip_Add;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.R;
import tynchtykbekkaldybaev.kettik.Registr.Profile_Additional_Registration;

import static android.app.Activity.RESULT_OK;

public class Fragment_SearchPassenger extends Fragment {
    private ArrayList<Passenger> passengers = new ArrayList<>();
    private ArrayList<Passenger_Info> passengers_info = new ArrayList<>();

    private RecyclerView passengerRecyclerView;
    private PassengerListAdapter passengerAdapter;

    private Button add_trip;

    public String submitURL;
    public requestThread task;

    public Fragment_SearchPassenger() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");

        View rootview = inflater.inflate(R.layout.fragment_search_passenger, container, false);

        final MainActivity tmp = (MainActivity) getActivity();
        View cView = getLayoutInflater().inflate(R.layout.actionbar_header, null);

        TextView search_in_action_bar = (TextView)  cView.findViewById(R.id.search);
        search_in_action_bar.setText("Поиск пассажира");
        search_in_action_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(tmp, Pop_Up_Search_Passenger.class);
                startActivityForResult(intent,1);

            }
        });
        tmp.actionBar.setCustomView(cView);

        add_trip = rootview.findViewById(R.id.add_trip);
        add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tmp.Id == -1){
                    Toast.makeText(tmp, "Чтобы добавить запрос, войдите в систему", Toast.LENGTH_SHORT).show();
                }
                else  if(tmp.driverflag == false) {
                    Intent intent;
                    intent = new Intent(tmp,Profile_Additional_Registration.class);
                    intent.putExtra("Id", tmp.Id);
                    startActivityForResult(intent, 3);
                }
                else {
                    Intent intent;
                    intent = new Intent(tmp,Driver_Trip_Add.class);
                    intent.putExtra("Id", tmp.Id);
                    startActivityForResult(intent, 2);
                }
            }
        });

        passengerRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewPassengers);


        //Log.d("FragSub", String.valueOf(tmp.menuLayout.getMenuEntries().size()));


        if(submitURL == null)
            submitURL = "http://81.214.24.77:7777/api/passengers";
        task = new requestThread();
        task.execute(submitURL);

        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String URL;
                String from = data.getStringExtra("from");
                String to = data.getStringExtra("to");
                String tripDate = data.getStringExtra("tripDate");
                URL = "http://81.214.24.77:7777/api/passengers?";
                if(!from.equals(""))
                    URL += "&from=" + from;
                if(!to.equals(""))
                    URL += "&to=" + to;
                if(!tripDate.equals(""))
                    URL += "&tripDate=" + tripDate;
                Fragment_SearchPassenger fragment = (Fragment_SearchPassenger)
                        getFragmentManager().findFragmentById(R.id.content_frame);
                fragment.submitURL = URL;

                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
                Log.e("FROMTOTRIPDATE", from + to + tripDate);
                Log.e("SUBMIT1", submitURL);

            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {

                String URL = "http://81.214.24.77:7777/api/passengers";
                Fragment_SearchPassenger fragment = (Fragment_SearchPassenger)
                        getFragmentManager().findFragmentById(R.id.content_frame);
                fragment.submitURL = URL;

                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
                Log.e("SUBMIT2", submitURL);

            }
        }
        if (requestCode == 3) {
            if(resultCode == RESULT_OK) {

                final MainActivity tmp = (MainActivity) getActivity();
                SharedPreferences userInfo = tmp.getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                tmp.Id = userInfo.getInt("Id", -1);
                tmp.driverflag = userInfo.getBoolean("driverFlag", false);
                Intent intent;
                intent = new Intent(tmp,Driver_Trip_Add.class);
                intent.putExtra("Id", tmp.Id);
                startActivityForResult(intent, 2);

            }
        }

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
            passengers.clear();
            passengers_info.clear();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            passengerRecyclerView.setLayoutManager(mLinearLayoutManager);

            passengerAdapter = new PassengerListAdapter(getActivity(), passengers, passengers_info);
            passengerRecyclerView.setAdapter(passengerAdapter);

            Log.e("RESULT", result);
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }
        public void parce_data(String JsonResponse) throws JSONException {
            JSONArray jsonArray = new JSONArray(JsonResponse);
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String from, to, tripDate, tripTime;
                int price, passenger = 5;
                from = jsonObject.getString("from");
                to = jsonObject.getString("to");
                tripDate= jsonObject.getString("tripDate");
                tripTime = jsonObject.getString("tripTime");
                price = jsonObject.getInt("price");
                //if(jsonObject.get("passengers") != null)
                passenger = jsonObject.getInt("passengers");

                Log.e("PASSENGERS", String.valueOf(passenger));

                JSONObject info = jsonObject.getJSONObject("user");

                int Id = info.getInt("id");
                String name = info.getString("name");
                String surname = info.getString("surname");
                String birthDate = info.getString("birthDate");
                String gender = info.getString("gender");

                String vehicleModel = info.getString("vehicleModel");
                String vehicleNumber = info.getString("vehicleNumber");
                String phoneNumber = info.getString("phoneNumber");

                passengers.add(new Passenger(from, to, tripDate + ", " + tripTime, passenger));

                passengers_info.add(new Passenger_Info(name, surname, birthDate, gender, String.valueOf(price), vehicleModel, vehicleNumber, phoneNumber, Id));


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
