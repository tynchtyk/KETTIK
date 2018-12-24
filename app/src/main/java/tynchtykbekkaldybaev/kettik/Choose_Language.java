package tynchtykbekkaldybaev.kettik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import tynchtykbekkaldybaev.kettik.Description.Description;

/**
 * Created by tynchtykbekkaldybaev on 20/12/2018.
 */

public class Choose_Language extends AppCompatActivity {
    private TextView kg,ru,en;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language);


        kg = findViewById(R.id.kyrgyz);
        ru = findViewById(R.id.russian);
        en = findViewById(R.id.english);

        kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choose_Language.this, Description.class);
                startActivity(intent);
            }
        });

        ru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choose_Language.this, Description.class);
                startActivity(intent);
            }
        });
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choose_Language.this, Description.class);
                startActivity(intent);
            }
        });

    }

}
