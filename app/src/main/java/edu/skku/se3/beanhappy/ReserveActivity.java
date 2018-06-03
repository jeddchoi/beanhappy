package edu.skku.se3.beanhappy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.annotation.Documented;
import java.util.Collection;

public class ReserveActivity extends AppCompatActivity {

    private
    Button mA1;
    Button mA2;
    Button mA3;
    Button mA4;
    Button mA5;
    Button mA6;
    FirebaseFirestore db;
    FirebaseFirestore bc;

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTypeRefA1 = mRootRef.child("A").child("A-1").child("Type");
    DatabaseReference mTypeRefA2 = mRootRef.child("A").child("A-2").child("Type");
    DatabaseReference mTypeRefA3 = mRootRef.child("A").child("A-3").child("Type");
    DatabaseReference mTypeRefA4 = mRootRef.child("A").child("A-4").child("Type");
    DatabaseReference mTypeRefA5 = mRootRef.child("A").child("A-5").child("Type");
    DatabaseReference mTypeRefA6 = mRootRef.child("A").child("A-6").child("Type");
    DatabaseReference mCount = mRootRef.child("BeanbagCount");

    String text1;
    String text2;
    String text3;
    String text4;
    String text5;
    String text6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        db = FirebaseFirestore.getInstance();

        mA1 = (Button)findViewById(R.id.bA1);
        mA2 = (Button)findViewById(R.id.bA2);
        mA3 = (Button)findViewById(R.id.bA3);
        mA4 = (Button)findViewById(R.id.bA4);
        mA5 = (Button)findViewById(R.id.bA5);
        mA6 = (Button)findViewById(R.id.bA6);

        mTypeRefA1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text1 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    protected void onStart() {
        super.onStart();

        mTypeRefA1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text1 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTypeRefA1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text1 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTypeRefA2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text2 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTypeRefA3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text3 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTypeRefA4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text4 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTypeRefA5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text5 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTypeRefA6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text6 = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("A-1", text1);
            }
        });
        mA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("A-2", text2);
            }
        });
        mA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("A-3", text3);
            }
        });
        mA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("A-4", text4);
            }
        });
        mA5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("A-5", text5);
            }
        });
        mA6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("A-6", text6);
            }
        });
    }
    void show(String An, String text){

        DocumentReference docRef = db.collection("Beanbag").document(An);
        DocumentReference beanRef = bc.collection("Beanbag").document("BeanbagCount");
        docRef.get().addOnCompleteListener((task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document!=null && document.exists()){
                    if(document.get("Type").equals("Usable")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("예약 확인");
                        builder.setMessage("자리를 예약 하시겠습니까?");
                        builder.setNegativeButton("예",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Beanbag.Bcount--;
                                        mCount.setValue(Beanbag.Bcount);
                                        if(An == "A-1")
                                            mTypeRefA1.setValue("Using");
                                        else if(An == "A-2")
                                            mTypeRefA2.setValue("Using");
                                        else if(An == "A-3")
                                            mTypeRefA3.setValue("Using");
                                        else if(An == "A-4")
                                            mTypeRefA4.setValue("Using");
                                        else if(An == "A-5")
                                            mTypeRefA5.setValue("Using");
                                        else if(An == "A-6")
                                            mTypeRefA6.setValue("Using");
                                        docRef.update("Type", "Using");
                                        beanRef.update("BCount", Beanbag.Bcount);
                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
                                        Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intentToMain);
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
                    } else if(document.get("Type").equals("Using")) {
                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }));
    }
}
