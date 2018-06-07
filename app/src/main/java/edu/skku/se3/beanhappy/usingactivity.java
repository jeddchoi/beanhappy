
//package edu.skku.se3.beanhappy;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.RotateAnimation;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Calendar;
//
//import static android.view.animation.Animation.RELATIVE_TO_SELF;
//
//public class usingactivity extends BaseActivity implements
//        View.OnClickListener{
//    public static final String TAG = "BeanHappy";
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//    private DeviceUuidFactory device;
//    private String uuid;
//    private DatabaseReference currentUser;
//
//    private int limit_usingtime = 40;
//    private int limit_leavingtime = 10;
//    /*피크타임 시작과 끝 그리고 피크타임 여부 설정*/
//    private int peak_starthour = 15;
//    private int peak_endhour = 19;
//    private boolean isnotpeak = true;
//
//    private Context mContext = this;
//    private  TextView txtView_beanbagseat;
//    private TextView txtView1;
//    private TextView txtView2;
//
//    private Button btn_main;
//    private Button btn_return;
//    private  Button btn_away;
//    private  Button btn_extend;
//
//    int pauseprogress = 0;
//    int myProgress = 0;
//    ProgressBar progressBarView;
//    //Button btn_start;
//    TextView tv_time;
//    int progress;
//    int additiontime = 0;
//    CountDownTimer countDownTimer;
//    int endTime = 250;
//    int extendtime;
//    boolean isextended = false;
//
//    Calendar t = Calendar.getInstance();
//    String hh = Integer.toString(t.get(Calendar.HOUR_OF_DAY));
//    int H = Integer.parseInt(hh);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_usingactivity);
//
//        device = new DeviceUuidFactory(this);
//        uuid = device.getDeviceUuid().toString();
//
//        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
//        tv_time= (TextView)findViewById(R.id.tv_timer);
//        txtView1=(TextView)findViewById(R.id.text1);
//        txtView2=(TextView)findViewById(R.id.text2);
//
//        btn_main.setOnClickListener(this);
//        btn_return.setOnClickListener(this);
//        btn_away.setOnClickListener(this);
//        btn_extend.setOnClickListener(this);
//
//        txtView_beanbagseat = findViewById(R.id.beanbagnum);
//
//        /*Animation*/
//        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
//        makeVertical.setFillAfter(true);
//        progressBarView.startAnimation(makeVertical);
//        progressBarView.setSecondaryProgress(endTime);
//        progressBarView.setProgress(0);
//
//
//
//
//        fn_countdown(0);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        switch(i) {
//            case R.id.to_main :
//                Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
//                intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intentToActivitymain);
//                break;
//            case R.id.wakeup:
//                getout();
//                break;
//            case R.id.btn_extend:
//                extend();
//                btn_extend.setVisibility(View.INVISIBLE);
//                break;
//            default :
//                break;
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        currentUser = mRootRef.child("users").child(uuid).getRef();
//
//        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {

// package edu.skku.se3.beanhappy;

// import android.app.Activity;
// import android.content.Context;
// import android.content.Intent;
// import android.os.Bundle;
// import android.os.CountDownTimer;
// import android.view.View;
// import android.view.animation.RotateAnimation;
// import android.widget.Button;
// import android.widget.ProgressBar;
// import android.widget.TextView;
// import android.widget.Toast;

// import java.util.Calendar;
// import java.util.UUID;

// import static android.view.animation.Animation.RELATIVE_TO_SELF;

// public class usingactivity extends Activity {

//     private int limit_usingtime = 40;
//     private int limit_leavingtime = 10;
//     /*피크타임 시작과 끝 그리고 피크타임 여부 설정*/
//     int peak_starthour = 9;
//     int peak_endhour = 15;
//     boolean isnotpeak = true;

