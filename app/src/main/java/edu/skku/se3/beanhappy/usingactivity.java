package edu.skku.se3.beanhappy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;



import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class usingactivity extends AppCompatActivity implements BeaconConsumer{
    public static final String TAG = "BeanHappy";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DeviceUuidFactory device;
    private String uuid;
    DatabaseReference currentUser;
    public String seat;
    private String TodayDate;
    Date today;

    public static final int limit_usingtime = 40; // 1 hour
    public static final int limit_leavingtime = 10; // 5 min

    public static final String BeaconsEverywhere = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 998;

    /*피크타임 시작과 끝 그리고 피크타임 여부 설정*/
    int peak_starthour = 9;
    int peak_endhour = 15;
    boolean isnotpeak = true;

    private  TextView txtView_beanbagseat;
    private TextView txtView1;
    private TextView txtView2;

    private Context mContext = this;

    public int localuserstate = 1;

    int pauseprogress = 0;
    int myProgress = 0;

    ProgressBar progressBarView;
    TextView tv_time;
    int progress;
    CountDownTimer countDownTimer;
    int endTime = 250;
    int extendtime;

    boolean istimeout = true;
    boolean isextended = false;
    boolean isbeacon = true;    //비콘의 유무로 함수 동작을 하기 위한 변수
    boolean realbeacon = true; //진짜 비콘의 위치 표현   (실제 비콘 구현되면 이거 지우고 함수값 넣자)

    Button btn_main;
    Button btn_return;
    Button btn_away;
    Button btn_extend;

    Calendar t = Calendar.getInstance();
    String hh = Integer.toString(t.get(Calendar.HOUR_OF_DAY));
    int H = Integer.parseInt(hh);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usingactivity);

        device = new DeviceUuidFactory(this);
        uuid = device.getDeviceUuid().toString();

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            beaconManager= BeaconManager.getInstanceForApplication(this);
            beaconManager.getBeaconParsers().add(new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            //이건 알트비콘의 layout 입니다
            //2-3/4-19이런 것들은 다 byte position 을 의미합니

            beaconManager.bind(this);
        }

