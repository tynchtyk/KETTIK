package tynchtykbekkaldybaev.kettik;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

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


    }
    @Override
    public int getItemCount() {
        return driverList.size();
    }


    public class DriverViewHolder extends RecyclerView.ViewHolder{
        final DriverListAdapter driverAdapter;

        public DriverViewHolder(View itemView, DriverListAdapter adapter) {

            super(itemView);

            this.driverAdapter = adapter;
        }
    }
}