//     private Context mContext = this;
//     private  TextView txtView_beanbagseat;
//     private TextView txtView1;
//     private TextView txtView2;
//     private  Button btn_away;
//     private  User user;
//     private  Button btn_extend;
//     int pauseprogress = 0;
//     int myProgress = 0;
//     ProgressBar progressBarView;
//     //Button btn_start;
//     TextView tv_time;
//     int progress;
//     int additiontime = 0;
//     CountDownTimer countDownTimer;
//     int endTime = 250;
//     int extendtime;
//     boolean isextended = false;
//     boolean isbeacon = true;    //비콘의 유무로 함수 동작을 하기 위한 변수
//     boolean realbeacon = true; //진짜 비콘의 위치 표현   (실제 비콘 구현되면 이거 지우고 함수값 넣자)

//     Calendar t = Calendar.getInstance();
//     String hh = Integer.toString(t.get(Calendar.HOUR_OF_DAY));
//     int H = Integer.parseInt(hh);

//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_usingactivity);

//         progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
//         tv_time= (TextView)findViewById(R.id.tv_timer);
//         txtView1=(TextView)findViewById(R.id.text1);
//         txtView2=(TextView)findViewById(R.id.text2);

//         Button btn_main = (Button)findViewById(R.id.to_main);
//         Button btn_return = (Button)findViewById(R.id.wakeup);
// //        Button btn_timeout = (Button)findViewById(R.id.timeout);
//         btn_extend = (Button)findViewById(R.id.btn_extend);
//         btn_away = (Button)findViewById(R.id.away);

//         txtView_beanbagseat = findViewById(R.id.beanbagnum);
//         user = new User("ddd@skku.edu", UUID.randomUUID());
//         user.setType(1);
//         //user.setType("Using");

//         /*Animation*/
//         RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
//         makeVertical.setFillAfter(true);
//         progressBarView.startAnimation(makeVertical);
//         progressBarView.setSecondaryProgress(endTime);
//         progressBarView.setProgress(0);

//         btn_extend.setVisibility(View.INVISIBLE);
//         txtView_beanbagseat.setText("A-4(함수X)");

//             fn_countdown(0);


//         btn_main.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
//                 intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                 startActivity(intentToActivitymain);
//             }
//         });
//         btn_return.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 getout();
//             }
//         });
//        btn_timeout.setOnClickListener(new View.OnClickListener() {

//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String bb_location = dataSnapshot.child("seatNum").getValue(String.class);
//                txtView_beanbagseat.setText(bb_location);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "seatNum ");
//            }
//        });

