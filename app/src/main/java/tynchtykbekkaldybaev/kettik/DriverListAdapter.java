package tynchtykbekkaldybaev.kettik;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tynchtykbekkaldybaev on 20/01/2019.
 */

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.DriverViewHolder>{
    private ArrayList<Driver> driverList;
    private LayoutInflater mInflater;
    private Context mContext;

    public DriverListAdapter(Context context, ArrayList<Driver> list) {
        mInflater = LayoutInflater.from(context);
        driverList = list;
        mContext = context;
    }


    @NonNull
    @Override
    public DriverListAdapter.DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.driver_item, parent, false);
        return new DriverViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder,final int position) {
        Driver item = driverList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);
        holder.available_space.setText(item.free + "/" + item.maxspace + " свободно");

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });
    }
    @Override
    public int getItemCount() {
        return driverList.size();
    }


    public class DriverViewHolder extends RecyclerView.ViewHolder{
        final DriverListAdapter driverAdapter;
        public final ImageView call;
        public final TextView from;
        public final TextView to;
        public final TextView date;
        public final TextView available_space;

        public DriverViewHolder(View itemView, DriverListAdapter adapter) {

            super(itemView);

            call = (ImageView) itemView.findViewById(R.id.call);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            available_space = (TextView) itemView.findViewById(R.id.available_space);

            this.driverAdapter = adapter;
        }
    }
}