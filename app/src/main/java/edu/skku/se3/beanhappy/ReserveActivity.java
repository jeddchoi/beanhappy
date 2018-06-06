package edu.skku.se3.beanhappy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReserveActivity extends AppCompatActivity {

    TextView mConditionTextView;
    Button mA1;
    Button mA2;
    Button mA3;
    Button mA4;
    Button mA5;
    Button mA6;

    Beanbag A1 = new Beanbag("A1");
    Beanbag A2 = new Beanbag("A2");
    Beanbag A3 = new Beanbag("A3");
    Beanbag A4 = new Beanbag("A4");
    Beanbag A5 = new Beanbag("A5");
    Beanbag A6 = new Beanbag("A6");

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTypeRef = mRootRef.child("A").child("A-1").child("Type");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        mConditionTextView = (TextView)findViewById(R.id.TextViewCondition);
        mA1 = (Button)findViewById(R.id.bA1);
        mA2 = (Button)findViewById(R.id.bA2);
        mA3 = (Button)findViewById(R.id.bA3);
        mA4 = (Button)findViewById(R.id.bA4);
        mA5 = (Button)findViewById(R.id.bA5);
        mA6 = (Button)findViewById(R.id.bA6);

    }

    protected void onStart() {
        super.onStart();

        mTypeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mConditionTextView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeRef.setValue("Using");
            }
        });
        mA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeRef.setValue("Empty");
            }
        });
        mA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeRef.setValue("Empty");
            }
        });
        mA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeRef.setValue("Empty");
            }
        });
        mA5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeRef.setValue("Empty");
            }
        });
        mA6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(A6);
                mTypeRef.setValue("Using");
            }
        });
    }
    void show(Beanbag An){
        if(An.getBtype() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("예약 확인");
            builder.setMessage("자리를 예약 하시겠습니까?");
            builder.setNegativeButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Beanbag.Bcount--;
                            An.setBtype(3);
                            Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intentToMain);
                            mTypeRef.setValue("Using");
                        }
                    });
            builder.setPositiveButton("아니요",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "자리 예약이 취소되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
        }
    }
}
