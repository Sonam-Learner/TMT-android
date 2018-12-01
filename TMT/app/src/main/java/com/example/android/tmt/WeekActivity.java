package com.example.android.tmt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import java.io.File;

public class WeekActivity extends AppCompatActivity {
    ImageButton shareButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        shareButton = (ImageButton)findViewById(R.id.share_Button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/jpg");
                Uri uri = Uri.fromFile(new File(getFilesDir(), "add.jpg"));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());
                if (sharingIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                }

            }
        });
    }
}
