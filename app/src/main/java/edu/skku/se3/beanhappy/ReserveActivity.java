package edu.skku.se3.beanhappy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ReserveActivity extends BaseActivity implements
        View.OnClickListener {

    public static final String TAG = "BeanHappy";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private DatabaseReference mA = mRootRef.child("bb_A");
    private DatabaseReference mA1 = mA.child("bb_A1"); private DatabaseReference mA2 = mA.child("bb_A2");
    private DatabaseReference mA3 = mA.child("bb_A3"); private DatabaseReference mA4 = mA.child("bb_A4");

    private DatabaseReference mB = mRootRef.child("bb_B");
    private DatabaseReference mB1 = mB.child("bb_B1"); private DatabaseReference mB2 = mB.child("bb_B2"); private DatabaseReference mB3 = mB.child("bb_B3");
    private DatabaseReference mB4 = mB.child("bb_B4"); private DatabaseReference mB5 = mB.child("bb_B5"); private DatabaseReference mB6 = mB.child("bb_B6");
    private DatabaseReference mB7 = mB.child("bb_B7"); private DatabaseReference mB8 = mB.child("bb_B8");

    private DatabaseReference mC = mRootRef.child("bb_C");
    private DatabaseReference mC1 = mB.child("bb_C1"); private DatabaseReference mC2 = mB.child("bb_C2"); private DatabaseReference mC3 = mB.child("bb_C3");
    private DatabaseReference mC4 = mB.child("bb_C4"); private DatabaseReference mC5 = mB.child("bb_C5");

    private DatabaseReference mD = mRootRef.child("bb_D");
    private DatabaseReference mD1 = mB.child("bb_D1"); private DatabaseReference mD2 = mB.child("bb_D2"); private DatabaseReference mD3 = mB.child("bb_D3");
    private DatabaseReference mD4 = mB.child("bb_D4"); private DatabaseReference mD5 = mB.child("bb_D5"); private DatabaseReference mD6 = mB.child("bb_D6");

    private DatabaseReference mE = mRootRef.child("bb_E");
    private DatabaseReference mE1 = mB.child("bb_E1"); private DatabaseReference mE2 = mB.child("bb_E2");
    private DatabaseReference mE3 = mB.child("bb_E3"); private DatabaseReference mE4 = mB.child("bb_E4");

    private DeviceUuidFactory device;
    private String uuid;
    private DatabaseReference currentUser;

    TextView A_AvailTextView, B_AvailTextView, C_AvailTextView, D_AvailTextView, E_AvailTextView;
    ImageButton bA1Btn, bA2Btn, bA3Btn, bA4Btn;
    ImageButton bB1Btn, bB2Btn, bB3Btn, bB4Btn, bB5Btn, bB6Btn, bB7Btn, bB8Btn;
    ImageButton bC1Btn, bC2Btn, bC3Btn, bC4Btn, bC5Btn;
    ImageButton bD1Btn, bD2Btn, bD3Btn, bD4Btn, bD5Btn, bD6Btn;
    ImageButton bE1Btn, bE2Btn, bE3Btn, bE4Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Textviews
        A_AvailTextView = findViewById(R.id.A_AvailTextView);
        B_AvailTextView = findViewById(R.id.B_AvailTextView);
        C_AvailTextView = findViewById(R.id.C_AvailTextView);
        D_AvailTextView = findViewById(R.id.D_AvailTextView);
        E_AvailTextView = findViewById(R.id.E_AvailTextView);

        // Buttons
        bA1Btn = (findViewById(R.id.bA1)); bA2Btn = (findViewById(R.id.bA2)); bA3Btn = (findViewById(R.id.bA3)); bA4Btn = (findViewById(R.id.bA4));
        bB1Btn = (findViewById(R.id.bB1)); bB2Btn = (findViewById(R.id.bB2)); bB3Btn = (findViewById(R.id.bB3)); bB4Btn = (findViewById(R.id.bB4));
        bB5Btn = (findViewById(R.id.bB5)); bB6Btn = (findViewById(R.id.bB6)); bB7Btn = (findViewById(R.id.bB7)); bB8Btn = (findViewById(R.id.bB8));
        bC1Btn = (findViewById(R.id.bC1)); bC2Btn = (findViewById(R.id.bC2)); bC3Btn = (findViewById(R.id.bC3)); bC4Btn = (findViewById(R.id.bC4));
        bC5Btn = (findViewById(R.id.bC5));
        bD1Btn = (findViewById(R.id.bD1)); bD2Btn = (findViewById(R.id.bD2)); bD3Btn = (findViewById(R.id.bD3)); bD4Btn = (findViewById(R.id.bD4));
        bD5Btn = (findViewById(R.id.bD5)); bD6Btn = (findViewById(R.id.bD6));
        bE1Btn = (findViewById(R.id.bE1)); bE2Btn = (findViewById(R.id.bE2)); bE3Btn = (findViewById(R.id.bE3)); bE4Btn = (findViewById(R.id.bE4));

        bA1Btn.setOnClickListener(this); bA2Btn.setOnClickListener(this); bA3Btn.setOnClickListener(this); bA4Btn.setOnClickListener(this);
        bB1Btn.setOnClickListener(this); bB2Btn.setOnClickListener(this); bB3Btn.setOnClickListener(this); bB4Btn.setOnClickListener(this);
        bB5Btn.setOnClickListener(this); bB6Btn.setOnClickListener(this); bB7Btn.setOnClickListener(this); bB8Btn.setOnClickListener(this);
        bC1Btn.setOnClickListener(this); bC2Btn.setOnClickListener(this); bC3Btn.setOnClickListener(this); bC4Btn.setOnClickListener(this);
        bC5Btn.setOnClickListener(this);
        bD1Btn.setOnClickListener(this); bD2Btn.setOnClickListener(this); bD3Btn.setOnClickListener(this); bD4Btn.setOnClickListener(this);
        bD5Btn.setOnClickListener(this); bD6Btn.setOnClickListener(this);
        bE1Btn.setOnClickListener(this); bE2Btn.setOnClickListener(this); bE3Btn.setOnClickListener(this); bE4Btn.setOnClickListener(this);

        device = new DeviceUuidFactory(this);
        uuid = device.getDeviceUuid().toString();

    }


    @Override
    protected void onStart() {
        super.onStart();

        mA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "datasnapshot.getchildren key = " + ds.getKey());
                    if(ds.getKey().equals("bb_NumAvail"))
                        continue;
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    assert map != null;
                    Object count = map.get("state");
                    if(Integer.parseInt(String.valueOf(count)) == 0)
                        sum += 1;
                }
                mA.child("bb_NumAvail").setValue(sum);
                String buf = String.format(getString(R.string.A_AvailNum), sum);
                A_AvailTextView.setText(buf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "datasnapshot.getchildren key = " + ds.getKey());
                    if(ds.getKey().equals("bb_NumAvail"))
                        continue;
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    assert map != null;
                    Object count = map.get("state");
                    if(Integer.parseInt(String.valueOf(count)) == 0)
                        sum += 1;
                }
                mB.child("bb_NumAvail").setValue(sum);
                String buf = String.format(getString(R.string.B_AvailNum), sum);
                B_AvailTextView.setText(buf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "datasnapshot.getchildren key = " + ds.getKey());
                    if(ds.getKey().equals("bb_NumAvail"))
                        continue;
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    assert map != null;
                    Object count = map.get("state");
                    if(Integer.parseInt(String.valueOf(count)) == 0)
                        sum += 1;
                }
                mC.child("bb_NumAvail").setValue(sum);
                String buf = String.format(getString(R.string.C_AvailNum), sum);
                C_AvailTextView.setText(buf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "datasnapshot.getchildren key = " + ds.getKey());
                    if(ds.getKey().equals("bb_NumAvail"))
                        continue;
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    assert map != null;
                    Object count = map.get("state");
                    if(Integer.parseInt(String.valueOf(count)) == 0)
                        sum += 1;
                }
                mD.child("bb_NumAvail").setValue(sum);
                String buf = String.format(getString(R.string.D_AvailNum), sum);
                D_AvailTextView.setText(buf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "datasnapshot.getchildren key = " + ds.getKey());
                    if(ds.getKey().equals("bb_NumAvail"))
                        continue;
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    assert map != null;
                    Object count = map.get("state");
                    if(Integer.parseInt(String.valueOf(count)) == 0)
                        sum += 1;
                }
                mE.child("bb_NumAvail").setValue(sum);
                String buf = String.format(getString(R.string.E_AvailNum), sum);
                E_AvailTextView.setText(buf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });


        mA1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bA1Btn.setClickable(true);
                    bA1Btn.setImageResource(R.drawable.a1);
                } else {
                    bA1Btn.setClickable(false);
                    bA1Btn.setImageResource(R.drawable.a1_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mA2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bA2Btn.setClickable(true);
                    bA2Btn.setImageResource(R.drawable.a2);
                } else {
                    bA2Btn.setClickable(false);
                    bA2Btn.setImageResource(R.drawable.a2_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mA3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bA3Btn.setClickable(true);
                    bA3Btn.setImageResource(R.drawable.a3);
                } else {
                    bA3Btn.setClickable(false);
                    bA3Btn.setImageResource(R.drawable.a3_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mA4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bA4Btn.setClickable(true);
                    bA4Btn.setImageResource(R.drawable.a4);
                } else {
                    bA4Btn.setClickable(false);
                    bA4Btn.setImageResource(R.drawable.a4_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB1Btn.setClickable(true);
                    bB1Btn.setImageResource(R.drawable.b1);
                } else {
                    bB1Btn.setClickable(false);
                    bB1Btn.setImageResource(R.drawable.b1_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB2Btn.setClickable(true);
                    bB2Btn.setImageResource(R.drawable.b2);
                } else {
                    bB2Btn.setClickable(false);
                    bB2Btn.setImageResource(R.drawable.b2_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB3Btn.setClickable(true);
                    bB3Btn.setImageResource(R.drawable.b3);
                } else {
                    bB3Btn.setClickable(false);
                    bB3Btn.setImageResource(R.drawable.b3_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB4Btn.setClickable(true);
                    bB4Btn.setImageResource(R.drawable.b4);
                } else {
                    bB4Btn.setClickable(false);
                    bB4Btn.setImageResource(R.drawable.b4_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB5Btn.setClickable(true);
                    bB5Btn.setImageResource(R.drawable.b5);
                } else {
                    bB5Btn.setClickable(false);
                    bB5Btn.setImageResource(R.drawable.b5_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB6Btn.setClickable(true);
                    bB6Btn.setImageResource(R.drawable.b6);
                } else {
                    bB6Btn.setClickable(false);
                    bB6Btn.setImageResource(R.drawable.b6_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bB7Btn.setClickable(true);
                    bB7Btn.setImageResource(R.drawable.b7);
                } else {
                    bB7Btn.setClickable(false);
                    bB7Btn.setImageResource(R.drawable.b7_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mB8.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);

                if (state == null || state < 1) {
                    bB8Btn.setClickable(true);
                    bB8Btn.setImageResource(R.drawable.b8);
                } else {
                    bB8Btn.setClickable(false);
                    bB8Btn.setImageResource(R.drawable.b8_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mC1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);

                if (state == null || state < 1) {
                    bC1Btn.setClickable(true);
                    bC1Btn.setImageResource(R.drawable.c1);
                } else {
                    bC1Btn.setClickable(false);
                    bC1Btn.setImageResource(R.drawable.c1_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mC2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bC2Btn.setClickable(true);
                    bC2Btn.setImageResource(R.drawable.c2);
                } else {
                    bC2Btn.setClickable(false);
                    bC2Btn.setImageResource(R.drawable.c2_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mC3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bC3Btn.setClickable(true);
                    bC3Btn.setImageResource(R.drawable.c3);
                } else {
                    bC3Btn.setClickable(false);
                    bC3Btn.setImageResource(R.drawable.c3_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mC4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bC4Btn.setClickable(true);
                    bC4Btn.setImageResource(R.drawable.c4);
                } else {
                    bC4Btn.setClickable(false);
                    bC4Btn.setImageResource(R.drawable.c4_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mC5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bC5Btn.setClickable(true);
                    bC5Btn.setImageResource(R.drawable.c5);
                } else {
                    bC5Btn.setClickable(false);
                    bC5Btn.setImageResource(R.drawable.c5_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bD1Btn.setClickable(true);
                    bD1Btn.setImageResource(R.drawable.d1);
                } else {
                    bD1Btn.setClickable(false);
                    bD1Btn.setImageResource(R.drawable.d1_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bD2Btn.setClickable(true);
                    bD2Btn.setImageResource(R.drawable.d2);
                } else {
                    bD2Btn.setClickable(false);
                    bD2Btn.setImageResource(R.drawable.d2_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bD3Btn.setClickable(true);
                    bD3Btn.setImageResource(R.drawable.d3);
                } else {
                    bD3Btn.setClickable(false);
                    bD3Btn.setImageResource(R.drawable.d3_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bD4Btn.setClickable(true);
                    bD4Btn.setImageResource(R.drawable.d4);
                } else {
                    bD4Btn.setClickable(false);
                    bD4Btn.setImageResource(R.drawable.d4_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bD5Btn.setClickable(true);
                    bD5Btn.setImageResource(R.drawable.d5);
                } else {
                    bD5Btn.setClickable(false);
                    bD5Btn.setImageResource(R.drawable.d5_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mD6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bD6Btn.setClickable(true);
                    bD6Btn.setImageResource(R.drawable.d6);
                } else {
                    bD6Btn.setClickable(false);
                    bD6Btn.setImageResource(R.drawable.d6_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mE1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bE1Btn.setClickable(true);
                    bE1Btn.setImageResource(R.drawable.e1);
                } else {
                    bE1Btn.setClickable(false);
                    bE1Btn.setImageResource(R.drawable.e1_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mE2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bE2Btn.setClickable(true);
                    bE2Btn.setImageResource(R.drawable.e2);
                } else {
                    bE2Btn.setClickable(false);
                    bE2Btn.setImageResource(R.drawable.e2_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mE3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bE3Btn.setClickable(true);
                    bE3Btn.setImageResource(R.drawable.e3);
                } else {
                    bE3Btn.setClickable(false);
                    bE3Btn.setImageResource(R.drawable.e3_occupied);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        mE4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long state = dataSnapshot.child("state").getValue(Long.class);
                if (state == null || state < 1) {
                    bE4Btn.setClickable(true);
                    bE4Btn.setImageResource(R.drawable.e4);
                } else {
                    bE4Btn.setClickable(false);
                    bE4Btn.setImageResource(R.drawable.e4_occupied);
                }
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
        currentUser = mRootRef.child("users").child(uuid).getRef();
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "currentUser = " + ds.getKey() + ds.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch(i) {
            case R.id.bA1 :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("예약 확인");
                builder.setMessage("자리를 예약 하시겠습니까?");
                builder.setNegativeButton("예",
                        (dialog, which) -> {
                            mA1.child("state").setValue(2);


                            Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                            intentToAfter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intentToAfter);
                            finish();
                        });
                builder.setPositiveButton("아니요",
                        (dialog, which) -> Toast.makeText(getApplicationContext(), "자리 예약이 취소되었습니다.", Toast.LENGTH_LONG).show());
                builder.show();
                break;
            case R.id.bA2 :
                break;
        }
    }
    //
//    void Ashow(String An){ // 눌렀을 때 자리 "Usable"이면 다이얼로그 표시, 그 외 "이용 중", 예약하면 Using, Reserving으로 변경
//

//        int[] Allcount = new int[1];
//        int[] ACount = new int[1];
//
//        DocumentReference docRef = db.collection("Beanbag").document(An);
//        DocumentReference userRef = db.collection("User").document(user.getUid());
//        DocumentReference allcountRef = db.collection("Beanbag").document("AllCount");
//        DocumentReference acountRef = db.collection("Beanbag").document("ACount");
//        docRef.get().addOnCompleteListener((task1 -> {
//            if(task1.isSuccessful()){
//                DocumentSnapshot document = task1.getResult();
//                allcountRef.get().addOnCompleteListener((task2 -> {
//                    if(task2.isSuccessful()){
//                        DocumentSnapshot allcount = task2.getResult();
//                        acountRef.get().addOnCompleteListener((task3 -> {
//                            if(task3.isSuccessful()){
//                                DocumentSnapshot acount = task3.getResult();
//                                if(document!=null && document.exists()){
//                                    if(document.getLong("Type") == 1){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                                        builder.setTitle("예약 확인");
//                                        builder.setMessage("자리를 예약 하시겠습니까?");
//                                        builder.setNegativeButton("예",
//                                                new DialogInterface.OnClickListener() {
//                                                    @RequiresApi(api = Build.VERSION_CODES.N)
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        docRef.update("Type", 2);
//                                                        userRef.update("type", 2);
//                                                        userRef.update("SeatNum", An);
//                                                        ACount[0] = Math.toIntExact(acount.getLong("ACount"));
//                                                        acountRef.update("ACount", --ACount[0]);
//                                                        Allcount[0] = Math.toIntExact(allcount.getLong("AllCount"));
//                                                        allcountRef.update("AllCount", --Allcount[0]);
//                                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
//                                                        Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
//                                                        startActivity(intentToAfter);
//                                                    }
//                                                });
//                                        builder.setPositiveButton("아니요",
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        Toast.makeText(getApplicationContext(), "자리 예약이 취소되었습니다.", Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                        builder.show();
//                                    } else if(document.getLong("Type") != 1) {
//                                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//                        }));
//                    }
//                }));
//            }
//        }));
//    }
//
//
//    void Bshow(String An){ // 눌렀을 때 자리 "Usable"이면 다이얼로그 표시, 그 외 "이용 중", 예약하면 Using, Reserving으로 변경
//
//        int[] Allcount = new int[1];
//        int[] BCount = new int[1];
//
//        DocumentReference docRef = db.collection("Beanbag").document(An);
//        DocumentReference userRef = db.collection("User").document(user.getUid());
//        DocumentReference allcountRef = db.collection("Beanbag").document("AllCount");
//        DocumentReference acountRef = db.collection("Beanbag").document("BCount");
//        docRef.get().addOnCompleteListener((task1 -> {
//            if(task1.isSuccessful()){
//                DocumentSnapshot document = task1.getResult();
//                allcountRef.get().addOnCompleteListener((task2 -> {
//                    if(task2.isSuccessful()){
//                        DocumentSnapshot allcount = task2.getResult();
//                        acountRef.get().addOnCompleteListener((task3 -> {
//                            if(task3.isSuccessful()){
//                                DocumentSnapshot bcount = task3.getResult();
//                                if(document!=null && document.exists()){
//                                    if(document.getLong("Type") == 1){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                                        builder.setTitle("예약 확인");
//                                        builder.setMessage("자리를 예약 하시겠습니까?");
//                                        builder.setNegativeButton("예",
//                                                new DialogInterface.OnClickListener() {
//                                                    @RequiresApi(api = Build.VERSION_CODES.N)
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        docRef.update("Type", 2);
//                                                        userRef.update("type", 2);
//                                                        userRef.update("SeatNum", An);
//                                                        BCount[0] = Math.toIntExact(bcount.getLong("BCount"));
//                                                        acountRef.update("BCount", --BCount[0]);
//                                                        Allcount[0] = Math.toIntExact(allcount.getLong("AllCount"));
//                                                        allcountRef.update("AllCount", --Allcount[0]);
//                                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
//                                                        Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
//                                                        startActivity(intentToAfter);
//                                                    }
//                                                });
//                                        builder.setPositiveButton("아니요",
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        Toast.makeText(getApplicationContext(), "자리 예약이 취소되었습니다.", Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                        builder.show();
//                                    } else if(document.getLong("Type") != 1) {
//                                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//                        }));
//                    }
//                }));
//            }
//        }));
//    }
//    void Cshow(String An){ // 눌렀을 때 자리 "Usable"이면 다이얼로그 표시, 그 외 "이용 중", 예약하면 Using, Reserving으로 변경
//
//        int[] Allcount = new int[1];
//        int[] CCount = new int[1];
//
//        DocumentReference docRef = db.collection("Beanbag").document(An);
//        DocumentReference userRef = db.collection("User").document(user.getUid());
//        DocumentReference allcountRef = db.collection("Beanbag").document("AllCount");
//        DocumentReference acountRef = db.collection("Beanbag").document("CCount");
//        docRef.get().addOnCompleteListener((task1 -> {
//            if(task1.isSuccessful()){
//                DocumentSnapshot document = task1.getResult();
//                allcountRef.get().addOnCompleteListener((task2 -> {
//                    if(task2.isSuccessful()){
//                        DocumentSnapshot allcount = task2.getResult();
//                        acountRef.get().addOnCompleteListener((task3 -> {
//                            if(task3.isSuccessful()){
//                                DocumentSnapshot ccount = task3.getResult();
//                                if(document!=null && document.exists()){
//                                    if(document.getLong("Type") == 1){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                                        builder.setTitle("예약 확인");
//                                        builder.setMessage("자리를 예약 하시겠습니까?");
//                                        builder.setNegativeButton("예",
//                                                new DialogInterface.OnClickListener() {
//                                                    @RequiresApi(api = Build.VERSION_CODES.N)
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        docRef.update("Type", 2);
//                                                        userRef.update("type", 2);
//                                                        userRef.update("SeatNum", An);
//                                                        CCount[0] = Math.toIntExact(ccount.getLong("CCount"));
//                                                        acountRef.update("CCount", --CCount[0]);
//                                                        Allcount[0] = Math.toIntExact(allcount.getLong("AllCount"));
//                                                        allcountRef.update("AllCount", --Allcount[0]);
//                                                        Toast.makeText(getApplicationContext(), "자리가 예약되었습니다.", Toast.LENGTH_LONG).show();
//                                                        Intent intentToAfter = new Intent(getApplicationContext(), AfterRegisterActivity.class);
//                                                        startActivity(intentToAfter);
//                                                    }
//                                                });
//                                        builder.setPositiveButton("아니요",
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        Toast.makeText(getApplicationContext(), "자리 예약이 취소되었습니다.", Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                        builder.show();
//                                    } else if(document.getLong("Type") != 1) {
//                                        Toast.makeText(getApplicationContext(), "이미 자리가 사용중입니다.", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//                        }));
//                    }
//                }));
//            }
//        }));
//    }
}
