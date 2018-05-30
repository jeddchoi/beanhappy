package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.content.SharedPreferences;
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
        logout_textBtn = (TextView) findViewById(R.id.logout_textBtn);
        pushAlarmSwitch = (Switch) findViewById(R.id.pushAlarmSwitch);
        seatNum = (TextView) findViewById(R.id.seatNum);

        // Buttons
        findViewById(R.id.logout_textBtn).setOnClickListener(this);
        findViewById(R.id.quickReserveBtn).setOnClickListener(this);
        findViewById(R.id.reserveBtn).setOnClickListener(this);
        findViewById(R.id.myStatusBtn).setOnClickListener(this);
        findViewById(R.id.reportBtn).setOnClickListener(this);


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
        Log.d(TAG, "myStatusBtn clicked"); // test
        if (i == R.id.quickReserveBtn) {

        } else if( i == R.id.quickReserveBtn) {

        } else if (i == R.id.reserveBtn) {
            Intent intentToReserve = new Intent(getApplicationContext(), ReserveActivity.class);
            startActivity(intentToReserve);
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
            Log.d(TAG, "LOGOUT"); // test
            mAuth.signOut();

            SharedPreferences pref;
            SharedPreferences.Editor editor;
            pref = getSharedPreferences("pref", 0);
            editor = pref.edit();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            editor.putBoolean("autoLogin", false);
            editor.commit();
            startActivity(intent);
            finish();
        }
    }
}
