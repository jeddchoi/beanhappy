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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class MainActivity extends BaseActivity implements
        View.OnClickListener {

    public static final String TAG = "BeanHappy";
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mNumAvailTotal = mRootRef.child("NumAvailTotal");
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
    public void onClick(View v) {
        int i = v.getId();
        Log.d(TAG, "myStatusBtn clicked"); // test
        if (i == R.id.quickReserveBtn) {
            Intent intentToquick = new Intent(getApplicationContext(), AfterRegisterActivity.class);
            intentToquick.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentToquick);
            finish();
        }  else if (i == R.id.reserveBtn) {
            Intent intentToReserve = new Intent(getApplicationContext(), ReserveActivity.class);
            startActivity(intentToReserve);
        } else if (i == R.id.myStatusBtn) {

            Intent intentToUsing = new Intent(getApplicationContext(), usingactivity.class);
            intentToUsing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentToUsing);
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
