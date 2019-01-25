package tynchtykbekkaldybaev.kettik;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.Calendar;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    TextView date;
    EditText password;
    ImageView licencePic;
    Boolean isPasswordShowing = false;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageButton accept = (ImageButton) findViewById(R.id.accept_button);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();

        date = (TextView) findViewById(R.id.birthday_text);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String d = i2 + "/" + (i1+1) + "/" + i;
                date.setText(d);
            }
        };

        licencePic = (ImageView) findViewById(R.id.licence_pic);
        password = (EditText)findViewById(R.id.password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    public void showTimePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                EditProfile.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showPassword(View view) {
        if (!isPasswordShowing) {
            password.setTransformationMethod(new DoNothingTransformation());
            isPasswordShowing = true;
        }
        else {
            password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            isPasswordShowing = false;
        }
    }

    public void addLicencePic(View view) {
        final CharSequence[] items={"Запустить камеру","Выбрать из галереи", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Добавить снимок вод. прав");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Запустить камеру")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);

                } else if (items[i].equals("Выбрать из галереи")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, 1);

                } else if (items[i].equals("Отмена")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==0){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                licencePic.setImageBitmap(bmp);
                licencePic.setVisibility(View.VISIBLE);

            }else if(requestCode==1){

                Uri selectedImageUri = data.getData();
                licencePic.setImageURI(selectedImageUri);
                licencePic.setVisibility(View.VISIBLE);
            }

        }
    }
}