//
//
//    }
//
//    /*뒤로가기를 눌렀을 때 작동되는 함수*/
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        Toast.makeText(usingactivity.this,"뒤로가기는 제한됩니다.", Toast.LENGTH_SHORT).show();
//    }
//
//    private void fn_countdown(int pausetime) {
//        //if (et_timer.getText().toString().length()>0) {
//
//        myProgress = 0;
//
//            try {
//                countDownTimer.cancel();
//
//            } catch (Exception e) {
//
//            }
//            //String timeInterval = "2000";
//            //String timeInterval = et_timer.getText().toString();
//
//            progress = pausetime;
//            //endTime = Integer.parseInt(timeInterval); // up to finish time
//            //if (user.getType() == "Using"){
//            if (user.getType() == 3){
//                endTime = limit_usingtime; // up to finish time
//            }
//            //if (user.getType() == "Out"){
//            if (user.getType() == 4){
//                endTime = limit_leavingtime;
//            }
//
//
//            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
//                @Override
//            //millisUntilFinished는 어디서 받아오려나?
//                public void onTick(long millisUntilFinished) {
//                    setProgress(progress, endTime);
//                    progress = progress + 1;
//
//
//
//                    //if((user.getType() == "Using") & (endTime - progress == 0)){
//                    if((user.getType() == 3) & (endTime - progress == 0)){
//                        timeout();
//                    }
//
//                    //if((user.getType() == "Out") & (pauseprogress + progress == limit_usingtime)){
//                    if((user.getType() == 4) & (pauseprogress + progress == limit_usingtime)){
//                        timeout();
//                    }
//                    //if((user.getType() == "Out") & (endTime - progress == 0)){
//                    if((user.getType() == 4) & (endTime - progress == 0)){
//                        getout();
//                    }
//
//                    //if((progress >= limit_leavingtime/2)&(user.getType() == "Out")){
//                    if((progress >= limit_leavingtime/2)&(user.getType() == 4)){
//                        if(isextended){
//                            btn_extend.setEnabled(false);
//                        }
//                        else{
//                            btn_extend.setEnabled(true);
//                        }
//                    }
//                    else{
//                        btn_extend.setEnabled(false);
//                    }
//
//
//                    int seconds = (endTime - progress) % 60;
//                    int minutes =  (((endTime - progress) / 60) % 60);
//                    int hours =  (((endTime - progress) / (60 * 60)) % 24);
////                    int seconds = (int) (millisUntilFinished / 1000) % 60;
////                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
////                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
//                    String newtime = hours + ":" + minutes + ":" + seconds;
//
//
//
//                    if (newtime.equals("0:0:0")) {
//                        tv_time.setText("00:00:00");
//                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
//                        tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
//                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
//                        tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
//                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
//                        tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
//                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
//                        tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
//                    } else if (String.valueOf(hours).length() == 1) {
//                        tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
//                    } else if (String.valueOf(minutes).length() == 1) {
//                        tv_time.setText(hours + ":0" + minutes + ":" + seconds);
//                    } else if (String.valueOf(seconds).length() == 1) {
//                        tv_time.setText(hours + ":" + minutes + ":0" + seconds);
//                    } else {
//                        tv_time.setText(hours + ":" + minutes + ":" + seconds);
//                    }
//
//                }
//
//                @Override
//                public void onFinish() {
//                    setProgress(progress, endTime);
//
//                    //if((endTime - progress) == 0){  //타이머 시간이 다 지난 경우
//                    //if(user.getType() == "Using"){    //시간 다되면 타임아웃 엑티비티로
//                    if(user.getType() == 3){    //시간 다되면 타임아웃 엑티비티로
//                        timeout();
//                    }
//                    //if(user.getType() == "Out"){    //자리비운상태로 시간 다되면 자리 반납
//                    if(user.getType() == 4){    //자리비운상태로 시간 다되면 자리 반납
//                        getout();
//                    }
//                    //}
//
//                }
//            };
//
//            if(peak_starthour < H & H < peak_endhour | isnotpeak) {
//                countDownTimer.start();
//            }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        //자리반납하는 함수 넣기
//        super.onDestroy();
//    }
//
//    public void setProgress(int startTime, int endTime) {
//        progressBarView.setMax(endTime);
//        progressBarView.setSecondaryProgress(endTime);
//        progressBarView.setProgress(startTime);
//
//    }
//
//    public void getout(){
//        Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
//        intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intentToActivitymain);
//        //checkusingbeanbag.using_beanbag = 0;
//        countDownTimer.cancel();
//        Toast.makeText(usingactivity.this,"자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
//        finish();
//
//        //+자리 반납. 타이머 종료. activity 종료
//    }
//    public void beaconout(){
//        //Intent intentToActivityaway = new Intent(mContext, awaybeacon.class);
//        //startActivity(intentToActivityaway);
//        //if (user.getType() == "Out"){
//        if (user.getType() == 4){
//            txtView_beanbagseat.setText("A-4(함수X)");
//            //빈백 자리 넣는 함수로 채울 예정
//            //btn_away.setText("beacon out");
//            //user.setType("Using");
//            user.setType(3);
//            btn_extend.setVisibility(View.INVISIBLE);
//            txtView1.setVisibility(View.VISIBLE);
//            txtView2.setVisibility(View.VISIBLE);
//            if(isextended){
//                pauseprogress = pauseprogress + progress + limit_leavingtime/2;
//            }
//            else{
//                pauseprogress = pauseprogress + progress;
//            }
//            countDownTimer.cancel();
//            fn_countdown(pauseprogress);
//        }
//        //else if(user.getType() == "Using"){
//        else if(user.getType() == 3){
//            txtView_beanbagseat.setText("자리를 비우셨습니다.");
//            txtView1.setVisibility(View.INVISIBLE);
//            txtView2.setVisibility(View.INVISIBLE);
//            //btn_away.setText("beacon in");
//            //user.setType("Out");
//            user.setType(4);
//            btn_extend.setVisibility(View.VISIBLE);
//            pauseprogress = progress;
//            isextended = false;
//            countDownTimer.cancel();
//            fn_countdown(0);
//        }
//        //+자리 반납. 타이머 종료. activity 종료
//    }
//
//    public void extend(){
//        extendtime = progress - limit_leavingtime/2;
//        countDownTimer.cancel();
//        fn_countdown(extendtime);
//        isextended = true;
//    }
//    public void timeout(){
//        Intent intentToActivitytimeout = new Intent(mContext, timeoutactivity.class);
//        startActivity(intentToActivitytimeout);
//        countDownTimer.cancel();
//        finish();
//    }
//
//}

