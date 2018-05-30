package edu.skku.se3.beanhappy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReserveActivity extends AppCompatActivity {

    Beanbag b = new Beanbag();
    int bcount = b.getBcount();

    TextView mConditionTextView;
    Button mA1;
    Button mA2;
    Button mA3;
    Button mA4;
    Button mA5;
    Button mA6;

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("A").child("Type");

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

        mConditionRef.addValueEventListener(new ValueEventListener() {
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
                mConditionRef.setValue("2");
            }
        });
        mA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("1");
            }
        });
    }
}
