package com.example.android.tmt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class SummaryActivity extends AppCompatActivity {
    private ImageButton weekSummaryButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        weekSummaryButton = (ImageButton)findViewById(R.id.week_Button);
        weekSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = SummaryActivity.this;
                Class destActivity = WeekActivity.class;
                Intent intent = new Intent(context, destActivity);
                startActivity(intent);

            }
        });
    }
}
