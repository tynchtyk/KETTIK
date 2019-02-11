package tynchtykbekkaldybaev.kettik.IamPassenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.Calendar;
import java.util.List;

import tynchtykbekkaldybaev.kettik.R;

/**
 * Created by tynchtykbekkaldybaev on 24/01/2019.
 */

public class Pop_Up_Search_Driver extends AppCompatActivity {

    private TextView date;
    private EditText from, where;
    private ImageView back;
    private ImageView cal;
    private Button search;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_driver_search);

        back = findViewById(R.id.back);
        from = findViewById(R.id.from);
        where = findViewById(R.id.where);

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
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(from.getText().toString() != null)
                intent.putExtra("from", from.getText().toString());
                if(where.getText().toString() != null)
                intent.putExtra("to", where.getText().toString());
                if(date.getText().toString() != null)
                intent.putExtra("tripDate", date.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}
