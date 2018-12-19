package com.example.android.tmt;

import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {
    ImageButton mySummaryButton;
    ImageButton mAddButton;
    DatabaseHelper myDb;
//    TextView mError;
    Cursor cursor;
    ArrayList<Integer> hours=new ArrayList();;
    ArrayList<String> activities=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb=new DatabaseHelper(this);

        mySummaryButton = (ImageButton)findViewById(R.id.summary_imagebutton);
        mAddButton = (ImageButton)findViewById(R.id.add_image);
//        mError=(TextView)findViewById(R.id.errorShow);

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
    @Override
    protected void onStart()
    {
        super.onStart();
        cursor=myDb.getData();
        setupPieChart();
        PieChart cchart = (PieChart) findViewById(R.id.chart);
        int count = hours.size();
        if (count==0){
            cchart.setNoDataText("No Activity Recorded for the Day!");
            cchart.setNoDataTextColor(Color.BLACK);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        cursor=myDb.getData();
        setupPieChart();
        PieChart cchart = (PieChart) findViewById(R.id.chart);
        int count = hours.size();
        if (count==0){
            cchart.setNoDataText("No Activity Recorded for the Day!");
            cchart.setNoDataTextColor(Color.BLACK);
        }

    }
    private void setupPieChart() {

        try {
//
//            // Figure out the index of each column
            int CategoryColumnIndex = cursor.getColumnIndex(DatabaseHelper.Col_2 );

            int hourColumnIndex = cursor.getColumnIndex("Diff");
//            // Iterate through all the returned rows in the cursor

//            hours =Integer.valueOf(cursor.getString(hourColumnIndex));
            while (cursor.moveToNext()) {
                Log.v(TAG,"hi"+CategoryColumnIndex);
                if(activities.contains(cursor.getString(CategoryColumnIndex))==false){
                hours.add(cursor.getInt(hourColumnIndex));
                activities.add(cursor.getString(CategoryColumnIndex));}

                List<PieEntry> pieEntries = new ArrayList<>();
                for (int i = 0; i < hours.size(); i++) {
                    pieEntries.add(new PieEntry(hours.get(i), activities.get(i)));
                }
//                Log.v(TAG,""+hours);
                Log.v(TAG,""+activities);
                PieDataSet dataSet = new PieDataSet(pieEntries,"");
                final int[] MY_COLORS = {Color.rgb(192,0,0), Color.rgb(255,0,0), Color.rgb(255,192,0),
                        Color.rgb(127,127,127), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189)};
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for(int c: MY_COLORS) colors.add(c);

                dataSet.setColors(colors);
                dataSet.setValueTextSize(15f);
                dataSet.setValueFormatter(new IntValueFormatter());
                PieData data= new PieData(dataSet);



                PieChart chart = (PieChart) findViewById(R.id.chart);
                chart.getDescription().setEnabled(false);
                chart.setDrawHoleEnabled(false);
                chart.setData(data);
                chart.animateY(500);
                chart.invalidate();

            }
//        }

        } finally {
            cursor.close();
        }


    }
}
