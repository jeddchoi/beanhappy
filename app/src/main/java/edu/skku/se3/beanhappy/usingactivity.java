package edu.skku.se3.beanhappy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class usingactivity extends AppCompatActivity {
    private Context mContext = this;
    private Context mContext1 = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usingactivity);

        Button btn = (Button)findViewById(R.id.to_main);
        Button btn1 = (Button)findViewById(R.id.returnseat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
                startActivity(intentToActivitymain);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getout();
            }
        });
    }
    public void getout(){
        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
        startActivity(intentToActivitymain);
        //+자리 반납. 타이머 종료. activity 종료
    }
}
