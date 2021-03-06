package edu.skku.se3.beanhappy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class MainActivity extends BaseActivity implements
        View.OnClickListener {


    /*피크타임 시작과 끝 그리고 피크타임 여부 설정*/
//    int peak_starthour = 9;
//    int peak_endhour = 15;

    /*현재시간(시) 계산*/
//    Calendar t = Calendar.getInstance();
//    String hh = Integer.toString(t.get(Calendar.HOUR_OF_DAY));
//    int H = Integer.parseInt(hh);

    public static final String TAG = "BeanHappy";
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mNumAvailTotal = mRootRef.child("NumAvailTotal");
    private DatabaseReference mUserState;
    TextView logout_textBtn;
    //Switch pushAlarmSwitch;
    Button quickReserveBtn, reserveBtn, myStatusBtn, reportBtn;
    TextView seatNum;
    private String uuid;
    private String TodayDate;
    private Date today;
    private DeviceUuidFactory device;
    private User CurrentUser;
    private FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // top ui
        logout_textBtn = (TextView) findViewById(R.id.logout_textBtn);
        //pushAlarmSwitch = (Switch) findViewById(R.id.pushAlarmSwitch);
        seatNum = (TextView) findViewById(R.id.seatNum);

        // Buttons
        findViewById(R.id.logout_textBtn).setOnClickListener(this);
        findViewById(R.id.quickReserveBtn).setOnClickListener(this);
        findViewById(R.id.reserveBtn).setOnClickListener(this);
        findViewById(R.id.myStatusBtn).setOnClickListener(this);
        findViewById(R.id.reportBtn).setOnClickListener(this);


        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        today = new Date();
        TodayDate = date.format(today);
        device = new DeviceUuidFactory(this);
        uuid = device.getDeviceUuid().toString();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        final Long[] time = new Long[1];
        mRootRef.child("users").child(TodayDate).child(uuid).child("last_login_time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                time[0] = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        CurrentUser = new User(user.getEmail(), uuid, time[0]);
        mUserState = mRootRef.child("users").child(TodayDate).child(uuid).child("status");
        /*피크타임 이용을 한번으로 제한시*/
        //mUserState = mRootRef.child("users").child(TodayDate).child(uuid);
    }

    @Override
    protected void onStart() {
        super.onStart();


        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().startsWith("bb_")) {
                        Log.d(TAG, "MAINACTIVITY = " + ds.getKey() + sum);
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object count = map.get("bb_NumAvail");
                        sum += Integer.parseInt(String.valueOf(count));
                    }
                }
                mNumAvailTotal.setValue(sum);
                String buf = String.format(getString(R.string.AllCount), sum);
                seatNum.setText(buf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "signIn:" + currentUser.getEmail().toString()); // test

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN); //태스크의 첫 액티비티로 시작
        intent.addCategory(Intent.CATEGORY_HOME);   //홈화면 표시
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //새로운 태스크를 생성하여 그 태스크안에서 액티비티 추가
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
       // Log.d(TAG, "myStatusBtn clicked"); // test
        mUserState.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int status = dataSnapshot.getValue(int.class);
                /*피크타임 한번 이용으로 제한시*/
//                int status = dataSnapshot.child("status").getValue(int.class);
//                long lastreservetime = dataSnapshot.child("last_reserve_time").getValue(int.class);
//                long lastreservehour = lastreservetime / (1000 * 60 * 60);
//                Boolen is_peaktime = false;
//                Boolen is_curentpeaktime = false;
//                if(peak_starthour <= lastreservehour & lastreservehour <= peak_endhour){
//                  is_peaktime = true;
//                }
//                if(peak_starthour <= lastreservehour & lastreservehour <= peak_endhour){
//                    is_curentpeaktime = true;
//                }
                mNumAvailTotal.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int num = dataSnapshot.getValue(int.class);
                        if(i == R.id.quickReserveBtn && (num == 0 ))
                            Toast.makeText(getApplicationContext(),"예약할 수 있는 좌석이 없습니다.", Toast.LENGTH_LONG).show();
