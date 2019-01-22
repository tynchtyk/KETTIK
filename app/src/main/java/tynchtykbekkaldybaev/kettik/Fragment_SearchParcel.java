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
