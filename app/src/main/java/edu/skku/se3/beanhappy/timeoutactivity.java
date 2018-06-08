package edu.skku.se3.beanhappy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.CountDownTimer;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class timeoutactivity extends Activity {

    private Context mContext = this;

    int myProgress = 0;
    ProgressBar progressBarView;
    TextView tv_time;
    int progress;
    CountDownTimer countDownTimer;
    int endTime = 240;
    Vibrator vide;

    //getout 에 들어갈 변수들
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private String TodayDate;
    private Date today;
    private String uuid;
    private DeviceUuidFactory device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        //진동기능
        vide = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        //btn_start = (Button)findViewById(R.id.btn_start);
        tv_time= (TextView)findViewById(R.id.tv_timer);


        /*vibrate*/
        long[] pattern = {0, 1000, 200, 1000, 200};
        vide.vibrate(pattern, 0);

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);


        fn_countdown();

        Button btn = (Button)findViewById(R.id.wakeup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getout();
            }
        });
        device = new DeviceUuidFactory(this);
        uuid = device.getDeviceUuid().toString();

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        today = new Date();
        TodayDate = date.format(today);
    }

    /*뒤로가기를 눌렀을 때 작동되는 함수*/
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(timeoutactivity.this,"뒤로가기는 제한됩니다.", Toast.LENGTH_SHORT).show();
    }

    private void fn_countdown() {
        //if (et_timer.getText().toString().length()>0) {

        myProgress = 0;

        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }
        //String timeInterval = "2000";
        //String timeInterval = et_timer.getText().toString();

        progress = 15000;
        //endTime = Integer.parseInt(timeInterval); // up to finish time
        endTime = 15000;


        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            //millisUntilFinished는 어디서 받아오려나?
            public void onTick(long millisUntilFinished) {
                setProgress(progress, endTime);
                progress = progress - 1;

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
                //자리반납(다른건 안바꾸기)
                Toast.makeText(timeoutactivity.this,"자리가 반납되었습니다", Toast.LENGTH_SHORT).show();

            }
        };
        countDownTimer.start();

    }

    @Override
    protected void onDestroy() {
        //자리를 반납하는 함수 넣기
        Toast.makeText(timeoutactivity.this,"자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);

    }

    public void getout(){
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String seatNum = dataSnapshot.child("users").child(TodayDate).child(uuid).child("seatNum").getValue(String.class);
                mRootRef.child("bb_"+seatNum.charAt(0)).child("bb_"+seatNum).child("state").setValue(0);
                mRootRef.child("bb_"+seatNum.charAt(0)).child("bb_"+seatNum).child("user").removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRef.child("users").child(TodayDate).child(uuid).child("status").setValue(0);
        mRootRef.child("users").child(TodayDate).child(uuid).child("seatNum").setValue(null);
        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
        startActivity(intentToActivitymain);
        //checkusingbeanbag.using_beanbag = 0;
        countDownTimer.cancel();
        vide.cancel();
        Toast.makeText(timeoutactivity.this,"자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
        finish();


        //+자리 반납
    }
}
