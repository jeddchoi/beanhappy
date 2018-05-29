package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends BaseActivity implements
        View.OnClickListener {
    public static final String TAG = "BeanHappy";
    private FirebaseAuth mAuth;
    TextView logout_textBtn;
    Switch pushAlarmSwitch;
    Button quickReserveBtn, reserveBtn, myStatusBtn, reportBtn;
    TextView seatNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // top ui
        logout_textBtn = (TextView)findViewById(R.id.logout_textBtn);
        pushAlarmSwitch = (Switch)findViewById(R.id.pushAlarmSwitch);
        seatNum = (TextView)findViewById(R.id.seatNum);

        // Buttons
        quickReserveBtn = (Button)findViewById(R.id.quickReserveBtn);
        reserveBtn = (Button)findViewById(R.id.reserveBtn);
        myStatusBtn = (Button)findViewById(R.id.myStatusBtn);
        reportBtn = (Button)findViewById(R.id.reportBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.quickReserveBtn) {

        } else if (i == R.id.reserveBtn) {

        } else if (i == R.id.myStatusBtn) {
            if(checkusingbeanbag.using_beanbag == 0) {
                Intent intentToUsing = new Intent(getApplicationContext(), usingactivity.class);
                startActivity(intentToUsing);
                checkusingbeanbag.using_beanbag = 1;
            }
            finish();
        } else if (i == R.id.reportBtn) {
            Intent intentToChat = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intentToChat);
        } else if (i == R.id.logout_textBtn) {

        }
    }
}
