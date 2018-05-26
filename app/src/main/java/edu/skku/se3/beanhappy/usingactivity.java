package edu.skku.se3.beanhappy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class usingactivity extends AppCompatActivity {
    private Context mContext = this;
    private  TextView txtView;
    private  Button btn_away;
    private  User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usingactivity);

        Button btn_main = (Button)findViewById(R.id.to_main);
        Button btn_return = (Button)findViewById(R.id.returnseat);
        Button btn_timeout = (Button)findViewById(R.id.timeout);
        btn_away = (Button)findViewById(R.id.away);
        txtView = findViewById(R.id.textView);
        user = new User("minki");
        user.setType(3);

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
                startActivity(intentToActivitymain);
            }
        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getout();
            }
        });
        btn_timeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitytimeout = new Intent(mContext, timeout.class);
                startActivity(intentToActivitytimeout);
            }
        });
        btn_away.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconout();
            }
        });
    }
    public void getout(){
        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
        startActivity(intentToActivitymain);
        //+자리 반납. 타이머 종료. activity 종료
    }
    public void beaconout(){
        //Intent intentToActivityaway = new Intent(mContext, awaybeacon.class);
        //startActivity(intentToActivityaway);
        if (user.getType() == 4){
            txtView.setText("사용중인 좌석은 //입니다");
            btn_away.setText("beacon out");
            user.setType(3);
        }
        else if(user.getType() == 3){
            txtView.setText("자리를 비우셨습니다.");
            btn_away.setText("beacon in");
            user.setType(4);
        }
        //+자리 반납. 타이머 종료. activity 종료
    }

}
