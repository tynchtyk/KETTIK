package tynchtykbekkaldybaev.kettik.Drivers;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import tynchtykbekkaldybaev.kettik.R;

/**
 * Created by tynchtykbekkaldybaev on 24/01/2019.
 */

public class Driver_Trip_Add extends AppCompatActivity {
    private TextView date;
    private EditText from, where, price, quantity;
    private ImageView cal;
    private TextView time;
    private Button send;

    private ImageView timeImage;
    private ImageView back;
    private DatePicker datePicker;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_route_add);

        back = findViewById(R.id.back);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        timeImage = findViewById(R.id.timeimage);
        from = findViewById(R.id.from);
        where = findViewById(R.id.where);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        send = findViewById(R.id.add);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();

        DatePickerBuilder builder1 = new DatePickerBuilder(this,new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendars) {
                int y = calendars.get(0).get(Calendar.YEAR);
                int m = calendars.get(0).get(Calendar.MONTH)+1;
                int d = calendars.get(0).get(Calendar.DAY_OF_MONTH);
                String strM = String.valueOf(m);
                String strD = String.valueOf(d);
                if(strM.length()==1){
                    strM = "0"+m;
                }
                if(strD.length()==1){
                    strD = "0"+d;
                }
                date.setText(strD+"/"+strM+"/"+y);
                Log.e("Date", date.getText().toString());
            }
        })
                .selectionColor(R.color.blue_gradient)
                .selectionLabelColor(R.color.white)
                .headerColor(R.color.blue_gradient)
                .date(calendar)
                .pickerType(CalendarView.ONE_DAY_PICKER);
        datePicker = builder1.build();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });



        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String d = i + ":" + (i1+1);
                time.setText(d);
            }
        };

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()) {
                    try {
                        collect_data();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(Driver_Trip_Add.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean check(){
        if(from.getText().toString().equals("")
                || where.getText().toString().equals("")
                || date.getText().toString().equals("")
                || time.getText().toString().equals("")
                || price.getText().toString().equals("")
                || quantity.getText().toString().equals("")
                )
            return false;
        return true;
    }

    public void collect_data() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("from", from.getText().toString());
        data.put("to", where.getText().toString());
        data.put("tripDate", date.getText().toString());
        data.put("tripTime", time.getText().toString());
        data.put("price", Integer.valueOf(price.getText().toString()));
        data.put("seats", Integer.valueOf(quantity.getText().toString()));
        data.put("userId", 2);
        data.put("note", "smth");

        Log.e("Driver_Tripp_add_send", data.toString());

        requestThread task = new requestThread();
        task.execute(String.valueOf(data.toString()));
    }

    public void showTimePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                Driver_Trip_Add.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mTimeSetListener,
                hour,
                minute,
                DateFormat.is24HourFormat(Driver_Trip_Add.this)
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        private String submitURL =
                "http://81.214.24.77:7777/api/trips?fbclid=IwAR3cDiiEDJ3nzUcpOD3LkaPxWvwi9232rpHlLNARny1eirQNu4cIYLy0his";

        @Override
        protected String doInBackground(String... strings) {

            return sendData(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Driver_Trip_Add.this);
            progressDialog.setMessage("Отправка данных...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
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
    }

    public boolean checkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) Driver_Trip_Add.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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