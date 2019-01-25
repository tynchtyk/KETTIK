package tynchtykbekkaldybaev.kettik;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.Calendar;
import java.util.List;

/**
 * Created by tynchtykbekkaldybaev on 24/01/2019.
 */

public class Passenger_Request_Add extends AppCompatActivity {
    private ImageView back;
    private TextView date;
    private ImageView cal;
    DatePicker datePicker;

    private TextView time;
    private ImageView timeImage;
    TimePickerDialog.OnTimeSetListener mTimeSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_request_add);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        date = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();

        DatePickerBuilder builder1 = new DatePickerBuilder(this,new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendars) {
                int y = calendars.get(0).get(Calendar.YEAR);
                int m = calendars.get(0).get(Calendar.MONTH)+1;
                int d = calendars.get(0).get(Calendar.DAY_OF_MONTH);
                String strM = String.valueOf(m);
                String strD = String.valueOf(d);
                if(strM.length()==1){
                    strM = "0"+m;
                }
                if(strD.length()==1){
                    strD = "0"+d;
                }
                date.setText(strD+"/"+strM+"/"+y);
            }
        })
                .selectionColor(R.color.blue_gradient)
                .selectionLabelColor(R.color.white)
                .headerColor(R.color.blue_gradient)
                .date(calendar)
                .pickerType(CalendarView.ONE_DAY_PICKER);
        datePicker = builder1.build();

        cal = findViewById(R.id.calendar);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        time = findViewById(R.id.time);
        timeImage = findViewById(R.id.timeimage);

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String d = i + ":" + (i1+1);
                time.setText(d);
            }
        };

        timeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
    }

    public void showTimePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                Passenger_Request_Add.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mTimeSetListener,
                hour,
                minute,
                DateFormat.is24HourFormat(Passenger_Request_Add.this)
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
