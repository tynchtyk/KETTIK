package tynchtykbekkaldybaev.kettik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_SearchParcel extends Fragment {
    private ArrayList<Parcel> parcels = new ArrayList<>();

    private RecyclerView parcelRecyclerView;
    private ParcelListAdapter parcelAdapter;

    public Fragment_SearchParcel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("DRIVER", "STARTED");

        View rootview = inflater.inflate(R.layout.fragment_search_parcels, container, false);

        final MainActivity tmp = (MainActivity) getActivity();
        View cView = getLayoutInflater().inflate(R.layout.actionbar_header, null);

        TextView search_in_action_bar = (TextView)  cView.findViewById(R.id.search);
        search_in_action_bar.setText("Поиск маршрута");
        search_in_action_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(tmp, Pop_Up_Search_Parcel.class);
                startActivity(intent);

            }
        });
        tmp.actionBar.setCustomView(cView);

        parcelRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerviewParcels);

        parcels.add(new Parcel("Ош", "Баткен", "19/12/2018, 21:00"));
        parcels.add(new Parcel("Бишкек", "Талас", "19/12/2018, 21:00"));
        parcels.add(new Parcel("Ыссык-Кол", "Баткен", "19/12/2018, 21:00"));
        parcels.add(new Parcel("Нарын", "Чуй", "19/12/2018, 21:00"));

        //Log.d("FragSub", String.valueOf(tmp.menuLayout.getMenuEntries().size()));

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        parcelRecyclerView.setLayoutManager(mLinearLayoutManager);

        parcelAdapter = new ParcelListAdapter(getActivity(), parcels);
        parcelRecyclerView.setAdapter(parcelAdapter);
        return rootview;
    }
}
