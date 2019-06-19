package tynchtykbekkaldybaev.kettik.IamDriver;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.PassengerViewHolder> {
    private ArrayList<Passenger> passengerList;
    private ArrayList<Passenger_Info> passengerInfoList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String submitURL;

    public PassengerListAdapter(Context context, ArrayList<Passenger> list, ArrayList<Passenger_Info> passengerinfolist) {
        mInflater = LayoutInflater.from(context);
        passengerList = list;
        passengerInfoList = passengerinfolist;
        mContext = context;
    }


    @NonNull
    @Override
    public PassengerListAdapter.PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.passenger_item, parent, false);
        return new PassengerViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final PassengerListAdapter.PassengerViewHolder holder, final int position) {
        final Passenger item = passengerList.get(position);
        final Passenger_Info item_info = passengerInfoList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date + " " + item.time);
        final String s = " " + mContext.getString(R.string.chelovek);
        holder.passengers_quantity.setText(item.passengers + s);
        holder.price.setText(item_info.price);
        holder.currency.setText("сом");

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.pop_up_passenger_information);

                TextView name_surname = myDialog.findViewById(R.id.driver_name);
                name_surname.setText(item_info.name + " " + item_info.surname);

                TextView driver_age_and_gender = myDialog.findViewById(R.id.driver_age_and_gender);
                driver_age_and_gender.setText(item_info.birthDate + ", " + item_info.gender);

                TextView price = myDialog.findViewById(R.id.price);
                price.setText(item_info.price + " сом");

                Button call = myDialog.findViewById(R.id.call);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = item_info.phoneNumber;
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        view.getContext().startActivity(intent);
                    }
                });

                Button sms = myDialog.findViewById(R.id.sms);
                sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = item_info.phoneNumber;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phone +"&text="+"text"));
                        if (intent.resolveActivity(view.getContext().getPackageManager()) == null) {
                            Toast.makeText(view.getContext(), "Whatsapp not installed.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        view.getContext().startActivity(intent);
                    }
                });




                Button cancel = (Button) myDialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                Button agree = (Button) myDialog.findViewById(R.id.agree);
                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(item.passengers - 1 >= 0)
                            holder.passengers_quantity.setText(item.passengers - 1  + s);
                        else
                            holder.passengers_quantity.setText(0  + s);
                        submitURL =  "http://kettik.kundoluk.kg/api/passengers" + "/" + String.valueOf(item.requestId);
                        try {
                            collect_data(item_info, item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

        holder.call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.call.setBackgroundResource(R.drawable.callbutton);
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.call.setBackgroundResource(R.drawable.call_button_unpressed);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.call.setBackgroundResource(R.drawable.call_button_unpressed);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }


    public class PassengerViewHolder extends RecyclerView.ViewHolder{
        final PassengerListAdapter passengerAdapter;
        public final ImageView call;
        public final TextView from;
        public final TextView to;
        public final TextView date;
        public final TextView passengers_quantity;
        public final TextView price;
        public final TextView currency;

        public PassengerViewHolder(View itemView, PassengerListAdapter adapter) {

            super(itemView);

            call = (ImageView) itemView.findViewById(R.id.call);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            passengers_quantity = (TextView) itemView.findViewById(R.id.passenger_quantity);
            price = (TextView) itemView.findViewById(R.id.price);
            currency = (TextView) itemView.findViewById(R.id.currency);

            this.passengerAdapter = adapter;
        }
    }

    public void collect_data(Passenger_Info passenger_info, Passenger passenger) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("id", passenger.requestId);
        data.put("from", passenger.from);
        data.put("to", passenger.to);
        data.put("tripDate", passenger.date);
        data.put("tripTime", passenger.time);
        data.put("price", Integer.valueOf(passenger_info.price));
        if(passenger.passengers - 1 >= 0)
            data.put("passengers",passenger.passengers - 1);
        else
            data.put("passengers", 0);
        data.put("note", "smth");
        data.put("statusFlag", true);
        data.put("parcelFlag", passenger_info.parcelFlag);

        Log.e("ADAPTEREDITSEND", data.toString());

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
            progressDialog.setMessage("...");
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
                    urlConnection.setDoOutput(true);
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

                    Log.e("ADAPTEROTVET", sb.toString());
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
