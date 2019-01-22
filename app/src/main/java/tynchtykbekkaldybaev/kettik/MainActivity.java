package tynchtykbekkaldybaev.kettik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import static android.widget.Toast.*;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setTitle("Поиск водителя");


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
                Fragment_SearchDriver fragment_searchDriver = new Fragment_SearchDriver();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_searchDriver).commit();
                MenuItem menuItem = navigationView.getMenu().getItem(1);
                navigationView.setCheckedItem(menuItem);
                updateNavigationButtons(DRIVERS_FRAGMENT);
            }
        });

        passengerButton = (Button) findViewById(R.id.passengers);
        passengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment_SearchPassenger fragment_searchPassenger = new Fragment_SearchPassenger();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_searchPassenger).commit();
                MenuItem menuItem = navigationView.getMenu().getItem(2);
                navigationView.setCheckedItem(menuItem);
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
                navigationView.setCheckedItem(menuItem);
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
        MenuCompat.setGroupDividerEnabled(menu, true);
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
            case R.id.my_trips:
                fragmentClass = BlankFragment.class;

                break;
            case R.id.drivers:
                fragmentClass = Fragment_SearchDriver.class;
                updateNavigationButtons(DRIVERS_FRAGMENT);
                break;
            case R.id.passengers:
                fragmentClass = Fragment_SearchPassenger.class;
                updateNavigationButtons(PASSENGERS_FRAGMENT);
                break;
            case R.id.parcels:
                fragmentClass = Fragment_SearchParcel.class;
                updateNavigationButtons(PARCELS_FRAGMENT);
                break;
            case R.id.settings:
                fragmentClass = BlankFragment.class;
                break;
            case R.id.help:
                fragmentClass = BlankFragment.class;
                break;
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
}
