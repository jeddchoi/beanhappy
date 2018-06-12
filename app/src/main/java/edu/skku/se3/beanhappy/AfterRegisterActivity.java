package edu.skku.se3.beanhappy;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Collection;
import java.util.Date;

public class AfterRegisterActivity extends AppCompatActivity implements BeaconConsumer{

    public static final String TAG = "BeanHappy";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DeviceUuidFactory device;
    private String uuid;
    private String TodayDate;
    private Date today;
    DatabaseReference currentUser;
    public String seat;


    private Context mContext = this;
    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;
    TextView tv_time;
    int endTime = 30;

    public static final String BeaconsEverywhere = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 999;
  
    private  TextView txtView_beanbagseat;
    private TextView txtView1;
    private TextView txtView2;

    Boolean realbeacon =false;
    Boolean istimeout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        /*Beacon permission*/
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            beaconManager=BeaconManager.getInstanceForApplication(this);
            beaconManager.getBeaconParsers().add(new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            //이건 알트비콘의 layout 입니다
            //2-3/4-19이런 것들은 다 byte position 을 의미합니

            beaconManager.bind(this);
        }

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        tv_time= (TextView)findViewById(R.id.tv_timer);
        Button btn_return = (Button)findViewById(R.id.returnseat);
        Button btn_showmap = (Button)findViewById(R.id.showmap);

        device = new DeviceUuidFactory(this);
        uuid = device.getDeviceUuid().toString();

        myCountDownTimer = new MyCountDownTimer(endTime * 1000, 1000);
        myCountDownTimer.start();

        txtView1=(TextView)findViewById(R.id.text1);
        txtView2=(TextView)findViewById(R.id.text2);
        txtView_beanbagseat = findViewById(R.id.beanbagnum);

        //+유저의 type 예약 전에서 예약중 타입으로 변경
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getout();
            }
        });


        btn_showmap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intentToshowmap = new Intent(getApplicationContext(), Aftershowmap.class);
                startActivity(intentToshowmap);
            }
        });

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        today = new Date();
        TodayDate = date.format(today);

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AfterRegisterActivity.this, "location!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
            Toast.makeText(AfterRegisterActivity.this, "자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
        }
        beaconManager.unbind(this);//위에 bind 가 있으니 여기 unbind 가 필요합니다
    }

    /*뒤로가기를 눌렀을 때 작동되는 함수*/
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(AfterRegisterActivity.this,"뒤로가기는 제한됩니다.", Toast.LENGTH_SHORT).show();
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

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            //비콘에 있나 확인하는 if문 넣기
            if(realbeacon){
                beaconin();
            }

            int progress = (int) (millisUntilFinished/1000);
            //progress = 시간이 지난 정도

            /*텍스트로 시간 보여주는 부분*/
            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
            String newtime = hours + ":" + minutes + ":" + seconds;

            if (newtime.equals("0:0:0")) {
                tv_time.setText("00:00:00");
                onFinish();   //원래는 onFinish에 써야하는건데 이상하게 중간에 계속 나가져서 일단 여기에 적어둠. 자리반납하는 함수
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
            istimeout = false;
            Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
            intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentToActivitymain);
            Toast.makeText(getApplicationContext(), "예약이 취소되었습니다.", Toast.LENGTH_LONG).show();
            myCountDownTimer.cancel();
            finish();
        }
    }

    /*자리반납하는 함수*/
    public void getout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("예약 취소");
        builder.setMessage("예약을 취소 하시겠습니까?");
        builder.setNegativeButton("예",
                (dialog, which) -> {
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
                    istimeout = false;
                    Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
                    intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentToActivitymain);

                    //activity 넘어갈때 FLAG로 해야함 일단은 startActivity로 만들었음
                    myCountDownTimer.cancel();  //ontick()(=타이머) 정지
                    finish();   //해당 activity종료
                    //+자리 반납
                });
        builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /*비콘에 들어오는 경우 실행되는 함수*/
    public void beaconin(){
        // Intent intentToActivityusing = new Intent(mContext, usingactivity.class);
        // startActivity(intentToActivityusing);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String seatNum = dataSnapshot.child("users").child(TodayDate).child(uuid).child("seatNum").getValue(String.class);
                mRootRef.child("bb_"+seatNum.charAt(0)).child("bb_"+seatNum).child("state").setValue(3);
                mRootRef.child("bb_"+seatNum.charAt(0)).child("bb_"+seatNum).child("user").child("last_start_time").setValue(System.currentTimeMillis());
                mRootRef.child("bb_"+seatNum.charAt(0)).child("bb_"+seatNum).child("user").child("status").setValue(3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mRootRef.child("users").child(TodayDate).child(uuid).child("status").setValue(3);
        mRootRef.child("users").child(TodayDate).child(uuid).child("last_start_time").setValue(System.currentTimeMillis());


//        Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
//        intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intentToMain);
        istimeout = false;
        Intent intentToUsing = new Intent(getApplicationContext(), usingactivity.class);
        intentToUsing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToUsing);
        //activity 넘어갈때 FLAG로 해야함 일단은 startActivity로 만들었음
        myCountDownTimer.cancel();//ontick()(=타이머) 정지
        finish();   //해당 activity종료
        //실제로는 버튼으로 하는게 아니라 ontick함수안에 수시로 비콘신호를 확인하는 if문을 넣어놨다가 비콘이 확인되면 beaconin함수가 실행되도록 해야함
    }

}
