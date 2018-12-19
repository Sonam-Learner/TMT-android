package com.example.android.tmt;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class WeekActivity extends AppCompatActivity {
    ImageButton shareButton;
    Cursor cursor;
    DatabaseHelper myDb;
    ArrayList<Integer> hours=new ArrayList();
    ArrayList<String> activities=new ArrayList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        final PieChart chart = (PieChart) findViewById(R.id.chart);
        myDb=new DatabaseHelper(this);
        shareButton = (ImageButton)findViewById(R.id.share_Button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap =getBitmapFromView(chart);
                try {
                    File file = new File(getExternalCacheDir(),"WeekActivity.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.setType("image/jpg");
//                Uri uri = Uri.fromFile(new File(getFilesDir(), "add.jpg"));
//                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());
//                if (sharingIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(Intent.createChooser(sharingIntent, "Share image using"));
//                }

            }
        });
    }
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        cursor=myDb.getWeekData();
        setupPieChart();
        PieChart cchart = (PieChart) findViewById(R.id.chart);
        int count = hours.size();
        if (count==0){
            cchart.setNoDataText("No Activity Recorded for the Week!");
            cchart.setNoDataTextColor(Color.BLACK);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        cursor=myDb.getWeekData();
        setupPieChart();
        PieChart cchart = (PieChart) findViewById(R.id.chart);
        int count = hours.size();
        if (count==0){
            cchart.setNoDataText("No Activity Recorded for the Week!");
            cchart.setNoDataTextColor(Color.BLACK);
        }

    }
    private void setupPieChart() {
        Log.v(TAG,"hi week");

        try {
            Log.v(TAG,"hi week try");
//
//            // Figure out the index of each column
            int CategoryColumnIndex = cursor.getColumnIndex(DatabaseHelper.Col_2 );

            int hourColumnIndex = cursor.getColumnIndex("Diff");
//            // Iterate through all the returned rows in the cursor
            Log.v(TAG,"hi"+CategoryColumnIndex);
//            hours =Integer.valueOf(cursor.getString(hourColumnIndex));
            List<PieEntry> pieEntries = new ArrayList<>();
            while (cursor.moveToNext()) {
                Log.v(TAG, "hi insidemove" + CategoryColumnIndex + hourColumnIndex);
                if (activities.contains(cursor.getString(CategoryColumnIndex)) == false) {
                    hours.add(cursor.getInt(hourColumnIndex));
                    activities.add(cursor.getString(CategoryColumnIndex));
                }
            }

                for (int i = 0; i < hours.size(); i++) {
                    pieEntries.add(new PieEntry(hours.get(i), activities.get(i)));
                }

                Log.v(TAG,""+hours);
                Log.v(TAG,""+activities);
                PieDataSet dataSet = new PieDataSet(pieEntries,"---> PieChart Showing Week Activities");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setValueTextSize(15f);
                dataSet.setValueFormatter(new IntValueFormatter());
                PieData data= new PieData(dataSet);

                PieChart chart = (PieChart) findViewById(R.id.chart);
                chart.getDescription().setEnabled(false);
                chart.setDrawHoleEnabled(false);
                chart.setData(data);
                chart.animateY(1000);
                chart.invalidate();


//        }

        } finally {
            cursor.close();
        }


    }
}