//                      else if(i == R.id.quickReserveBtn && is_peaktime && is_curentpeaktime){
//                          Toast.makeText(getApplicationContext(),"이미 피크타임에 이용하셨습니다.", Toast.LENGTH_LONG).show();
//                      }
                        else if(i == R.id.quickReserveBtn && num != 0 && status == 0){
                            reserving();
                        }
                        else if (i == R.id.quickReserveBtn && num != 0 && status != 0)
                            Toast.makeText(getApplicationContext(), "이미 이용중 입니다.", Toast.LENGTH_LONG).show();
                        else if (i == R.id.myStatusBtn && status == 0)
                            Toast.makeText(getApplicationContext(), "아직 이용중이지 않습니다.", Toast.LENGTH_LONG).show();
                        else if ( i == R.id.myStatusBtn && (status == 3 || status == 4)){
                            Intent intentToUsing = new Intent(getApplicationContext(), usingactivity.class);
                            intentToUsing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentToUsing);
                        }
                        else if (i == R.id.reserveBtn && status == 0){
                            Intent intentToReserve = new Intent(getApplicationContext(), ReserveActivity.class);
                            intentToReserve.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentToReserve);
                            finish();
                        }
//                        else if (i == R.id.reserveBtn && is_peaktime && is_curentpeaktime){
//                            Toast.makeText(getApplicationContext(),"이미 피크타임에 이용하셨습니다. 예약은 불가합니다.", Toast.LENGTH_LONG).show();
//                            Intent intentToStatus = new Intent(getApplicationContext(), StatusActivity.class);
//                            intentToStatus.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intentToStatus);
//                        }
                        else if (i == R.id.reserveBtn && status != 0){
                            Intent intentToStatus = new Intent(getApplicationContext(), StatusActivity.class);
                            intentToStatus.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentToStatus);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* if (i == R.id.quickReserveBtn) {
            Intent intentToquick = new Intent(getApplicationContext(), AfterRegisterActivity.class);
            intentToquick.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentToquick);
            finish();
        } if (i == R.id.reserveBtn) {
            Intent intentToReserve = new Intent(getApplicationContext(), ReserveActivity.class);
            intentToReserve.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentToReserve);
        } else if (i == R.id.myStatusBtn) {

//            Intent intentToUsing = new Intent(getApplicationContext(), usingactivity.class);
//            intentToUsing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intentToUsing);
//            finish();*/
         if (i == R.id.reportBtn) {
            Intent intentToChat = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intentToChat);
        } else if (i == R.id.logout_textBtn) {
            Log.d(TAG, "LOGOUT"); // test
            mAuth.signOut();

            SharedPreferences pref;
            SharedPreferences.Editor editor;
            pref = PreferenceManager.getDefaultSharedPreferences(this);
            editor = pref.edit();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            editor.putBoolean("autoLogin", false);
            editor.commit();
            startActivity(intent);
            finish();
        }
    }
    public void reserving() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("빠른 예약");
        builder.setMessage("빠른 예약을 하시겠습니까?");
        builder.setNegativeButton("예",
                (dialog, which) -> {
                    mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            outerLoop :
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                if(ds.getKey().startsWith("bb_")){
                                    for(DataSnapshot ds2 : ds.getChildren()){
                                        if(!ds2.getKey().equals("bb_NumAvail")){
                                            if(ds2.child("state").getValue(int.class) == 0){
                                                String seatNumber = ds2.getKey().substring(3, 5);
                                                mRootRef.child(ds.getKey()).child(ds2.getKey()).child("state").setValue(2);
                                                mRootRef.child(ds.getKey()).child(ds2.getKey()).child("user").setValue(CurrentUser);
                                                mRootRef.child(ds.getKey()).child(ds2.getKey()).child("user").child("last_reserve_time").setValue(System.currentTimeMillis());
                                                mRootRef.child(ds.getKey()).child(ds2.getKey()).child("user").child("status").setValue(2);

                                                mRootRef.child("users").child(TodayDate).child(uuid).setValue(CurrentUser);
                                                mRootRef.child("users").child(TodayDate).child(uuid).child("last_reserve_time").setValue(System.currentTimeMillis());
                                                mRootRef.child("users").child(TodayDate).child(uuid).child("seatNum").setValue(seatNumber);
                                                mRootRef.child("users").child(TodayDate).child(uuid).child("status").setValue(2);

                                                Toast.makeText(getApplicationContext(), seatNumber+" 자리가 예약 되었습니다.", Toast.LENGTH_LONG).show();
                                                break outerLoop;
                                            }
                                        }
                                    }
                                }
                            }
                            Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                            intentToAfter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentToAfter);
                            finish();
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
                    //intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //startActivity(intentToActivitymain);
                });
        builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
