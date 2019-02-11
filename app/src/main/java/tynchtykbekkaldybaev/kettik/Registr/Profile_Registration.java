package tynchtykbekkaldybaev.kettik.Registr;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import tynchtykbekkaldybaev.kettik.AsteriskPasswordTransformationMethod;
import tynchtykbekkaldybaev.kettik.DoNothingTransformation;
import tynchtykbekkaldybaev.kettik.R;

public class Profile_Registration extends AppCompatActivity {
    EditText name;
    EditText surname;
    TextView birthdate;
    EditText password;
    EditText carnumber;
    EditText cartype;
    Spinner gender;

    Switch switcher;

    ImageView licencePic;
    CircleImageView profilePic;
    Boolean isPasswordShowing = false;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_registration);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        carnumber = findViewById(R.id.carnumber);
        cartype = findViewById(R.id.cartype);
        gender = findViewById(R.id.gender_selection);
        switcher = findViewById(R.id.switcher);

        save = findViewById(R.id.save);



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

        Calendar calendar = Calendar.getInstance();

        birthdate = (TextView) findViewById(R.id.birthday_text);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String d = "";
                if(i2 < 10)
                    d += "0";
                d += i2 + "/";
                if(i1+1 < 10)
                    d+="0";
                d+=(i1+1) + "/" + i;
                birthdate.setText(d);
            }
        };

        licencePic = (ImageView) findViewById(R.id.licence_pic);
        profilePic = (CircleImageView) findViewById(R.id.photo);
        password = (EditText)findViewById(R.id.password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()) {
                    Intent intent;
                    intent = new Intent(Profile_Registration.this, Phone_Registration.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("surname", surname.getText().toString());
                    intent.putExtra("carnumber", carnumber.getText().toString());
                    intent.putExtra("cartype", cartype.getText().toString());
                    intent.putExtra("birthdate", birthdate.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    intent.putExtra("gender", gender.getSelectedItem().toString());

                    startActivity(intent);
                    setResult(RESULT_OK, intent);

                    finish();
                }
                else {
                    Toast.makeText(Profile_Registration.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean check(){
        if(name.getText().toString().equals("")
                || surname.getText().toString().equals("")
                || carnumber.getText().toString().equals("")
                || cartype.getText().toString().equals("")
                || birthdate.getText().toString().equals("")
                || password.getText().toString().equals("")
                || gender.getSelectedItem().toString().equals("")
                )
            return false;
        return true;
    }

    public void showTimePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Profile_Registration.this,
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Registration.this);
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

    public void addProfilePic(View view) {
        final CharSequence[] items={"Запустить камеру","Выбрать из галереи", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Registration.this);
        builder.setTitle("Сменить фото профиля");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Запустить камеру")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 3);

                } else if (items[i].equals("Выбрать из галереи")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, 4);

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

            }
            else if(requestCode==1){

                Uri selectedImageUri = data.getData();
                licencePic.setImageURI(selectedImageUri);
                licencePic.setVisibility(View.VISIBLE);
            }

            else if(requestCode==3){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                profilePic.setImageBitmap(bmp);

            }else if(requestCode==4){

                Uri selectedImageUri = data.getData();
                profilePic.setImageURI(selectedImageUri);
            }

        }
    }
}
