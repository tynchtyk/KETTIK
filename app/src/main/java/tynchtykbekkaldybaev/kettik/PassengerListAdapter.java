package tynchtykbekkaldybaev.kettik;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.PassengerViewHolder>{
    private ArrayList<Passenger> passengerList;
    private LayoutInflater mInflater;
    private Context mContext;

    public PassengerListAdapter(Context context, ArrayList<Passenger> list) {
        mInflater = LayoutInflater.from(context);
        passengerList = list;
        mContext = context;
    }


    @NonNull
    @Override
    public PassengerListAdapter.PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.passenger_item, parent, false);
        return new PassengerViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerListAdapter.PassengerViewHolder holder, final int position) {
        Passenger item = passengerList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }


    public class PassengerViewHolder extends RecyclerView.ViewHolder{
        final PassengerListAdapter passengerAdapter;
        public final ImageView call;
        public final TextView from;
        public final TextView to;
        public final TextView date;

        public PassengerViewHolder(View itemView, PassengerListAdapter adapter) {

            super(itemView);

            call = (ImageView) itemView.findViewById(R.id.call);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);

            this.passengerAdapter = adapter;
        }
    }
}
