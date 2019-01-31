package tynchtykbekkaldybaev.kettik;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ArrayList<Driver_Info> driverInfoList;
    private LayoutInflater mInflater;
    private Context mContext;

    public DriverListAdapter(Context context, ArrayList<Driver> driverlist, ArrayList<Driver_Info> driverinfolist) {
        mInflater = LayoutInflater.from(context);
        driverList = driverlist;
        driverInfoList = driverinfolist;
        mContext = context;
    }


    @NonNull
    @Override
    public DriverListAdapter.DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.driver_item, parent, false);
        return new DriverViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final DriverViewHolder holder, final int position) {
        final Driver item = driverList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);
        holder.available_space.setText(item.free + "/" + item.maxspace + " свободно");

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Driver_Info item_info = driverInfoList.get(position);
                final Dialog myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.pop_up_information);

                TextView name_surname = myDialog.findViewById(R.id.driver_name);
                name_surname.setText(item_info.name + " " + item_info.surname);

                TextView driver_age_and_gender = myDialog.findViewById(R.id.driver_age_and_gender);
                driver_age_and_gender.setText(item_info.birthDate + ", " + item_info.gender);

                TextView price = myDialog.findViewById(R.id.price);
                price.setText(item_info.price + " сом");

                TextView autonumber = myDialog.findViewById(R.id.autonumber);
                autonumber.setText(item_info.vehicleNumber);

                TextView autotype = myDialog.findViewById(R.id.autotype);
                autotype.setText(item_info.vehicleModel);



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