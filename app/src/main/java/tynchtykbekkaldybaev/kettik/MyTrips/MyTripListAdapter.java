package tynchtykbekkaldybaev.kettik.MyTrips;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tynchtykbekkaldybaev.kettik.IamPassenger.Driver;
import tynchtykbekkaldybaev.kettik.IamPassenger.Driver_Info;
import tynchtykbekkaldybaev.kettik.R;

/**
 * Created by tynchtykbekkaldybaev on 03/03/2019.
 */

public class MyTripListAdapter extends RecyclerView.Adapter<MyTripListAdapter.MyTripViewHolder> {
    private ArrayList<MyTrip> mytripList;
    private ArrayList<MyTrip_Info> mytripInfoList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyTripListAdapter(Context context, ArrayList<MyTrip> mytriplist, ArrayList<MyTrip_Info> mytripinfolist) {
        mInflater = LayoutInflater.from(context);
        mytripList = mytriplist;
        mytripInfoList = mytripinfolist;
        mContext = context;
    }


    @NonNull
    @Override
    public MyTripListAdapter.MyTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.mytrip_item, parent, false);
        return new MyTripListAdapter.MyTripViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyTripListAdapter.MyTripViewHolder holder, final int position) {
        final MyTrip item = mytripList.get(position);
        final MyTrip_Info item_info = mytripInfoList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);


    }
    @Override
    public int getItemCount() {
        return mytripList.size();
    }


    public class MyTripViewHolder extends RecyclerView.ViewHolder{
        final MyTripListAdapter mytripAdapter;
        public final TextView from;
        public final TextView to;
        public final TextView date;


        public MyTripViewHolder(View itemView, MyTripListAdapter adapter) {

            super(itemView);

            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            this.mytripAdapter = adapter;
        }
    }

}
