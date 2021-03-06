package tynchtykbekkaldybaev.kettik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import tynchtykbekkaldybaev.kettik.Description.Description;
import tynchtykbekkaldybaev.kettik.IamPassenger.Fragment_SearchDriver;
import tynchtykbekkaldybaev.kettik.MyTrips.My_Trips;
import tynchtykbekkaldybaev.kettik.Parcels.Fragment_SearchParcel;
import tynchtykbekkaldybaev.kettik.IamDriver.Fragment_SearchPassenger;
import tynchtykbekkaldybaev.kettik.Registr.Confirmation;
import tynchtykbekkaldybaev.kettik.Registr.EditProfile;
import tynchtykbekkaldybaev.kettik.Registr.Login;
import tynchtykbekkaldybaev.kettik.Registr.Profile_Registration;

public class MainActivity extends AppCompatActivity{
    final int DRIVERS_FRAGMENT = 0;
    final int PASSENGERS_FRAGMENT = 1;
    final int PARCELS_FRAGMENT = 2;

    private DrawerLayout mDrawerLayout;
    public NavigationView navigationView;
    public ActionBar actionBar = null;
    public SearchView searchView;

    Menu globalMenu;
    Button driversButton;
    Button passengerButton;
    Button parcelsButton;
    Button login;
    ImageButton editButton;
    TextView userName, userProf, userGuest;
    CardView avatar_man, avatar_woman;

    public TextView toolbarTitle;

    public int Id;
    public boolean driverflag;
    Boolean islogin;
    SharedPreferences userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        set_locale();

        set_View();



        if(islogin == false) {
            Intent intent = new Intent(MainActivity.this, Choose_Language.class);
            startActivityForResult(intent, 5);

        }
        else{
            boolean activeFlag = userInfo.getBoolean("activeFlag", false);
                String name = userInfo.getString("name", null);
                String surname = userInfo.getString("surname", null);
                userName.setText(name + " " + surname);
                userProf.setText(R.string.voditel);

                set_login();
        }



    }
    public void set_locale(){
        userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
        String lan = userInfo.getString("language", "ru");
        islogin = userInfo.getBoolean("islogin", false);

        // language
        Locale myLocale = new Locale(lan);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //id
        Id = userInfo.getInt("Id", -1);
        driverflag = userInfo.getBoolean("driverFlag", false);
        Log.e("ISLOGIN", String.valueOf(islogin));

    }

    public void set_View(){
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        toolbarTitle = findViewById(R.id.toolbarTitle);

        View headView = navigationView.getHeaderView(0);
        editButton = (ImageButton) headView.findViewById(R.id.edit_button);
        login = headView.findViewById(R.id.signup);
        userName = headView.findViewById(R.id.userName);
        userGuest = headView.findViewById(R.id.guest);
        userProf = headView.findViewById(R.id.userProf);

        String s = userInfo.getString("gender", "");
        Log.e("GENDER", s);
        avatar_man = headView.findViewById(R.id.card_avatar_man);
        avatar_woman = headView.findViewById(R.id.card_avatar_woman);

        if(s.equals("Мужской")) {
            avatar_man.setVisibility(View.VISIBLE);
            avatar_woman.setVisibility(View.INVISIBLE);
        }
        else {
            avatar_man.setVisibility(View.INVISIBLE);
            avatar_woman.setVisibility(View.VISIBLE);

        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditProfile.class);
                intent.putExtra("Id", Id);
                startActivityForResult(intent, 3);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, Login.class);
                startActivityForResult(intent,1);
            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        selectFragment(menuItem);

                        return true;
                    }
                });

        driversButton = (Button) findViewById(R.id.drivers);
        driversButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment_SearchPassenger fragment_searchPassenger = new Fragment_SearchPassenger();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_searchPassenger).commit();
                MenuItem menuItem = navigationView.getMenu().getItem(2);
                navigationView.setCheckedItem(2);
                updateNavigationButtons(DRIVERS_FRAGMENT);
            }
        });

        passengerButton = (Button) findViewById(R.id.passengers);
        passengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment_SearchDriver fragment_searchDriver = new Fragment_SearchDriver();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_searchDriver).commit();
                MenuItem menuItem = navigationView.getMenu().getItem(1);
                navigationView.setCheckedItem(1);
                updateNavigationButtons(PASSENGERS_FRAGMENT);
            }
        });

        parcelsButton = (Button) findViewById(R.id.parcels);
        parcelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment_SearchParcel fragment_searchParcel = new Fragment_SearchParcel();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_searchParcel).commit();
                MenuItem menuItem = navigationView.getMenu().getItem(3);
                navigationView.setCheckedItem(3);
                updateNavigationButtons(PARCELS_FRAGMENT);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.action_bar_menu, menu);
       // MenuCompat.setGroupDividerEnabled(menu, true);
        globalMenu = menu;

