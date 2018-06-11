package edu.skku.se3.beanhappy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "BeanHappy";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean sentToSettings = false;


    private EditText email_edit,pw_edit; // 사용자 id, 사용자 pw
    private CheckBox autologin_ChkBox;
    private boolean loginChecked;
    private Context mContext = this;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DeviceUuidFactory device;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private String TodayDate;
    private Date today;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_button; // 로그인 버튼
        Button register_button; //등록버튼

        email_edit = findViewById(R.id.email_edit);
        pw_edit = findViewById(R.id.pw_edit);
        login_button = findViewById(R.id.email_sign_in_button);
        register_button = findViewById(R.id.email_sign_up_button);

        autologin_ChkBox = findViewById(R.id.autologinChk);
        mAuth = FirebaseAuth.getInstance();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        device = new DeviceUuidFactory(this);


        /* -- 회원가입 클릭시 -- */
        register_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.putExtra("email", email_edit.getText().toString()); // 아이디, 비밀번호
            intent.putExtra("password", pw_edit.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        /* -- 로그인 클릭시 -- */
        login_button.setOnClickListener(v -> {
            try {
                attemptLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /* -- 자동로그인 체크박스 클릭시 -- */
        autologin_ChkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                loginChecked = true;
            } else {
                // if unChecked, removeAll
                loginChecked = false;
                editor.clear();
                editor.apply();
            }
        });

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        today = new Date();
        TodayDate = date.format(today);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    LoginActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (pref.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        if(email != null)
            email_edit.setText(email);

        if(password != null)
            pw_edit.setText(password);


        if (pref.getBoolean("autoLogin", false)) {
            email_edit.setText(pref.getString("id", ""));
            pw_edit.setText(pref.getString("pw", ""));
            autologin_ChkBox.setChecked(true);

            signIn(email_edit.getText().toString(), pw_edit.getText().toString());

        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    private void attemptLogin() {

        // Reset errors.
        email_edit.setError(null);
        pw_edit.setError(null);

        // Store values at the time of the login attempt.
        String email = email_edit.getText().toString();
        String password = pw_edit.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            pw_edit.setError(getString(R.string.error_invalid_password_register));
            focusView = pw_edit;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            email_edit.setError(getString(R.string.error_field_required));
            focusView = email_edit;
            cancel = true;
        } else if (!isEmailValid(email)) {
            email_edit.setError(getString(R.string.error_invalid_email));
            focusView = email_edit;
            cancel = true;
        }


        if(IsTablet(mContext)) {
            Toast.makeText(LoginActivity.this, R.string.reject_tablet, Toast.LENGTH_LONG).show();
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if(loginChecked) {
                // if autoLogin Checked, save values
                editor.putString("id", email);
                editor.putString("pw", password);
                editor.putBoolean("autoLogin", true);
                editor.commit();
            }

            signIn(email, password);
        }
    }


    private boolean isEmailValid(String email) {

        String regex = "^[_a-zA-Z0-9-.]+@skku.edu";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    private boolean isPasswordValid(String password) {

        String regex = "^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$"; // 8자리 ~ 16자리까지 가능
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);

        return m.matches();
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Check for a email verification.
                        assert user != null;
                        if(!user.isEmailVerified())
                        {
                            Toast.makeText(getApplicationContext(), R.string.error_please_verify, Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            return;
                        }

                        String uuid = device.getDeviceUuid().toString();

                        mRootRef.child("users");
                        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(uuid) && !email.equals(dataSnapshot.child(uuid).child("email").getValue(String.class))) {
                                    Toast.makeText(LoginActivity.this, R.string.reject_bad_uuid, Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "mRootRef_" + dataSnapshot.child(uuid).child("email").getValue(String.class));
                                } else {
                                    writeNewUser(uuid, email);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
        // [END sign_in_with_email]
    }

    public static boolean IsTablet(Context context)
    {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        double wInches = displayMetrics.widthPixels / (double)displayMetrics.densityDpi;
        double hInches = displayMetrics.heightPixels / (double)displayMetrics.densityDpi;

        double screenDiagonal = Math.sqrt(Math.pow(wInches, 2) + Math.pow(hInches, 2));
        return (screenDiagonal >= 7.0);
    }

    private void writeNewUser(String userId, String email) {
        mRootRef.child("users").child(TodayDate).child(userId).child("email").setValue(email);
        mRootRef.child("users").child(TodayDate).child(userId).child("uuid").setValue(userId);
        mRootRef.child("users").child(TodayDate).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("status")){
                    mRootRef.child("users").child(TodayDate).child(userId).child("status").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[2])){
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}

