package com.example.android.tmt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class PreviousActvity extends AppCompatActivity {
    private ImageButton weekSummaryButton;
    private ImageButton nextSummaryButton;
    Cursor cursor;
    DatabaseHelper myDb;
    BarChart barChart;
    ArrayList<String> str = new ArrayList<String>();
    ArrayList<Integer> rating=new ArrayList<>();
    ArrayList<String> start=new ArrayList<>();
    ArrayList<String> end=new ArrayList<>();
    ArrayList<String> categories=new ArrayList<>();
    ArrayList<String> TimeDiff=new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        myDb=new DatabaseHelper(this);
        weekSummaryButton = (ImageButton)findViewById(R.id.week_Button);
        nextSummaryButton = (ImageButton)findViewById(R.id.next_Button);
        weekSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = PreviousActvity.this;
                Class destActivity = WeekActivity.class;
                Intent intent = new Intent(context, destActivity);
                startActivity(intent);

            }
        });
        nextSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = PreviousActvity.this;
                Class destActivity = SummaryActivity.class;
                Intent intent = new Intent(context, destActivity);
                startActivity(intent);

            }
        });
        str.add("");
        str.add("Sad");
        str.add("Okey");
        str.add("Happy");
        barChart = (BarChart) findViewById(R.id.bChart);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        cursor=myDb.getPrevData();
        getCursor();
        int count = rating.size();
        if (count==0){
            barChart.setNoDataText("No Activity Recorded for the Day!");
            barChart.setNoDataTextColor(Color.BLACK);
        }
        else setData(count);
    }
    private void setData(int count)
    {
        ArrayList<BarEntry> entries=new ArrayList<>();

        for (int i=0;i<count;i++){
//            float value=(float)(Math.random()*100);
            entries.add(new BarEntry(i,rating.get(i)));
            TimeDiff.add(start.get(i)+" to "+end.get(i)+"("+categories.get(i)+")");
        }

        BarDataSet set=new BarDataSet(entries,"");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);


        BarData data=new BarData(set);



        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setLabelCount(4, true);

        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(3f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(2f);
        barChart.setData(data);


        String [] arrayTime = TimeDiff.toArray(new String[TimeDiff.size()]);
        String [] arrayRating = str.toArray(new String[str.size()]);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(arrayTime));
        leftAxis.setValueFormatter(new MyYAxisValueFormatter(arrayRating));


        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(arrayTime));
        barChart.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(arrayRating));
        barChart.getXAxis().setLabelRotationAngle(-52);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getLegend().setEnabled(false);
//        barChart.setFitBars(true);
        rightAxis.setEnabled(false);
        barChart.invalidate();
        barChart.animateY(500);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] myValues;

        public MyXAxisValueFormatter(String[] values) {
            this.myValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return myValues[(int) value];
        }

    }
    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyYAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

    }
    public void getCursor(){
        try{
            int RatingColumnIndex = cursor.getColumnIndex(DatabaseHelper.Col_6 );
            int CategoryColumnIndex = cursor.getColumnIndex(DatabaseHelper.Col_2 );
            int StartColumnIndex = cursor.getColumnIndex(DatabaseHelper.Col_4 );
            int EndColumnIndex = cursor.getColumnIndex(DatabaseHelper.Col_5 );


            while (cursor.moveToNext()){
                if(categories.contains(cursor.getString(CategoryColumnIndex))==false){
                    rating.add(Integer.parseInt(cursor.getString(RatingColumnIndex)));
                    categories.add(cursor.getString(CategoryColumnIndex));
                    start.add(cursor.getString(StartColumnIndex));
                    end.add(cursor.getString(EndColumnIndex));}

            }

        }
        finally {
            cursor.close();
        }
    }
}

