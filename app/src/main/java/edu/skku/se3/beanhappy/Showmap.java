package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Showmap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmap);

        Button button = (Button) findViewById(R.id.map_OKBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToOK = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intentToOK);
                finish();
            }
        });
    }
}