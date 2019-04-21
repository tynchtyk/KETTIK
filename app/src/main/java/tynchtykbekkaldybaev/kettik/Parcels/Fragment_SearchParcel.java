package tynchtykbekkaldybaev.kettik.Parcels;

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
import android.widget.Button;
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

import tynchtykbekkaldybaev.kettik.IamPassenger.Driver;
import tynchtykbekkaldybaev.kettik.IamPassenger.DriverListAdapter;
import tynchtykbekkaldybaev.kettik.IamPassenger.Driver_Info;
import tynchtykbekkaldybaev.kettik.IamPassenger.Fragment_SearchDriver;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.Parcels.Parcel;
import tynchtykbekkaldybaev.kettik.Parcels.ParcelListAdapter;
import tynchtykbekkaldybaev.kettik.Parcels.Parcel_Request_Add;
import tynchtykbekkaldybaev.kettik.Parcels.Pop_Up_Search_Parcel;
import tynchtykbekkaldybaev.kettik.R;

import static android.app.Activity.RESULT_OK;

public class Fragment_SearchParcel extends Fragment {
    private ArrayList<Parcel> parcels = new ArrayList<>();
    private ArrayList<Parcel_Info> parcels_info = new ArrayList<>();

    private RecyclerView parcelRecyclerView;
    private ParcelListAdapter parcelAdapter;

    public String submitURLTrip;
    public String submitURLPassenger;
    public requestThread taskTrip, taskPassenger;
    public Fragment_SearchParcel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");

        View rootview = inflater.inflate(R.layout.fragment_search_parcels, container, false);
        final MainActivity tmp = (MainActivity) getActivity();


         View cView = getLayoutInflater().inflate(R.layout.actionbar_header, null);



        TextView search_in_action_bar = (TextView)  tmp.toolbarTitle;
        search_in_action_bar.setText(R.string.poiskmarwruta);
        search_in_action_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parcels.clear();
                parcels_info.clear();
                Intent intent;
                intent = new Intent(tmp, Pop_Up_Search_Parcel.class);
                startActivityForResult(intent, 1);
            }
        });

        parcelRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewParcels);


        //Log.d("FragSub", String.valueOf(tmp.menuLayout.getMenuEntries().size()));





        if(submitURLTrip == null)
            submitURLTrip = "http://kettik.kundoluk.kg/api/trips?parcelFlag=true";


        taskTrip = new requestThread();
        taskTrip.execute(submitURLTrip);

        if(submitURLPassenger == null)
            submitURLPassenger = "http://kettik.kundoluk.kg/api/passengers?parcelFlag=true";


        taskPassenger = new requestThread();
        taskPassenger.execute(submitURLPassenger);



        return rootview;
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;



        @Override
        protected String doInBackground(String... strings) {

            String JsonResponse = getData(strings[0]);

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



        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();



            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            parcelRecyclerView.setLayoutManager(mLinearLayoutManager);


            parcelAdapter = new ParcelListAdapter(getActivity(), parcels, parcels_info);
            parcelRecyclerView.setAdapter(parcelAdapter);

            Log.e("RESULT", result);
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }
        public void parce_data(String JsonResponse) throws JSONException {
            JSONArray jsonArray = new JSONArray(JsonResponse);
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String from, to, tripDate, tripTime;
                from = jsonObject.getString("from");
                to = jsonObject.getString("to");
                tripDate= jsonObject.getString("tripDate");
                tripTime = jsonObject.getString("tripTime");
                JSONObject info = jsonObject.getJSONObject("user");

                int Id = info.getInt("id");
                String name = info.getString("name");
                String surname = info.getString("surname");
                String birthDate = info.getString("birthDate");
                String gender = info.getString("gender");

                String phoneNumber = info.getString("countryCode") + info.getString("phoneNumber");

                parcels.add(new Parcel(from, to, tripDate + ", " + tripTime));

                parcels_info.add(new Parcel_Info(name, surname, birthDate, gender, phoneNumber, Id));


            }

        }
        public String getData(String submitURL){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String URLTrip, URLPassenger;
                String from = data.getStringExtra("from");
                String to = data.getStringExtra("to");
                String tripDate = data.getStringExtra("tripDate");
                URLTrip = "http://kettik.kundoluk.kg/api/trips?parcelFlag=true";
                URLPassenger = "http://kettik.kundoluk.kg/api/passengers?parcelFlag=true";
                if(!from.equals("")) {
                    URLTrip += "&from=" + from;
                    URLPassenger += "&from=" + from;
                }

                if(!to.equals("")) {
                    URLTrip += "&to=" + to;
                    URLPassenger += "&to=" + to;
                }
                if(!tripDate.equals("")) {
                    URLTrip += "&tripDate=" + tripDate;
                    URLPassenger += "&to=" + to;
                }

                Fragment_SearchParcel fragment = (Fragment_SearchParcel)
                        getFragmentManager().findFragmentById(R.id.content_frame);
                fragment.submitURLTrip =URLTrip;
                fragment.submitURLPassenger =URLPassenger;

                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
                Log.e("FROMTOTRIPDATE", from + to + tripDate);
                Log.e("SUBMIT1", submitURLTrip);

            }
        }
    }
}
