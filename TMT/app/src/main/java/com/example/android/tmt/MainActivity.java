package com.example.android.tmt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.Context;


public class MainActivity extends AppCompatActivity {

    ImageButton mySummaryButton;
    ImageButton mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySummaryButton = (ImageButton)findViewById(R.id.summary_imagebutton);
        mAddButton = (ImageButton)findViewById(R.id.add_image);

        mySummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Class destActivity = SummaryActivity.class;
                Intent intent = new Intent(context, destActivity);
                startActivity(intent);

            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Class destActivity = AddActivity.class;
                Intent intent = new Intent(context, destActivity);
                startActivity(intent);

            }
        });
    }
}