///        MenuInflater menuInflater = getMenuInflater();
   //     menuInflater.inflate(R.menu.action_bar_menu, menu);

  //      MenuItem menuItem =  menu.findItem(R.id.m_search);
//        View view = MenuItemCompat.getActionView(menuItem);






        return super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.drawer_view, menu);


    }


    public void selectFragment(MenuItem menuItem) {
        android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.drivers:
                fragmentClass = Fragment_SearchPassenger.class;
                updateNavigationButtons(DRIVERS_FRAGMENT);
                break;
            case R.id.passengers:
                fragmentClass = Fragment_SearchDriver.class;
                updateNavigationButtons(PASSENGERS_FRAGMENT);

                break;
            case R.id.parcels:
                fragmentClass = Fragment_SearchParcel.class;
                updateNavigationButtons(PARCELS_FRAGMENT);
                break;
            case R.id.my_trips:
                Intent intent = new Intent(MainActivity.this, My_Trips.class);
                if(Id == -1) {
                    Toast.makeText(MainActivity.this, R.string.chtobyuvidet, Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("Id", Id);
                    startActivity(intent);
                }
                return;
            case R.id.settings:
                intent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(intent,2);
                return;
            case R.id.help:
                intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
                return ;
            case R.id.log_out:
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                userInfo.edit().clear().commit();

                mDrawerLayout.closeDrawers();
                final ProgressDialog progressDialog = new ProgressDialog(this,
                        R.style.Theme_AppCompat_Light_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("...");
                progressDialog.show();
                userName = null;
                userProf = null;
                clearBackStack();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        MainActivity.this.recreate();
                    }
                }, 2000);
                return ;
            default:
                fragmentClass = BlankFragment.class;
                break;
        }

        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            // Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        menuItem.setChecked(true);

        mDrawerLayout.closeDrawers();
    }
    public void clearBackStack() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        //Toast.makeText(MainActivity.this, "Number of fragments in the stack: " + fm.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    void updateNavigationButtons (int fragment) {
        Button button;
        ImageView imageView;

        switch (fragment) {
            case DRIVERS_FRAGMENT:
                button = (Button) findViewById(R.id.drivers);
                imageView = (ImageView) findViewById(R.id.drivers_image);
                imageView.setImageResource(R.drawable.drivericonpressed);
                button.setTextColor(Color.parseColor("#4CA2CD"));

                button = (Button) findViewById(R.id.passengers);
                imageView = (ImageView) findViewById(R.id.passengers_image);
                imageView.setImageResource(R.drawable.passengersicon);
                button.setTextColor(Color.parseColor("#378243"));

                button = (Button) findViewById(R.id.parcels);
                imageView = (ImageView) findViewById(R.id.parcels_image);
                imageView.setImageResource(R.drawable.parcel);
                button.setTextColor(Color.parseColor("#378243"));
                break;
            case PASSENGERS_FRAGMENT:
                button = (Button) findViewById(R.id.drivers);
                imageView = (ImageView) findViewById(R.id.drivers_image);
                imageView.setImageResource(R.drawable.carfront);
                button.setTextColor(Color.parseColor("#378243"));

                button = (Button) findViewById(R.id.passengers);
                imageView = (ImageView) findViewById(R.id.passengers_image);
                imageView.setImageResource(R.drawable.passengersiconpressed);
                button.setTextColor(Color.parseColor("#4CA2CD"));

                button = (Button) findViewById(R.id.parcels);
                imageView = (ImageView) findViewById(R.id.parcels_image);
                imageView.setImageResource(R.drawable.parcel);
                button.setTextColor(Color.parseColor("#378243"));
                break;
            case PARCELS_FRAGMENT:
                button = (Button) findViewById(R.id.drivers);
                imageView = (ImageView) findViewById(R.id.drivers_image);
                imageView.setImageResource(R.drawable.carfront);
                button.setTextColor(Color.parseColor("#378243"));

                button = (Button) findViewById(R.id.passengers);
                imageView = (ImageView) findViewById(R.id.passengers_image);
                imageView.setImageResource(R.drawable.passengersicon);
                button.setTextColor(Color.parseColor("#378243"));

                button = (Button) findViewById(R.id.parcels);
                imageView = (ImageView) findViewById(R.id.parcels_image);
                imageView.setImageResource(R.drawable.parceliconpressed);
                button.setTextColor(Color.parseColor("#4CA2CD"));
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Login intent
            if (resultCode == RESULT_OK) {
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                String name = userInfo.getString("name", null);
                String surname = userInfo.getString("surname", null);
                //country
                //countrycode

                String vehicleModel = userInfo.getString("cartype","nan");
                String vehicleNumber = userInfo.getString("carnumber","nan");

                Id = userInfo.getInt("Id", -1);
                userName.setText(name + " " + surname);
                userProf.setText("Водитель");
                driverflag = userInfo.getBoolean("driverFlag",false);
                String s = userInfo.getString("gender", "");
                Log.e("GENDER", s);

                if(s.equals("Мужской")) {
                    avatar_man.setVisibility(View.VISIBLE);
                    avatar_woman.setVisibility(View.INVISIBLE);
                }
                else {
                    avatar_man.setVisibility(View.INVISIBLE);
                    avatar_woman.setVisibility(View.VISIBLE);

                }
                Log.e("LOGINRESULT", name + " " + surname + " " + vehicleModel + " " + vehicleNumber + " " + String.valueOf(driverflag));


                set_login();
            }
            else if(resultCode == RESULT_FIRST_USER){
                Intent intent = new Intent(MainActivity.this, Profile_Registration.class);
                startActivityForResult(intent,4);

            }
            else if(resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(MainActivity.this, Description.class);
                startActivityForResult(intent, 5);
            }
            else if(resultCode == 2){ //activeflag = false
                Intent intent = new Intent(MainActivity.this, Confirmation.class);
                startActivityForResult(intent, 6);
            }
        }
        if (requestCode == 2) { //Settings
            if (resultCode == RESULT_OK) {
                final ProgressDialog progressDialog = new ProgressDialog(this,
                        R.style.Theme_AppCompat_Light_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("...");
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        MainActivity.this.recreate();
                    }
                }, 2000);
            }
        }
        if (requestCode == 3) { // Edit intent
            if (resultCode == RESULT_OK) {
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                String name = userInfo.getString("name", null);
                String surname = userInfo.getString("surname", null);
                Id = userInfo.getInt("Id", -1);
                Log.e("EDITSHAREDRESULT", name + " " + surname);
                userName.setText(name + " " + surname);
                userProf.setText("Водитель");
                driverflag = userInfo.getBoolean("driverFlag",false);

                String s = userInfo.getString("gender", "");
                Log.e("GENDER", s);

                if(s.equals("Мужской")) {
                    avatar_man.setVisibility(View.VISIBLE);
                    avatar_woman.setVisibility(View.INVISIBLE);
                }
                else {
                    avatar_man.setVisibility(View.INVISIBLE);
                    avatar_woman.setVisibility(View.VISIBLE);

                }

                set_login();
            }
        }

        if (requestCode == 4) { // Registration intent
            if (resultCode == RESULT_OK) {
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                String name = userInfo.getString("name", null);
                String surname = userInfo.getString("surname", null);
                Id = userInfo.getInt("Id", -1);
                Log.e("REGISTRSHAREDRESULT", name + " " + surname);

                if(name != null && surname != null) {
                    userName.setText(name + " " + surname);
                    userProf.setText("Водитель");
                    set_login();
                    userInfo.edit()
                            .putBoolean("islogin", true)
                            .commit();

                }
                driverflag = userInfo.getBoolean("driverFlag",false);

            }
            else if(resultCode == RESULT_CANCELED) {
                Intent intent;
                intent = new Intent(MainActivity.this, Login.class);
                startActivityForResult(intent,1);
            }
        }
        if (requestCode == 5) { // Description
            set_View();
            if (resultCode == RESULT_OK) {
                Intent intent;
                intent = new Intent(MainActivity.this, Login.class);
                startActivityForResult(intent,1);
            }

        }
        if (requestCode == 6) { // Confirmation
            if (resultCode == RESULT_OK) {
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
                String name = userInfo.getString("name", null);
                String surname = userInfo.getString("surname", null);
                Id = userInfo.getInt("Id", -1);
                Log.e("REGISTRSHAREDRESULT", name + " " + surname);

                if(name != null && surname != null) {
                    userName.setText(name + " " + surname);
                    userProf.setText("Водитель");
                    set_login();
                    userInfo.edit()
                            .putBoolean("islogin", true)
                            .commit();

                }
                driverflag = userInfo.getBoolean("driverFlag",false);
            }

        }

    }
    public void set_unlogined(){
        editButton.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        userProf.setVisibility(View.GONE);
        userGuest.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
    }
    public void set_login(){
        userGuest.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
        userProf.setVisibility(View.VISIBLE);

        MenuItem menuItem = navigationView.getMenu().getItem(1);
        navigationView.setCheckedItem(1);
        updateNavigationButtons(PASSENGERS_FRAGMENT);

        Fragment_SearchDriver fragment_searchDriver = new Fragment_SearchDriver();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment_searchDriver);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
