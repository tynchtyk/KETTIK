package tynchtykbekkaldybaev.kettik;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_SearchDriver extends Fragment {
    private ArrayList<Driver> driver = new ArrayList<>();

    private RecyclerView driverRecyclerView;
    private DriverListAdapter driverAdapter;

    LinearLayout addition;
    boolean isUp;
    Button add_trip;
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
        driverRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewDrivers);

        addition = rootview.findViewById(R.id.addition_layout);
//        slideUp(addition);
        all_drivers = rootview.findViewById(R.id.all_drivers);

        all_drivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // slideDown(addition);
            }
        });
        isUp = false;



        driver.add(new Driver("Ош", "Баткен", "19/12/2018, 21:00", 1, 5));
        driver.add(new Driver("Бишкек", "Талас", "19/12/2018, 21:00", 4, 5));
        driver.add(new Driver("Ыссык-Кол", "Баткен", "19/12/2018, 21:00", 2, 5));
        driver.add(new Driver("Нарын", "Чуй", "19/12/2018, 21:00", 2, 5));

        //Log.d("FragSub", String.valueOf(tmp.menuLayout.getMenuEntries().size()));

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        driverRecyclerView.setLayoutManager(mLinearLayoutManager);

        driverAdapter = new DriverListAdapter(getActivity(), driver);
        driverRecyclerView.setAdapter(driverAdapter);
        return rootview;
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
        add_trip.setVisibility(View.GONE);

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

}