//         btn_away.setOnClickListener(new View.OnClickListener() {
//                 @Override
//                 public void onClick(View v) {
//                     if(realbeacon){
//                         realbeacon = false;
//                     }else{
//                         realbeacon = true;
//                     }
//                 }
//         });
//         btn_extend.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 extend();
//             }
//         });
//     }
//     /*뒤로가기를 눌렀을 때 작동되는 함수*/
//     @Override
//     public void onBackPressed() {
//         //super.onBackPressed();
//         Toast.makeText(usingactivity.this,"뒤로가기는 제한됩니다.", Toast.LENGTH_SHORT).show();
//     }

//     private void fn_countdown(int pausetime) {
//         //if (et_timer.getText().toString().length()>0) {

//             myProgress = 0;

//             try {
//                 countDownTimer.cancel();

//             } catch (Exception e) {

//             }
//             //String timeInterval = "2000";
//             //String timeInterval = et_timer.getText().toString();

//             progress = pausetime;
//             //endTime = Integer.parseInt(timeInterval); // up to finish time
//             //if (user.getType() == "Using"){
//             if (user.getType() == 1){
//                 endTime = limit_usingtime; // up to finish time
//             }
//             //if (user.getType() == "Out"){
//             if (user.getType() == 3){
//                 endTime = limit_leavingtime;
//             }


//             countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
//                 @Override

//                 public void onTick(long millisUntilFinished) {
//                     setProgress(progress, endTime);
//                     progress = progress + 1;

//                     if(isbeacon != realbeacon){
//                         beaconact();
//                         isbeacon = realbeacon;
//                     }

//                     //if((user.getType() == "Using") & (endTime - progress == 0)){
//                     if((user.getType() == 1) & (endTime - progress == 0)){
//                         timeout();
//                     }

//                     //if((user.getType() == "Out") & (pauseprogress + progress == limit_usingtime)){
//                     if((user.getType() == 3) & (pauseprogress + progress == limit_usingtime)){
//                         timeout();
//                     }
//                     //if((user.getType() == "Out") & (endTime - progress == 0)){
//                     if((user.getType() == 3) & (endTime - progress == 0)){
//                         getout();
//                     }

//                     //if((progress >= limit_leavingtime/2)&(user.getType() == "Out")){
//                     if((progress >= limit_leavingtime/2)&(user.getType() == 3)){
//                         if(isextended){
//                             btn_extend.setEnabled(false);
//                         }
//                         else{
//                             btn_extend.setEnabled(true);
//                         }
//                     }
//                     else{
//                         btn_extend.setEnabled(false);
//                     }


//                     int seconds = (endTime - progress) % 60;
//                     int minutes =  (((endTime - progress) / 60) % 60);
//                     int hours =  (((endTime - progress) / (60 * 60)) % 24);
// //                    int seconds = (int) (millisUntilFinished / 1000) % 60;
// //                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
// //                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
//                     String newtime = hours + ":" + minutes + ":" + seconds;



