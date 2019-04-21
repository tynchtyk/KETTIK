package tynchtykbekkaldybaev.kettik.IamPassenger;


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

import tynchtykbekkaldybaev.kettik.IamDriver.Passenger_Request_Add;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_SearchDriver extends Fragment {
    private ArrayList<Driver> driver = new ArrayList<>();
    private ArrayList<Driver_Info> driver_info = new ArrayList<>();

    private RecyclerView driverRecyclerView;
    private DriverListAdapter driverAdapter;

    public String submitURL;
    public requestThread task;

    LinearLayout addition;
    boolean isUp;
    Button add_request;
    TextView all_drivers,or;


    public Fragment_SearchDriver() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");


        View rootview = inflater.inflate(R.layout.fragment__search_driver, container, false);

        final MainActivity tmp = (MainActivity) getActivity();
        add_request = rootview.findViewById(R.id.add_request);
        driverRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewDrivers);
        addition = rootview.findViewById(R.id.addition_layout);
        all_drivers = rootview.findViewById(R.id.all_drivers);


        /*View cView = getLayoutInflater().inflate(R.layout.actionbar_header, null);*/

        TextView search_in_action_bar = (TextView)  tmp.toolbarTitle;
        search_in_action_bar.setText(R.string.poiskvoditelya);
        search_in_action_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(tmp, Pop_Up_Search_Driver.class);
                startActivityForResult(intent, 1);

            }
        });

        add_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tmp.Id == -1){
                    Toast.makeText(tmp, R.string.chtobydobavitmarwrut, Toast.LENGTH_SHORT).show();
                }
               else {
                    Intent intent;
                    intent = new Intent(tmp, Passenger_Request_Add.class);
                    intent.putExtra("Id", tmp.Id);
                    startActivityForResult(intent, 2);
                }
            }
        });

//        slideUp(addition);

        all_drivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // slideDown(addition);
            }
        });
        isUp = false;

        if(submitURL == null)
            submitURL = "http://kettik.kundoluk.kg/api/trips";


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
                URL = "http://kettik.kundoluk.kg/api/trips?";
                if(!from.equals(""))
                        URL += "&from=" + from;
                if(!to.equals(""))
                    URL += "&to=" + to;
                if(!tripDate.equals(""))
                    URL += "&tripDate=" + tripDate;
                Fragment_SearchDriver fragment = (Fragment_SearchDriver)
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

                String URL = "http://kettik.kundoluk.kg/api/trips";
                Fragment_SearchDriver fragment = (Fragment_SearchDriver)
                        getFragmentManager().findFragmentById(R.id.content_frame);
                fragment.submitURL = URL;

                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
                Log.e("SUBMIT2", submitURL);

            }
        }

    }
     public void slideUp(LinearLayout view){
        /*TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                100,  // fromYDelta
                view.getHeight()-400);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);*/
        or.setVisibility(View.GONE);
        add_request.setVisibility(View.GONE);

    }

    // slide the view from its current position to below itself
    public void slideDown(LinearLayout view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(addition);

        } else {
            slideUp(addition);
        }
        isUp = !isUp;
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

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            driverRecyclerView.setLayoutManager(mLinearLayoutManager);


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
                int tripID;
                int price, seats;
                from = jsonObject.getString("from");
                to = jsonObject.getString("to");
                tripDate= jsonObject.getString("tripDate");
                tripTime = jsonObject.getString("tripTime");
                price = jsonObject.getInt("price");
                seats = jsonObject.getInt("seats");
                boolean parcelFlag = jsonObject.getBoolean("parcelFlag");
                tripID = jsonObject.getInt("id");


                JSONObject info = jsonObject.getJSONObject("user");

                int userId = info.getInt("id");
                String name = info.getString("name");
                String surname = info.getString("surname");
                String birthDate = info.getString("birthDate");
                String gender = info.getString("gender");

                String vehicleModel = info.getString("vehicleModel");
                String vehicleNumber = info.getString("vehicleNumber");
                String phoneNumber = info.getString("countryCode") + info.getString("phoneNumber");

                driver.add(new Driver(tripID, from, to, tripDate, seats, tripTime));

                driver_info.add(new Driver_Info(name, surname, birthDate, gender, String.valueOf(price), vehicleModel, vehicleNumber, phoneNumber, userId, parcelFlag));


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