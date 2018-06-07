package edu.skku.se3.beanhappy;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AfterRegisterActivity extends AppCompatActivity {

    public static final String TAG = "BeanHappy";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DeviceUuidFactory device;
    private String uuid;

    private Context mContext = this;
    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;
    TextView tv_time;
    int endTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        tv_time= (TextView)findViewById(R.id.tv_timer);
        Button btn_return = (Button)findViewById(R.id.returnseat);
        Button btn_beaconin = (Button)findViewById(R.id.beaconin);

        device = new DeviceUuidFactory(this);
        uuid = device.getDeviceUuid().toString();

        myCountDownTimer = new MyCountDownTimer(endTime * 1000, 1000);
        myCountDownTimer.start();

        //+유저의 type 예약 전에서 예약중 타입으로 변경
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getout();

            }
        });

        /*테스트용 임시 버튼*/
        btn_beaconin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beaconin();
            }
        });

    }
    /*뒤로가기를 눌렀을 때 작동되는 함수*/
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(AfterRegisterActivity.this,"뒤로가기는 제한됩니다.", Toast.LENGTH_SHORT).show();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            //비콘에 있나 확인하는 if문 넣기

            int progress = (int) (millisUntilFinished/1000);
            //progress = 시간이 지난 정도

            /*텍스트로 시간 보여주는 부분*/
            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
            String newtime = hours + ":" + minutes + ":" + seconds;

            if (newtime.equals("0:0:0")) {
                tv_time.setText("00:00:00");
                getout();   //원래는 onFinish에 써야하는건데 이상하게 중간에 계속 나가져서 일단 여기에 적어둠. 자리반납하는 함수
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

            /*progressbar에 진행정도 보여주는 부분*/

            //progressBar.setProgress(progressBar.getMax() - progress); //이건 원래 소스에 있던건데 막대가 점점 차는 식으로 보여줌
            progressBar.setMax(endTime);
            progressBar.setProgress(progress);  //실제로 progressbar로 진행정도를 출력하는 부분

        }

        @Override
        public void onFinish() {    //타이머가 종료될때 실행됨
            myCountDownTimer.cancel();
            finish();
        }
    }

    /*자리반납하는 함수*/
    public void getout(){

        mRootRef.child("users").child(uuid).child("state").setValue(0);
        mRootRef.child("users").child(uuid).child("seatNum").setValue(null);
        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
        intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToActivitymain);

        //activity 넘어갈때 FLAG로 해야함 일단은 startActivity로 만들었음
        myCountDownTimer.cancel();  //ontick()(=타이머) 정지
        finish();   //해당 activity종료
        //+자리 반납
    }

    /*비콘에 들어오는 경우 실행되는 함수*/
    public void beaconin(){
//        Intent intentToActivityusing = new Intent(mContext, usingactivity.class);
//        startActivity(intentToActivityusing);



//        Intent intentToUsing = new Intent(getApplicationContext(), usingactivity.class);
//        intentToUsing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intentToUsing);
//        //activity 넘어갈때 FLAG로 해야함 일단은 startActivity로 만들었음
//        myCountDownTimer.cancel();  //ontick()(=타이머) 정지
//        finish();   //해당 activity종료
        //실제로는 버튼으로 하는게 아니라 ontick함수안에 수시로 비콘신호를 확인하는 if문을 넣어놨다가 비콘이 확인되면 beaconin함수가 실행되도록 해야함
    }
}
