package edu.skku.se3.beanhappy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class awaybeacon extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awaybeacon);

        Button btn = (Button) findViewById(R.id.beaconin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getin();
            }
        });
    }
        public void getin(){
            Intent intentToActivityusing = new Intent(mContext, usingactivity.class);
            startActivity(intentToActivityusing);
            //+자리 반납. 타이머 종료. activity 종료
        }
}
