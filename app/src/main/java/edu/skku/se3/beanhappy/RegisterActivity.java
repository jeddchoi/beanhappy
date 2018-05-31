package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class RegisterActivity extends BaseActivity implements
        View.OnClickListener {

    public static final int REQUEST_CODE_AGREEMENT = 101;
    public static final String TAG = "BeanHappy";

    private EditText email_edit,pw_edit, pw_edit_check; //사용자 id,pw
    private CheckBox register_check; //checkbox
    private FirebaseAuth mAuth; //사용자 회원가입 정보 객체



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // EditTexts
        email_edit = (EditText)findViewById(R.id.register_email_edit);
        pw_edit = (EditText)findViewById(R.id.register_pw_edit);
        pw_edit_check = (EditText)findViewById(R.id.register_pw_edit_check);

        // CheckBox
        register_check = (CheckBox)findViewById(R.id.register_check);

        // Buttons
        findViewById(R.id.register_layout).setOnClickListener(this);
        findViewById(R.id.verify_button).setOnClickListener(this);
        findViewById(R.id.return_login_txt).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);

        // Firebase API
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();


        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        if(email != null)
            email_edit.setText(email);

        if(password != null)
            pw_edit.setText(password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void attemptRegister() {

        // Reset errors.
        email_edit.setError(null);
        pw_edit.setError(null);
        pw_edit_check.setError(null);

        // Store values at the time of the login attempt.
        String email = email_edit.getText().toString();
        String password = pw_edit.getText().toString();
        String passwordChk = pw_edit_check.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            pw_edit.setError(getString(R.string.error_invalid_password_register));
            focusView = pw_edit;
            cancel = true;
        }


        // Check whether password equals passwordChk.
        if(!TextUtils.equals(password, passwordChk)) {
            pw_edit_check.setError(getString(R.string.error_incorrect_passwordchk));
            focusView = pw_edit_check;
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

        // Check for an agreement for registeration.
        if(!register_check.isChecked()){
            Toast.makeText(getApplicationContext(), R.string.error_please_agree, Toast.LENGTH_SHORT).show();
            focusView = register_check;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            createAccount(email, password);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_AGREEMENT && resultCode == RESULT_OK) {
            register_check.setChecked(true);
        }
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.register_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.register_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }



    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

        if (user != null && !user.isEmailVerified()) {
            findViewById(R.id.register_button).setVisibility(GONE);
            findViewById(R.id.email_password_fields).setVisibility(GONE);
            findViewById(R.id.verify_button).setVisibility(VISIBLE);
            findViewById(R.id.register_layout).setVisibility(GONE);

            findViewById(R.id.verify_button).setEnabled(!user.isEmailVerified());
        } else {
            findViewById(R.id.register_button).setVisibility(VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(VISIBLE);
            findViewById(R.id.verify_button).setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_button) {
            attemptRegister();
        } else if (i == R.id.verify_button) {
            sendEmailVerification();
        } else if (i == R.id.return_login_txt) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (i == R.id.register_layout) {
            Intent intent = new Intent(getApplicationContext(), AgreementActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, REQUEST_CODE_AGREEMENT);
        }
    }
}
