package edu.skku.se3.beanhappy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class usingactivity extends Activity {
    private int limit_usingtime = 40;
    private int limit_leavingtime = 10;
    private Context mContext = this;
    private  TextView txtView;
    private  Button btn_away;
    private  User user;
    private  Button btn_extend;
    int pauseprogress = 0;
    int myProgress = 0;
    ProgressBar progressBarView;
    //Button btn_start;
    TextView tv_time;
    int progress;
    int additiontime = 0;
    CountDownTimer countDownTimer;
    int endTime = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usingactivity);

        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        //btn_start = (Button)findViewById(R.id.btn_start);
        tv_time= (TextView)findViewById(R.id.tv_timer);


        Button btn_main = (Button)findViewById(R.id.to_main);
        Button btn_return = (Button)findViewById(R.id.returnseat);
        Button btn_timeout = (Button)findViewById(R.id.timeout);
        btn_extend = (Button)findViewById(R.id.btn_extend);
        btn_away = (Button)findViewById(R.id.away);
        txtView = findViewById(R.id.textView);
        user = new User("minki");
        user.setType(3);

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        btn_extend.setVisibility(View.INVISIBLE);
        fn_countdown(0);
//        btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fn_countdown();
//            }
//        });


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
                Intent intentToActivitytimeout = new Intent(mContext, timeoutactivity.class);
                startActivity(intentToActivitytimeout);
            }
        });
        btn_away.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beaconout();
                }
        });
        btn_extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extend();
            }
        });
    }

    private void fn_countdown(int pausetime) {
        //if (et_timer.getText().toString().length()>0) {

            myProgress = 0;

//            try {
//                countDownTimer.cancel();
//
//            } catch (Exception e) {
//
//            }
            //String timeInterval = "2000";
            //String timeInterval = et_timer.getText().toString();

            progress = pausetime;
            //endTime = Integer.parseInt(timeInterval); // up to finish time
            if (user.getType() == 3){
                endTime = limit_usingtime; // up to finish time
            }
            if (user.getType() == 4){
                endTime = limit_leavingtime;
            }


            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
            //millisUntilFinished는 어디서 받아오려나?
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime);
                    progress = progress + 1;

                    if((user.getType() == 4) & (pauseprogress + progress) == limit_usingtime){
                        Intent intentToActivitytimeout = new Intent(mContext, timeoutactivity.class);
                        startActivity(intentToActivitytimeout);
                        finish();
                    }

                    if((progress <= limit_leavingtime/2)&(user.getType() == 4)){
                        btn_extend.setEnabled(false);
                    }
                    else if((user.getType() == 4)){
                        btn_extend.setEnabled(true);
                    }else{
                        btn_extend.setEnabled(true);
                    }


                    int seconds = (endTime - progress) % 60;
                    int minutes =  (((endTime - progress) / 60) % 60);
                    int hours =  (((endTime - progress) / (60 * 60)) % 24);
//                    int seconds = (int) (millisUntilFinished / 1000) % 60;
//                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
//                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    String newtime = hours + ":" + minutes + ":" + seconds;



                    if (newtime.equals("0:0:0")) {
                        tv_time.setText("00:00:00");
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                        tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(hours).length() == 1) {
                        tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        tv_time.setText(hours + ":0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        tv_time.setText(hours + ":" + minutes + ":0" + seconds);
                    } else {
                        tv_time.setText(hours + ":" + minutes + ":" + seconds);
                    }

                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime);

                    if((endTime - progress) == 0){  //타이머 시간이 다 지난 경우
                        if(user.getType() == 3){    //시간 다되면 타임아웃 엑티비티로
                            Intent intentToActivitytimeout = new Intent(mContext, timeoutactivity.class);
                            startActivity(intentToActivitytimeout);
                            //ontick 뭠춰
                            finish();
                        }
                        if(user.getType() == 4){    //자리비운상태로 시간 다되면 자리 반납
                            getout();
                        }
                    }

                }
            };
            countDownTimer.start();

    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);

    }

    public void getout(){
        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
        startActivity(intentToActivitymain);
        checkusingbeanbag.using_beanbag = 0;
        finish();
        //+자리 반납. 타이머 종료. activity 종료
    }
    public void beaconout(){
        //Intent intentToActivityaway = new Intent(mContext, awaybeacon.class);
        //startActivity(intentToActivityaway);
        if (user.getType() == 4){
            txtView.setText("사용중인 좌석은 //입니다");
            btn_away.setText("beacon out");
            user.setType(3);
            btn_extend.setVisibility(View.INVISIBLE);
            pauseprogress = pauseprogress + progress;
            fn_countdown(pauseprogress);
        }
        else if(user.getType() == 3){
            txtView.setText("자리를 비우셨습니다.");
            btn_away.setText("beacon in");
            user.setType(4);
            btn_extend.setVisibility(View.VISIBLE);
            pauseprogress = progress;
            fn_countdown(0);
        }
        //+자리 반납. 타이머 종료. activity 종료
    }
    public void extend(){
        progress = progress - 3;
        //error: 연장시 3초에서 멈춤
    }

}
