package edu.skku.se3.beanhappy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BeanHappy";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "signIn:" + currentUser.getEmail().toString());
      
        Button btn = (Button)findViewById(R.id.button4);
        Button chat_btn = (Button)findViewById(R.id.button5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkusingbeanbag.using_beanbag == 0) {
                    Intent intentToUsing = new Intent(mContext, usingactivity.class);
                    startActivity(intentToUsing);
                    checkusingbeanbag.using_beanbag = 1;
                }
                finish();
            }
        });
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToChat = new Intent(mContext, ChatActivity.class);
                startActivity(intentToChat);
            }
        });
    }
}
