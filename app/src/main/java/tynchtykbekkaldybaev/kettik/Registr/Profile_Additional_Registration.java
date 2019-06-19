package tynchtykbekkaldybaev.kettik.Registr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import tynchtykbekkaldybaev.kettik.AsteriskPasswordTransformationMethod;
import tynchtykbekkaldybaev.kettik.DoNothingTransformation;
import tynchtykbekkaldybaev.kettik.R;

public class Profile_Additional_Registration extends AppCompatActivity {
    String  name;
    String surname;
    String birthdate;
    String password;
    String phoneNumber;
    EditText carnumber;
    EditText cartype;
    String gender;
    private String submitURL;

    Button save;
    ImageView licencePic;
    int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_registration);

        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
        name = userInfo.getString("name", "nan");
        surname = userInfo.getString("surname", "nan");
        birthdate = userInfo.getString("birthdate", "nan");
        password = userInfo.getString("password","nan");
        phoneNumber = userInfo.getString("phoneNumber","nan");
        gender = userInfo.getString("gender","nan");

        Intent intent = getIntent();
        Id = userInfo.getInt("Id",-1);
        submitURL = "http://kettik.kundoluk.kg/api/users";
        if(Id != -1)
            submitURL += "/" + String.valueOf(Id);

        carnumber = findViewById(R.id.carnumber);
        cartype = findViewById(R.id.cartype);

        save = findViewById(R.id.save);


        ImageButton accept = (ImageButton) findViewById(R.id.accept_button);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Profile_Additional_Registration.this, R.string.zapolnitevsepolya, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean check(){
        if(     carnumber.getText().toString().equals("")
                || cartype.getText().toString().equals("")

                )
            return false;
        return true;
    }


    public void addLicencePic(View view) {
        final CharSequence[] items={"Запустить камеру","Выбрать из галереи", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Additional_Registration.this);
        builder.setTitle(R.string.dobavitsnimokvodprav);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Запустить камеру")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);

                } else if (items[i].equals("Выбрать из галереи")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, 1);

                } else if (items[i].equals("Отмена")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==0){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                licencePic.setImageBitmap(bmp);
                licencePic.setVisibility(View.VISIBLE);

            }
            else if(requestCode==1){

                Uri selectedImageUri = data.getData();
                licencePic.setImageURI(selectedImageUri);
                licencePic.setVisibility(View.VISIBLE);
            }

        }
    }

    public void collect_data() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("surname", surname);
        data.put("birthDate", birthdate);
        data.put("gender", gender);
        data.put("profilePicture", "url");
        data.put("country", "kyrgyzstan");
        data.put("countryCode", "+996");
        data.put("phoneNumber", phoneNumber);
        data.put("password", password);
        data.put("driverFlag", true);
        data.put("vehicleModel", cartype.getText().toString());
        data.put("vehicleNumber", carnumber.getText().toString());
        data.put( "driverLicense","url");

        requestThread task = new requestThread();
        task.execute(String.valueOf(data.toString()));
    }

    public class requestThread extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;




        @Override
        protected String doInBackground(String... strings) {

            return sendData(strings[0]);
//            return getData();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Profile_Additional_Registration.this);
            progressDialog.setMessage("...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(result.equals("")) {
                Toast.makeText(Profile_Additional_Registration.this, R.string.proizowlaowibka,Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Profile_Additional_Registration.this.recreate();
                    }
                }, 1000);
            }
            else {

                try {
                    parce_data(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // this is expecting a response code to be sent from your server upon receiving the POST data

        }

        public String sendData(String data){
            if ( checkConnection() ) {
                String JsonResponse = null;
                Log.e("Submiturl", submitURL);
                Log.e("SENDPHONEREGISTR", data);
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
                    JsonResponse = sb.toString();

                    Log.e("PHONEREGISTROTVET", sb.toString());
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

        Log.e("Additionalresult", JsonResponse);


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
        setResult(RESULT_OK, intent);
        finish();
    }
    public boolean checkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) Profile_Additional_Registration.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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