package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Showrule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showrule);

        Button button = (Button) findViewById(R.id.map_OKBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToChat = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intentToChat);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToChat = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intentToChat);
        finish();
    }
}