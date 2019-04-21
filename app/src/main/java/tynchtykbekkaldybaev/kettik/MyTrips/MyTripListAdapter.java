package tynchtykbekkaldybaev.kettik.MyTrips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;

import tynchtykbekkaldybaev.kettik.IamPassenger.Driver;
import tynchtykbekkaldybaev.kettik.IamPassenger.DriverListAdapter;
import tynchtykbekkaldybaev.kettik.IamPassenger.Driver_Info;
import tynchtykbekkaldybaev.kettik.R;

/**
 * Created by tynchtykbekkaldybaev on 03/03/2019.
 */

public class MyTripListAdapter extends RecyclerView.Adapter<MyTripListAdapter.MyTripViewHolder> {
    private ArrayList<MyTrip> mytripList;
    private ArrayList<MyTrip_Info> mytripInfoList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String submitURL;

    public MyTripListAdapter(Context context, ArrayList<MyTrip> mytriplist, ArrayList<MyTrip_Info> mytripinfolist) {
        mInflater = LayoutInflater.from(context);
        mytripList = mytriplist;
        mytripInfoList = mytripinfolist;
        mContext = context;
    }


    @NonNull
    @Override
    public MyTripListAdapter.MyTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.mytrip_item, parent, false);
        return new MyTripListAdapter.MyTripViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyTripListAdapter.MyTripViewHolder holder, final int position) {
        final MyTrip item = mytripList.get(position);
        final MyTrip_Info item_info = mytripInfoList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.vyuvereny)
                        .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    mytripList.remove(position);
                                    mytripInfoList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mytripInfoList.size());
                                    notifyItemRangeChanged(position, mytripList.size());

                                } catch (IndexOutOfBoundsException e){
                                    e.printStackTrace();
                                }

                                submitURL =  "http://kettik.kundoluk.kg/api/trips" + "/" + String.valueOf(item.tripId);
                                try {
                                    collect_data(item_info, item);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                //builder.create();
                builder.show();


            }
        });


    }
    @Override
    public int getItemCount() {
        return mytripList.size();
    }


    public class MyTripViewHolder extends RecyclerView.ViewHolder{
        final MyTripListAdapter mytripAdapter;
        public final TextView from;
        public final TextView to;
        public final TextView date;
        public final ImageView delete;


        public MyTripViewHolder(View itemView, MyTripListAdapter adapter) {

            super(itemView);

            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            this.mytripAdapter = adapter;
        }
    }

    public void collect_data(MyTrip_Info mytrip_info, MyTrip mytrip) throws JSONException {
        JSONObject data = new JSONObject();
        /*data.put("id", mytrip.tripId);
        data.put("from", "DDDD");
        data.put("to", mytrip.to);
        data.put("tripDate", mytrip.date);
        data.put("tripTime", mytrip.time);
        data.put("price", Integer.valueOf(mytrip_info.price));
        data.put("seats", mytrip.free );
        data.put("note", "smth");
        data.put("statusFlag", true);
        data.put("parcelFlag", mytrip_info.parcelFlag);

        Log.e("ADAPTEREDITSEND", data.toString());*/

        requestThread task = new requestThread();
        task.execute(String.valueOf(data.toString()));
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;



        @Override
        protected String doInBackground(String... strings) {

            return sendData(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

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
                    //urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("DELETE");
 //                   urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

/*utputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(data);
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    Log.e("ADAPTEROTVET", sb.toString());
                    writer.close();
                    os.close();*/
                    int responseCode = urlConnection.getResponseCode();
                    Log.e("DELETEANSWER", String.valueOf(responseCode));
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
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
