package edu.skku.se3.beanhappy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "BeanHappy";

    private EditText email_edit,pw_edit; // 사용자 id, 사용자 pw
    private CheckBox autologin_ChkBox;
    private boolean loginChecked;

    private Button login_button; // 로그인 버튼
    private Button register_button; //등록버튼
    private FirebaseAuth mAuth;


//    SharedPreferences pref;
//    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edit = (EditText)findViewById(R.id.email_edit);
        pw_edit = (EditText)findViewById(R.id.pw_edit);
        login_button = (Button)findViewById(R.id.email_sign_in_button);
        register_button = (Button)findViewById(R.id.email_sign_up_button);

        autologin_ChkBox = (CheckBox)findViewById(R.id.autologinChk);
        mAuth = FirebaseAuth.getInstance();

//        pref = getSharedPreferences("pref", 0);
//        editor = pref.edit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
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

        /* -- 회원가입 클릭시 -- */
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("email", email_edit.getText().toString()); // 아이디, 비밀번호
                intent.putExtra("password", pw_edit.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        /* -- 로그인 클릭시 -- */
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        /* -- 자동로그인 체크박스 클릭시 -- */
//        autologin_ChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked) {
//                    loginChecked = true;
//                } else {
//                    // if unChecked, removeAll
//                    loginChecked = false;
//                    editor.clear();
//                    editor.commit();
//                }
//            }
//        });
//
//        if (pref.getBoolean("autoLogin", false)) {
//            email_edit.setText(pref.getString("id", ""));
//            pw_edit.setText(pref.getString("pw", ""));
//            autologin_ChkBox.setChecked(true);
//            // goto mainActivity
//            showProgress(true);
//            signIn(email_edit.getText().toString(), pw_edit.getText().toString());
//
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//
//        } else {
//            // if autoLogin unChecked
//            String id = email_edit.getText().toString();
//            String password = pw_edit.getText().toString();
//            Boolean validation = loginValidation(id, password);
//
//            if(validation) {
//                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
//                // save id, password to Database
//
//                if(loginChecked) {
//                    // if autoLogin Checked, save values
//                    editor.putString("id", id);
//                    editor.putString("pw", password);
//                    editor.putBoolean("autoLogin", true);
//                    editor.commit();
//                }
//                // goto mainActivity
//                showProgress(true);
//                signIn(email_edit.getText().toString(), pw_edit.getText().toString());
//
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
//                // goto LoginActivity
//            }
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            signIn(email, password);
        }
    }

    private boolean isEmailValid(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@skku.edu";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();

        return isNormal;
    }

    private boolean isPasswordValid(String password) {

        String regex = "^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$"; // 8자리 ~ 16자리까지 가능
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean isNormal = m.matches();

        return isNormal;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Check for a email verification.
                            if(!user.isEmailVerified())
                            {
                                Toast.makeText(getApplicationContext(), R.string.error_please_verify, Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                                return;
                            }

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), R.string.auth_failed, Toast.LENGTH_LONG).show();
//                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
}

