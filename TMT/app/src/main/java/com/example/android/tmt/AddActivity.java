package com.example.android.tmt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Calendar;
import android.database.Cursor;

public class AddActivity extends AppCompatActivity {

//    for database
    private Spinner mCategory;
    private TextView mDate, mSTime, mETime;
    private RadioGroup mRadioGroup;
    private Button mAdd;
    private RadioButton radioButton;

//    For Time and Date Picker
    private ImageButton mDisplayDate;
    private ImageButton mDisplayTime;
    private ImageButton mDisplayTime1;
    private TextView mDisplayDate1;
    private TextView mDisplayTime2;
    private TextView mDisplayTime3;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button mShow;

    private int mRating=DatabaseHelper.RATING_HAPPY;
    DatabaseHelper myDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        myDb=new DatabaseHelper(this);




//        For database
        mCategory=(Spinner) findViewById(R.id.spinner1);
        mDate=(TextView) findViewById(R.id.date_val1);
        mSTime=(TextView) findViewById(R.id.s_val1);
        mETime=(TextView) findViewById(R.id.e_val);
        mRadioGroup=(RadioGroup) findViewById(R.id.rGroup);
        mAdd=(Button) findViewById(R.id.submit);
        AddData();

//        setCurrentTimeOnView(); For Time Picker

        mDisplayDate = (ImageButton) findViewById(R.id.date_val);
        mDisplayTime = (ImageButton) findViewById(R.id.time_button);
        mDisplayTime1 = (ImageButton) findViewById(R.id.time_button2);


        mDisplayDate1 = (TextView) findViewById(R.id.date_val1);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date=String.format("%d-%02d-%02d", year, month, day);
//                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

//                String date =  year + "-" +month  + "-" +day;
                mDisplayDate1.setText(date);
            }
        };

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
//                        strBuf.append("You select time is ");
                        strBuf.append(hour);
                        strBuf.append(":");
                        strBuf.append(minute);
                        mDisplayTime2 = (TextView) findViewById(R.id.s_val1);
//                        TextView timePickerValueTextView = (TextView)findViewById(R.id.timePickerValue);
                        mDisplayTime2.setText(strBuf.toString());
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);

//                timePickerDialog.setIcon(R.drawable.if_snowman);
//                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });

        mDisplayTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
//                        strBuf.append("You select time is ");
                        strBuf.append(hour);
                        strBuf.append(":");
                        strBuf.append(minute);
                        mDisplayTime3 = (TextView) findViewById(R.id.e_val);

                        mDisplayTime3.setText(strBuf.toString());
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);

//                timePickerDialog.setIcon(R.drawable.if_snowman);
//                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });

    }

    public void AddData(){
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDataEntered() == false) {
                    Toast.makeText(AddActivity.this, "Empty Details found. Please Check all the fields", Toast.LENGTH_LONG).show();


                }
                else{
                    int selectedId=mRadioGroup.getCheckedRadioButtonId();
                    radioButton=(RadioButton)findViewById(selectedId);
                    String rating=radioButton.getText().toString().trim();

                    if (rating.equals("Sad")){
                        mRating=DatabaseHelper.RATING_SAD;
                    }
                    else if(rating.equals("Happy")){
                        mRating=DatabaseHelper.RATING_HAPPY;
                    }
                    else  mRating=DatabaseHelper.RATING_OKEY;

                    if (validateTime())
                    {boolean isInserted = myDb.insertData(mCategory.getSelectedItem().toString(), mDate.getText().toString(), mSTime.getText().toString(), mETime.getText().toString(), String.valueOf(mRating));
                        if (isInserted = true){
                            Toast.makeText(AddActivity.this, "Your Activity is recorded", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        else
                            Toast.makeText(AddActivity.this, "Sorry! Your Activity is not recorded. Try Again", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(AddActivity.this, "Wrong time setting", Toast.LENGTH_LONG).show();
                    }


            }

            }
        });
    }
    public boolean validateTime() {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String stime = mSTime.getText().toString();
        String etime = mETime.getText().toString();

        Date mystime = null;
        Date myetime = null;
        try {
            mystime = formatter.parse(stime);
            myetime = formatter.parse(etime);
            if (myetime.after(mystime) && myetime.compareTo(mystime)!=0) return true;
            else return false;
        } catch (ParseException e) {
            Log.v("TIme", e.toString());
            return false;
        }
    }

    public boolean checkDataEntered(){
        if (mDate.getText().toString().isEmpty()){
            return false;
        }
        if (mSTime.getText().toString().isEmpty()){
            return false;
        }
        if (mETime.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }

}