//                     if (newtime.equals("0:0:0")) {
//                         tv_time.setText("00:00:00");
//                     } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
//                         tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
//                     } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
//                         tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
//                     } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
//                         tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
//                     } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
//                         tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
//                     } else if (String.valueOf(hours).length() == 1) {
//                         tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
//                     } else if (String.valueOf(minutes).length() == 1) {
//                         tv_time.setText(hours + ":0" + minutes + ":" + seconds);
//                     } else if (String.valueOf(seconds).length() == 1) {
//                         tv_time.setText(hours + ":" + minutes + ":0" + seconds);
//                     } else {
//                         tv_time.setText(hours + ":" + minutes + ":" + seconds);
//                     }

//                 }

//                 @Override
//                 public void onFinish() {
//                     setProgress(progress, endTime);

//                     //if((endTime - progress) == 0){  //타이머 시간이 다 지난 경우
//                     //if(user.getType() == "Using"){    //시간 다되면 타임아웃 엑티비티로
//                     if(user.getType() == 1){    //시간 다되면 타임아웃 엑티비티로
//                         timeout();
//                     }
//                     //if(user.getType() == "Out"){    //자리비운상태로 시간 다되면 자리 반납
//                     if(user.getType() == 3){    //자리비운상태로 시간 다되면 자리 반납
//                         getout();
//                     }
//                     //}

//                 }
//             };

//             if(peak_starthour < H & H < peak_endhour | isnotpeak) {
//                 countDownTimer.start();
//             }

//     }

//     @Override
//     protected void onDestroy() {
//         //자리반납하는 함수 넣기
//         super.onDestroy();
//     }

//     public void setProgress(int startTime, int endTime) {
//         progressBarView.setMax(endTime);
//         progressBarView.setSecondaryProgress(endTime);
//         progressBarView.setProgress(startTime);

//     }

//     public void getout(){
//         Intent intentToActivitymain = new Intent(mContext, MainActivity.class);
//         intentToActivitymain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//         startActivity(intentToActivitymain);
//         //checkusingbeanbag.using_beanbag = 0;
//         countDownTimer.cancel();
//         Toast.makeText(usingactivity.this,"자리가 반납되었습니다", Toast.LENGTH_SHORT).show();
//         finish();

//         //+자리 반납. 타이머 종료. activity 종료
//     }
//     public void beaconact(){
//         //Intent intentToActivityaway = new Intent(mContext, awaybeacon.class);
//         //startActivity(intentToActivityaway);
//         //if (user.getType() == "Out"){
//         if (user.getType() == 3){
//             txtView_beanbagseat.setText("A-4(함수X)");
//             //빈백 자리 넣는 함수로 채울 예정
//             //btn_away.setText("beacon out");
//             //user.setType("Using");
//             user.setType(1);
//             btn_extend.setVisibility(View.INVISIBLE);
//             txtView1.setVisibility(View.VISIBLE);
//             txtView2.setVisibility(View.VISIBLE);
//             if(isextended){
//                 pauseprogress = pauseprogress + progress + limit_leavingtime/2;
//             }
//             else{
//                 pauseprogress = pauseprogress + progress;
//             }
//             countDownTimer.cancel();
//             fn_countdown(pauseprogress);
//         }
//         //else if(user.getType() == "Using"){
//         else if(user.getType() == 1){
//             txtView_beanbagseat.setText("자리를 비우셨습니다.");
//             txtView1.setVisibility(View.INVISIBLE);
//             txtView2.setVisibility(View.INVISIBLE);
//             //btn_away.setText("beacon in");
//             //user.setType("Out");
//             user.setType(3);
//             btn_extend.setVisibility(View.VISIBLE);
//             pauseprogress = progress;
//             isextended = false;
//             countDownTimer.cancel();
//             fn_countdown(0);
//         }
//         //+자리 반납. 타이머 종료. activity 종료
//     }
//     public void extend(){
//         extendtime = progress - limit_leavingtime/2;
//         countDownTimer.cancel();
//         fn_countdown(extendtime);
//         isextended = true;
//     }
//     public void timeout(){
//         Intent intentToActivitytimeout = new Intent(mContext, timeoutactivity.class);
//         startActivity(intentToActivitytimeout);
//         countDownTimer.cancel();
//         finish();
//     }

// }

