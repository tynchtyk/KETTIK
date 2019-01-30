package tynchtykbekkaldybaev.kettik;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LanguageSelection extends AppCompatActivity {

    ImageView kyrgyz, russian, english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        kyrgyz = (ImageView) findViewById(R.id.kyrgyz_check);
        russian = (ImageView) findViewById(R.id.russian_check);
        english = (ImageView) findViewById(R.id.english_check);
    }

    public void Russian(View view) {
        russian.setVisibility(View.VISIBLE);
        kyrgyz.setVisibility(View.INVISIBLE);
        english.setVisibility(View.INVISIBLE);
    }

    public void Kyrgyz(View view) {
        kyrgyz.setVisibility(View.VISIBLE);
        russian.setVisibility(View.INVISIBLE);
        english.setVisibility(View.INVISIBLE);
    }

    public void English(View view) {
        english.setVisibility(View.VISIBLE);
        russian.setVisibility(View.INVISIBLE);
        kyrgyz.setVisibility(View.INVISIBLE);
    }

    public void Back(View view) {
        finish();
    }
}
