package tynchtykbekkaldybaev.kettik;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_SearchDriver extends Fragment {
    private static ArrayList<Driver> driver = new ArrayList<>();

    private RecyclerView driverRecyclerView;
    private DriverListAdapter driverAdapter;

    public Fragment_SearchDriver() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");

        View rootview = inflater.inflate(R.layout.fragment__search_driver, container, false);
        driverRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewDrivers);

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

}