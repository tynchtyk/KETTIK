package tynchtykbekkaldybaev.kettik.Parcels;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tynchtykbekkaldybaev.kettik.R;

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
    public void onBindViewHolder(@NonNull final ParcelViewHolder holder, final int position) {
        Parcel item = parcels.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.pop_up_driver_information);
                ImageButton imageButton = (ImageButton) myDialog.findViewById(R.id.cancel);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

        holder.call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.call.setBackgroundResource(R.drawable.callbutton);
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.call.setBackgroundResource(R.drawable.call_button_unpressed);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.call.setBackgroundResource(R.drawable.call_button_unpressed);
                        break;
                }
                return false;
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
