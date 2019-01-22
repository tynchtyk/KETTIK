package tynchtykbekkaldybaev.kettik;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.shape.ShapePath;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ParcelListAdapter extends RecyclerView.Adapter<ParcelListAdapter.ParcelViewHolder>{
    private ArrayList<Parcel> parcels;
    private LayoutInflater mInflater;
    private Context mContext;

    public ParcelListAdapter(Context context, ArrayList<Parcel> list) {
        mInflater = LayoutInflater.from(context);
        parcels = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ParcelListAdapter.ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.parcel_item, parent, false);
        return new ParcelViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelViewHolder holder, final int position) {
        Parcel item = parcels.get(position);
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
        return parcels.size();
    }


    public class ParcelViewHolder extends RecyclerView.ViewHolder{
        final ParcelListAdapter parcelAdapter;
        public final ImageView call;
        public final TextView from;
        public final TextView to;
        public final TextView date;

        public ParcelViewHolder(View itemView, ParcelListAdapter adapter) {

            super(itemView);

            call = (ImageView) itemView.findViewById(R.id.call);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);

            this.parcelAdapter = adapter;
        }
    }
}
