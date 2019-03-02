package tynchtykbekkaldybaev.kettik.IamDriver;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import tynchtykbekkaldybaev.kettik.R;

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.PassengerViewHolder> {
    private ArrayList<Passenger> passengerList;
    private ArrayList<Passenger_Info> passengerInfoList;
    private LayoutInflater mInflater;
    private Context mContext;

    public PassengerListAdapter(Context context, ArrayList<Passenger> list, ArrayList<Passenger_Info> passengerinfolist) {
        mInflater = LayoutInflater.from(context);
        passengerList = list;
        passengerInfoList = passengerinfolist;
        mContext = context;
    }


    @NonNull
    @Override
    public PassengerListAdapter.PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.passenger_item, parent, false);
        return new PassengerViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final PassengerListAdapter.PassengerViewHolder holder, final int position) {
        final Passenger item = passengerList.get(position);
        final Passenger_Info item_info = passengerInfoList.get(position);
        holder.itemView.setTag(item);

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);
        holder.passengers_quantity.setText(item.passengers + " человек");
        holder.price.setText(item_info.price);
        holder.currency.setText("сом");

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.pop_up_passenger_information);

                TextView name_surname = myDialog.findViewById(R.id.driver_name);
                name_surname.setText(item_info.name + " " + item_info.surname);

                TextView driver_age_and_gender = myDialog.findViewById(R.id.driver_age_and_gender);
                driver_age_and_gender.setText(item_info.birthDate + ", " + item_info.gender);

                TextView price = myDialog.findViewById(R.id.price);
                price.setText(item_info.price + " сом");

                Button call = myDialog.findViewById(R.id.call);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = item_info.phoneNumber;
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        view.getContext().startActivity(intent);
                    }
                });

                Button sms = myDialog.findViewById(R.id.sms);
                sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = item_info.phoneNumber;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phone +"&text="+"text"));
                        if (intent.resolveActivity(view.getContext().getPackageManager()) == null) {
                            Toast.makeText(view.getContext(), "Whatsapp not installed.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        view.getContext().startActivity(intent);
                    }
                });




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
        return passengerList.size();
    }


    public class PassengerViewHolder extends RecyclerView.ViewHolder{
        final PassengerListAdapter passengerAdapter;
        public final ImageView call;
        public final TextView from;
        public final TextView to;
        public final TextView date;
        public final TextView passengers_quantity;
        public final TextView price;
        public final TextView currency;

        public PassengerViewHolder(View itemView, PassengerListAdapter adapter) {

            super(itemView);

            call = (ImageView) itemView.findViewById(R.id.call);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            passengers_quantity = (TextView) itemView.findViewById(R.id.passenger_quantity);
            price = (TextView) itemView.findViewById(R.id.price);
            currency = (TextView) itemView.findViewById(R.id.currency);

            this.passengerAdapter = adapter;
        }
    }
}
