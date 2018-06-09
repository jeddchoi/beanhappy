package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    // Views
    private ListView mListView;
    private EditText mEdtMessage;
    // Values
    private ChatAdapter mAdapter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findViewById(R.id.chat_ruleBtn).setOnClickListener(this);
        findViewById(R.id.chat_mapBtn).setOnClickListener(this);
        findViewById(R.id.chat_backBtn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
        initFirebaseDatabase();
        initValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mChildEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void initViews() {
        mListView = (ListView) findViewById(R.id.list_message);
        mAdapter = new ChatAdapter(this, 0);
        mListView.setAdapter(mAdapter);

        mEdtMessage = (EditText) findViewById(R.id.edit_message);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    private void initFirebaseDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("message");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
                mAdapter.add(chatData);
                mListView.smoothScrollToPosition(mAdapter.getCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                        mAdapter.remove(mAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    private void initValues() {
        userName = "Guest" + new Random().nextInt(5000);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.chat_ruleBtn) {
            Intent intentToShowrule = new Intent(getApplicationContext(), Showrule.class);
            startActivity(intentToShowrule);
        }
        else if (i == R.id.chat_mapBtn) {
            Intent intentToshowmap = new Intent(getApplicationContext(), Showmap.class);
            startActivity(intentToshowmap);
        }
        else if (i == R.id.chat_backBtn) {
            Intent intentToBack = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentToBack);
            finish();
        }
        else{
            String message = mEdtMessage.getText().toString();
            if (!TextUtils.isEmpty(message)) {
                mEdtMessage.setText("");
                ChatData chatData = new ChatData();
                chatData.userName = userName;
                chatData.message = message;
                chatData.time = System.currentTimeMillis();
                mDatabaseReference.push().setValue(chatData);
            }
        }
    }
}