//        btn_main.setOnClickListener(this);
//        btn_return.setOnClickListener(this);
//        btn_away.setOnClickListener(this);
//        btn_extend.setOnClickListener(this);

        btn_main = (Button)findViewById(R.id.to_main);
        btn_return = (Button)findViewById(R.id.wakeup);
        btn_extend = (Button)findViewById(R.id.btn_extend);
        btn_away = (Button)findViewById(R.id.away);


        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        tv_time= (TextView)findViewById(R.id.tv_timer);
        txtView1=(TextView)findViewById(R.id.text1);
        txtView2=(TextView)findViewById(R.id.text2);
        txtView_beanbagseat = findViewById(R.id.beanbagnum);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        today = new Date();
        TodayDate = date.format(today);

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        btn_extend.setVisibility(View.INVISIBLE);

        currentUser = mRootRef.child("users").child(TodayDate).child(uuid).getRef();
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                seat = dataSnapshot.child("seatNum").getValue(String.class);
                txtView_beanbagseat.setText(seat);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //txtView_beanbagseat.setText(seat);

        fn_countdown(0);


        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
                intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                istimeout= false;
                startActivity(intentToActivitymain);
            }
        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getout();
            }
        });
        btn_away.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realbeacon){
                    realbeacon = false;
                }else{
                    realbeacon = true;
                }
            }
        });
        btn_extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extend();
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        switch(i) {
//            case R.id.to_main :
//                Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
//                intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intentToActivitymain);
//                finish();
//                break;
//            case R.id.wakeup:
//                getout();
//                break;
//            case R.id.btn_extend:
//                extend();
//                btn_extend.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.away:
//                if(realbeacon){
//                    realbeacon = false;
//                }
//                else{
//                    realbeacon = true;
//                }
//                break;
//            default :
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(usingactivity.this,"뒤로가기는 제한됩니다.", Toast.LENGTH_SHORT).show();
    }

    private void fn_countdown(int pausetime) {
        //if (et_timer.getText().toString().length()>0) {

        myProgress = 0;

        try {
            countDownTimer.cancel();

        } catch (Exception e) {

        }
        //String timeInterval = "2000";
        //String timeInterval = et_timer.getText().toString();

        progress = pausetime;
        //endTime = Integer.parseInt(timeInterval); // up to finish time
        //if (user.getType() == "Using"){
        if (localuserstate == 1){
            endTime = limit_usingtime; // up to finish time
        }
        //if (user.getType() == "Out"){
        if (localuserstate == 3){
            endTime = limit_leavingtime;
        }


        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                setProgress(progress, endTime);
                progress = progress + 1;

                if(isbeacon != realbeacon){
                    beaconact();
                    isbeacon = realbeacon;
                }

                //if((user.getType() == "Using") & (endTime - progress == 0)){
                if((localuserstate == 1) & (endTime - progress == 0)){
                    timeout();
                }

                //if((user.getType() == "Out") & (pauseprogress + progress == limit_usingtime)){
                if((localuserstate == 3) & (pauseprogress + progress == limit_usingtime)){
                    timeout();
                }
                //if((user.getType() == "Out") & (endTime - progress == 0)){
                if((localuserstate == 3) & (endTime - progress == 0)){
                    getout();
                }

                //if((progress >= limit_leavingtime/2)&(user.getType() == "Out")){
                if((progress >= limit_leavingtime/2)&(localuserstate == 3)){
                    if(isextended){
                        btn_extend.setEnabled(false);
                    }
                    else{
                        btn_extend.setEnabled(true);
                    }
                }
                else{
                    btn_extend.setEnabled(false);
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

                //if((endTime - progress) == 0){  //타이머 시간이 다 지난 경우
                //if(user.getType() == "Using"){    //시간 다되면 타임아웃 엑티비티로
                if(localuserstate == 1){    //시간 다되면 타임아웃 엑티비티로
                    timeout();
                }
                //if(user.getType() == "Out"){    //자리비운상태로 시간 다되면 자리 반납
                if(localuserstate == 3){    //자리비운상태로 시간 다되면 자리 반납
                    getout();
                }
                //}

            }
        };

        if(peak_starthour < H & H < peak_endhour | isnotpeak) {
            countDownTimer.start();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(usingactivity.this, "location!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (istimeout) {
            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String seatNum = dataSnapshot.child("users").child(TodayDate).child(uuid).child("seatNum").getValue(String.class);
                    mRootRef.child("bb_" + seatNum.charAt(0)).child("bb_" + seatNum).child("state").setValue(0);
                    mRootRef.child("bb_" + seatNum.charAt(0)).child("bb_" + seatNum).child("user").removeValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });

            mRootRef.child("users").child(TodayDate).child(uuid).child("status").setValue(0);
            mRootRef.child("users").child(TodayDate).child(uuid).child("seatNum").setValue(null);
            Toast.makeText(usingactivity.this, "자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
        }
        beaconManager.unbind(this);
        super.onDestroy();
    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(endTime - startTime);

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
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
        mRootRef.child("users").child(TodayDate).child(uuid).child("status").setValue(0);
        mRootRef.child("users").child(TodayDate).child(uuid).child("seatNum").setValue(null);
        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
        intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToActivitymain);

        //activity 넘어갈때 FLAG로 해야함 일단은 startActivity로 만들었음
        countDownTimer.cancel();  //ontick()(=타이머) 정지
        Toast.makeText(usingactivity.this,"자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
        istimeout = false;
        finish();   //해당 activity종료
        //+자리 반납
        //+자리 반납

    }
    public void beaconact(){
        if (localuserstate == 3){
            txtView_beanbagseat.setText(seat);
            //빈백 자리 넣는 함수로 채울 예정
            //btn_away.setText("beacon out");
            //user.setType("Using");
            localuserstate = 1;
            btn_extend.setVisibility(View.INVISIBLE);
            txtView1.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.VISIBLE);
            if(isextended){
                pauseprogress = pauseprogress + progress + limit_leavingtime/2;
            }
            else{
                pauseprogress = pauseprogress + progress;
            }
            countDownTimer.cancel();
            fn_countdown(pauseprogress);
        }
        else if(localuserstate == 1){
            txtView_beanbagseat.setText("자리를 비우셨습니다.");
            txtView1.setVisibility(View.INVISIBLE);
            txtView2.setVisibility(View.INVISIBLE);
            //btn_away.setText("beacon in");
            //user.setType("Out");
            localuserstate = 3;
            btn_extend.setVisibility(View.VISIBLE);
            pauseprogress = progress;
            isextended = false;
            countDownTimer.cancel();
            fn_countdown(0);
        }
    }
    public void extend(){
        extendtime = progress - limit_leavingtime/2;
        countDownTimer.cancel();
        fn_countdown(extendtime);
        isextended = true;
    }
    public void timeout(){
        Intent intentToActivitytimeout = new Intent(mContext, timeoutactivity.class);
        startActivity(intentToActivitytimeout);
        countDownTimer.cancel();
        istimeout = false;
        finish();
    }

    @Override
    public void onBeaconServiceConnect(){
        //비콘은 UUID/major/minor 넘버로 구별합니다

        //region 이라는 것은 geographical region 을 의미하는 것이 아니라
        //저희가 관심있는 특정 비콘을 의미합니다 그러므로 비콘의 UUID 를 알아야 합니다

        final Region region = new Region("myBeacons", Identifier.parse("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), null, null);

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.i(BeaconsEverywhere, "I just saw an beacon for the first time! Id1->"+region.getId1()
                            +" id 2:"+region.getId2()+" id 3:"+region.getId3());

                    //첫번째 아이디는 UUID
                    //두번째 아이디는 major
                    //세번째 아이디는 minor

                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.d(BeaconsEverywhere, "did exit region");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.i(BeaconsEverywhere,"beacons.size less then 0");
                if (beacons.size() > 0) {
                    Log.i(BeaconsEverywhere, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                    for(Beacon beacon: beacons){
                        if(beacon.getDistance()<2.0){
                            //비콘이 2미터 안으로 들어왔을 경우
                            realbeacon = true;
                            Log.d(BeaconsEverywhere, "I see a beacon that in inside the 2.0 range");
                            //특정한 액션을 여기에 쓰면 됩니다
                        }
                        else{
                            realbeacon = false;
                            Log.d(BeaconsEverywhere, "I see a beacon that in outside the 2.0 range");
                        }
                    }
                }
                else{
                    realbeacon = false;
                    Log.d(BeaconsEverywhere, "Where is beacon...");
                }
            }
        });


        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }


}
