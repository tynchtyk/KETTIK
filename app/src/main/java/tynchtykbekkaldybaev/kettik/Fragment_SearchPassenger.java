package tynchtykbekkaldybaev.kettik;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Fragment_SearchPassenger extends Fragment {
    private ArrayList<Passenger> passengers = new ArrayList<>();

    private RecyclerView passengerRecyclerView;
    private PassengerListAdapter passengerAdapter;

    public Fragment_SearchPassenger() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");

        View rootview = inflater.inflate(R.layout.fragment_search_passenger, container, false);
        passengerRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewDrivers);

        passengers.add(new Passenger("Нарын", "Чуй", "19/12/2018, 21:00"));
        passengers.add(new Passenger("Ыссык-Кол", "Баткен", "19/12/2018, 21:00"));
        passengers.add(new Passenger("Бишкек", "Талас", "19/12/2018, 21:00"));
        passengers.add(new Passenger("Ош", "Баткен", "19/12/2018, 21:00"));

        //Log.d("FragSub", String.valueOf(tmp.menuLayout.getMenuEntries().size()));

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        passengerRecyclerView.setLayoutManager(mLinearLayoutManager);

        passengerAdapter = new PassengerListAdapter(getActivity(), passengers);
        passengerRecyclerView.setAdapter(passengerAdapter);
        return rootview;
    }
}
