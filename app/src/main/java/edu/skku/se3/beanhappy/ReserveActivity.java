package edu.skku.se3.beanhappy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReserveActivity extends AppCompatActivity {

    ImageButton mA1, mA2, mA3, mA4;
    ImageButton mB1, mB2, mB3, mB4, mB5, mB6, mB7, mB8;
    ImageButton mC1, mC2, mC3, mC4, mC5;
    FirebaseFirestore db; //DB
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mA1 = (ImageButton)findViewById(R.id.bA1);
        mA2 = (ImageButton)findViewById(R.id.bA2);
        mA3 = (ImageButton)findViewById(R.id.bA3);
        mA4 = (ImageButton)findViewById(R.id.bA4);
        mB1 = (ImageButton)findViewById(R.id.bB1);
        mB2 = (ImageButton)findViewById(R.id.bB2);
        mB3 = (ImageButton)findViewById(R.id.bB3);
        mB4 = (ImageButton)findViewById(R.id.bB4);
        mB5 = (ImageButton)findViewById(R.id.bB5);
        mB6 = (ImageButton)findViewById(R.id.bB6);
        mB7 = (ImageButton)findViewById(R.id.bB7);
        mB8 = (ImageButton)findViewById(R.id.bB8);
        mC1 = (ImageButton)findViewById(R.id.bC1);
        mC2 = (ImageButton)findViewById(R.id.bC2);
        mC3 = (ImageButton)findViewById(R.id.bC3);
        mC4 = (ImageButton)findViewById(R.id.bC4);
        mC5 = (ImageButton)findViewById(R.id.bC5);


        user = mAuth.getCurrentUser();

        mA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ashow("A-1");
            }
        });
        mA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ashow("A-2");
            }
        });
        mA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ashow("A-3");
            }
        });
        mA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ashow("A-4");
            }
        });
        mB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-1");
            }
        });
        mB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-2");
            }
        });
        mB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-3");
            }
        });
        mB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-4");
            }
        });
        mB5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-5");
            }
        });
        mB6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-6");
            }
        });
        mB7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-7");
            }
        });
        mB8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bshow("B-8");
            }
        });
        mC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cshow("C-1");
            }
        });
        mC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cshow("C-2");
            }
        });
        mC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cshow("C-3");
            }
        });
        mC4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cshow("C-4");
            }
        });
        mC5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cshow("5-4");
            }
        });
    }
    void Ashow(String An){ // 눌렀을 때 자리 "Usable"이면 다이얼로그 표시, 그 외 "이용 중", 예약하면 Using, Reserving으로 변경

        int[] Allcount = new int[1];
        int[] ACount = new int[1];

        DocumentReference docRef = db.collection("Beanbag").document(An);
        DocumentReference userRef = db.collection("User").document(user.getUid());
        DocumentReference allcountRef = db.collection("Beanbag").document("AllCount");
        DocumentReference acountRef = db.collection("Beanbag").document("ACount");
        docRef.get().addOnCompleteListener((task1 -> {
            if(task1.isSuccessful()){
                DocumentSnapshot document = task1.getResult();
                allcountRef.get().addOnCompleteListener((task2 -> {
                    if(task2.isSuccessful()){
                        DocumentSnapshot allcount = task2.getResult();
                        acountRef.get().addOnCompleteListener((task3 -> {
                            if(task3.isSuccessful()){
                                DocumentSnapshot acount = task3.getResult();
                                if(document!=null && document.exists()){
                                    if(document.getLong("Type") == 1){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                        builder.setTitle("예약 확인");
                                        builder.setMessage("자리를 예약 하시겠습니까?");
                                        builder.setNegativeButton("예",
                                                new DialogInterface.OnClickListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        docRef.update("Type", 2);
                                                        userRef.update("type", 2);
                                                        userRef.update("SeatNum", An);
                                                        ACount[0] = Math.toIntExact(acount.getLong("ACount"));
                                                        acountRef.update("ACount", --ACount[0]);
                                                        Allcount[0] = Math.toIntExact(allcount.getLong("AllCount"));
                                                        allcountRef.update("AllCount", --Allcount[0]);
                                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
                                                        Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                                                        startActivity(intentToAfter);
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
                                    } else if(document.getLong("Type") != 1) {
                                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }));
                    }
                }));
            }
        }));
    }
    void Bshow(String An){ // 눌렀을 때 자리 "Usable"이면 다이얼로그 표시, 그 외 "이용 중", 예약하면 Using, Reserving으로 변경

        int[] Allcount = new int[1];
        int[] BCount = new int[1];

        DocumentReference docRef = db.collection("Beanbag").document(An);
        DocumentReference userRef = db.collection("User").document(user.getUid());
        DocumentReference allcountRef = db.collection("Beanbag").document("AllCount");
        DocumentReference acountRef = db.collection("Beanbag").document("BCount");
        docRef.get().addOnCompleteListener((task1 -> {
            if(task1.isSuccessful()){
                DocumentSnapshot document = task1.getResult();
                allcountRef.get().addOnCompleteListener((task2 -> {
                    if(task2.isSuccessful()){
                        DocumentSnapshot allcount = task2.getResult();
                        acountRef.get().addOnCompleteListener((task3 -> {
                            if(task3.isSuccessful()){
                                DocumentSnapshot bcount = task3.getResult();
                                if(document!=null && document.exists()){
                                    if(document.getLong("Type") == 1){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                        builder.setTitle("예약 확인");
                                        builder.setMessage("자리를 예약 하시겠습니까?");
                                        builder.setNegativeButton("예",
                                                new DialogInterface.OnClickListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        docRef.update("Type", 2);
                                                        userRef.update("type", 2);
                                                        userRef.update("SeatNum", An);
                                                        BCount[0] = Math.toIntExact(bcount.getLong("BCount"));
                                                        acountRef.update("BCount", --BCount[0]);
                                                        Allcount[0] = Math.toIntExact(allcount.getLong("AllCount"));
                                                        allcountRef.update("AllCount", --Allcount[0]);
                                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
                                                        Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                                                        startActivity(intentToAfter);
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
                                    } else if(document.getLong("Type") != 1) {
                                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }));
                    }
                }));
            }
        }));
    }
    void Cshow(String An){ // 눌렀을 때 자리 "Usable"이면 다이얼로그 표시, 그 외 "이용 중", 예약하면 Using, Reserving으로 변경

        int[] Allcount = new int[1];
        int[] CCount = new int[1];

        DocumentReference docRef = db.collection("Beanbag").document(An);
        DocumentReference userRef = db.collection("User").document(user.getUid());
        DocumentReference allcountRef = db.collection("Beanbag").document("AllCount");
        DocumentReference acountRef = db.collection("Beanbag").document("CCount");
        docRef.get().addOnCompleteListener((task1 -> {
            if(task1.isSuccessful()){
                DocumentSnapshot document = task1.getResult();
                allcountRef.get().addOnCompleteListener((task2 -> {
                    if(task2.isSuccessful()){
                        DocumentSnapshot allcount = task2.getResult();
                        acountRef.get().addOnCompleteListener((task3 -> {
                            if(task3.isSuccessful()){
                                DocumentSnapshot ccount = task3.getResult();
                                if(document!=null && document.exists()){
                                    if(document.getLong("Type") == 1){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                        builder.setTitle("예약 확인");
                                        builder.setMessage("자리를 예약 하시겠습니까?");
                                        builder.setNegativeButton("예",
                                                new DialogInterface.OnClickListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        docRef.update("Type", 2);
                                                        userRef.update("type", 2);
                                                        userRef.update("SeatNum", An);
                                                        CCount[0] = Math.toIntExact(ccount.getLong("CCount"));
                                                        acountRef.update("CCount", --CCount[0]);
                                                        Allcount[0] = Math.toIntExact(allcount.getLong("AllCount"));
                                                        allcountRef.update("AllCount", --Allcount[0]);
                                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
                                                        Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                                                        startActivity(intentToAfter);
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
                                    } else if(document.getLong("Type") != 1) {
                                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }));
                    }
                }));
            }
        }));
    }
}
