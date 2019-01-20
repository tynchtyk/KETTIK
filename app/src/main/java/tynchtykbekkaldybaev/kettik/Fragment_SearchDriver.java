package tynchtykbekkaldybaev.kettik;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        View rootview = inflater.inflate(R.layout.fragment__search_driver, container, false);
        driverRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewDrivers);

        //Log.d("FragSub", String.valueOf(tmp.menuLayout.getMenuEntries().size()));
        driverAdapter = new DriverListAdapter(getActivity(), driver);
        driverRecyclerView.setAdapter(driverAdapter);
        return rootview;
    }

}
