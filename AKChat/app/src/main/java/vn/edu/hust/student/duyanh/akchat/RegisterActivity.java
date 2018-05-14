package vn.edu.hust.student.duyanh.akchat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import controllers.Validate;
import models.PersonalInformation;
import models.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mConfirmPassword;
    private Button btnSignUp;
    private TextView mTxtExistAccount;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_app_sidebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create New Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mConfirmPassword = (TextInputLayout) findViewById(R.id.reg_confirm_password);
        btnSignUp = (Button) findViewById(R.id.btn_reg);
        mTxtExistAccount = (TextView) findViewById(R.id.reg_exist_account);
        progressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        mTxtExistAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent sing_in_intent = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(sing_in_intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String display_name = mDisplayName.getEditText().getText().toString();
                final String email = mEmail.getEditText().getText().toString();
                final String password = mPassword.getEditText().getText().toString();
                final String confirm_password = mConfirmPassword.getEditText().getText().toString();
                if(!password.equals(confirm_password)) {
                    mPassword.getEditText().setText("");
                    mConfirmPassword.getEditText().setText("");
                    mConfirmPassword.setError("Confirm password and password are not equal");
                } else if(password.length() < 8 || password.length() > 30) {
                    mPassword.setError("Password length from 8 to 30");
                } else if(!Validate.validateEmail(email)) {
                    mEmail.setError("Invalid email");
                } else if(!Validate.validatePassword(password)) {
                    mPassword.setError("Invalid password.");
                } else if(!Validate.validatePassword(confirm_password)) {
                    mConfirmPassword.setError("Invalid confirm password.");
                } else {
                    mEmail.setErrorEnabled(false);
                    mPassword.setErrorEnabled(false);
                    mConfirmPassword.setErrorEnabled(false);
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Create new Account")
                            .setMessage("Do you want to continue?")
                            .setIcon(R.drawable.create)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ProgressDialog pd =  new ProgressDialog(RegisterActivity.this);
                                    pd.setMessage("Signing up ...");
                                    pd.show();
                                    createAccount(display_name, email, password);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new AlertDialog.Builder(RegisterActivity.this)
                                            .setTitle("Cancel to create new account")
                                            .setMessage("Are you sure to cancel?")
                                            .setIcon(R.drawable.cancel)
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(RegisterActivity.this, "You 've canceled to create new account.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ProgressDialog pd =  new ProgressDialog(RegisterActivity.this);
                                                    pd.setMessage("Signing up ...");
                                                    pd.show();
                                                    createAccount(display_name, email, password);
                                                }
                                            }).show();
                                }
                            }).show();
                }
            }
        });
    }
    private void createAccount(final String display_name, final String email, String password) {
        //them User vao db

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    User newUser = new User();
                    newUser.setUserId(mAuth.getCurrentUser().getUid());
                    newUser.setDisplay_name(display_name);
                    newUser.setEmail(email);
                    PersonalInformation pi = new PersonalInformation(true);
                    newUser.setPi(pi);
//                    newUser.setPi(new PersonalInformation());

                    writeNewUser(newUser.getUserId(), newUser);
                }
                else
                    Toast.makeText(RegisterActivity.this, "Error to create a new account.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.reg_exit_menu) {
            finish();
        }
        return true;
    }
    //write a new user to firebase database
    private void writeNewUser(String userId, User newUser) {
        mDatabaseReference = mDatabase.getReference().child("Users").child(userId);
        mDatabaseReference.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent sign_in_intent = new Intent(RegisterActivity.this, SignInActivity.class);
                    sign_in_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(sign_in_intent);
                    finish();
                }
            }
        });
    }
}
