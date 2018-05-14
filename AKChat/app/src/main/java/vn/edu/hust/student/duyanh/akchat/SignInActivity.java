package vn.edu.hust.student.duyanh.akchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import controllers.Utils;
import controllers.Validate;
import models.User;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout edit_email, edit_password;
    private Button bt_sign_in, sig_by_facebook, sig_by_google;
    private FirebaseAuth mAuth;
    private ProgressBar mRegProgressBar;
    private Toolbar mToolbar;
    private RelativeLayout sign_in_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edit_email = (TextInputLayout) findViewById(R.id.edit_email);
        edit_password = (TextInputLayout) findViewById(R.id.edit_password);
        bt_sign_in = (Button) findViewById(R.id.bt_sign_in);
        sig_by_facebook = (Button) findViewById(R.id.sig_by_facebook);
        sig_by_google = (Button) findViewById(R.id.sig_by_google);
        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        sign_in_layout = (RelativeLayout) findViewById(R.id.sign_in_layout);
        mRegProgressBar = (ProgressBar) findViewById(R.id.sig_progress_bar);
        mRegProgressBar.setVisibility(View.INVISIBLE);


        findViewById(R.id.sig_txt_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_up_intent = new Intent(SignInActivity.this, RegisterActivity.class);
                mRegProgressBar.setVisibility(View.VISIBLE);
                startActivity(sign_up_intent);
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.common_google_signin_btn_icon_dark);

        sign_in_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideKeyBoard(SignInActivity.this);
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();

        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edit_email.getEditText().getText().toString().trim();
                final String pass = edit_password.getEditText().getText().toString();
                if(!Validate.validateEmail(email)) {
                    edit_email.setError("Invalid email. Please enter again.");
                    edit_email.getEditText().setText("");
                } else if (!Validate.validatePassword(pass)) {
                    edit_password.setError("Invalid password. Please enter again.");

                } else {
                    signIn(email, pass);
//                    edit_email.setErrorEnabled(false);
//                    edit_password.setErrorEnabled(false);
//                    SafetyNet.getClient(SignInActivity.this).verifyWithRecaptcha("6Lf9IE8UAAAAANtRM4UJLbCQjtPUa029le_tp__m")
//                            .addOnSuccessListener(SignInActivity.this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
//                                @Override
//                                public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
//                                    if (!response.getTokenResult().isEmpty()) {
//                                        signIn(email, pass);
//                                    }
//                                }
//                            })
//                            .addOnFailureListener(SignInActivity.this, new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    if (e instanceof ApiException) {
//                                        ApiException apiException = (ApiException) e;
//                                        Log.d("ERROR_CAPTCHA", "Error message: " + CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
//                                    } else {
//                                        Log.d("ERROR_CAPTCHA", "Unknown type of error: " + e.getMessage());
//                                    }
//                                }
//                            });
                }
            }
        });
    }

    private void signIn(String email, String pass) {
        final User user = new User(email, pass, "");
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    mRegProgressBar.setVisibility(View.VISIBLE);
                    Intent main_intent = new Intent(SignInActivity.this, HomePageActivity.class);
                    main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main_intent);
                    finish();
                } else
                    Toast.makeText(SignInActivity.this, "Sign in not successful.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
