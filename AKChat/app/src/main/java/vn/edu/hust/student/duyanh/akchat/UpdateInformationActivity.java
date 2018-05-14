package vn.edu.hust.student.duyanh.akchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import models.User;

public class UpdateInformationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button edit_save;
    private User editUser;
    private EditText edit_display_name, edit_description, edit_full_name, edit_addr, edit_hometown, edit_numberphone, edit_birthday, edit_zodiac;
    private ProgressDialog progressDialog;

    //firebase
    private FirebaseUser mCurrUser;
    private DatabaseReference mDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        progressDialog = new ProgressDialog(UpdateInformationActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editUser = new User(true);

        //firebase
        mCurrUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mCurrUser.getUid();
        mDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);


        //anh xa
        edit_display_name = (EditText) findViewById(R.id.edit_display_name);
        edit_description = (EditText) findViewById(R.id.edit_description);
        edit_save = (Button) findViewById(R.id.edit_save);
        edit_full_name = (EditText) findViewById(R.id.edit_full_name);
        edit_addr = (EditText) findViewById(R.id.edit_address);
        edit_hometown = (EditText) findViewById(R.id.edit_home_town);
        edit_numberphone = (EditText) findViewById(R.id.edit_phone_number);
        edit_birthday = (EditText) findViewById(R.id.edit_birthday);
        edit_zodiac = (EditText) findViewById(R.id.edit_zodiac);
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editUser = dataSnapshot.getValue(User.class);
                setValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        onDataChange();
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Saving data ...");
                progressDialog.show();
                Map<String, Object> map = toMap(editUser);
                mDataRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateInformationActivity.this, "Saving successful.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateInformationActivity.this, "Error saving data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private HashMap<String, Object> toMap(User user) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("display_name", user.getDisplay_name());
        result.put("email", user.getEmail());
        result.put("pi", user.getPi());
        result.put("userId", mCurrUser.getUid());
        return result;
    }
    private void setValue() {
        edit_display_name.setText(editUser.getDisplay_name());
        edit_description.setText(editUser.getPi().getDescription());
        edit_full_name.setText(editUser.getPi().getFull_name());
        edit_addr.setText(editUser.getPi().getAddress());
        edit_hometown.setText(editUser.getPi().getHome_town());
        edit_birthday.setText(editUser.getPi().getBirthday());
        edit_numberphone.setText(editUser.getPi().getNumber_phone());
        edit_zodiac.setText(editUser.getPi().getZodiac());
    }
    private void onDataChange() {
        edit_display_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.setDisplay_name(edit_display_name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setDescription(edit_description.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_full_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setFull_name(edit_full_name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_addr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setAddress(edit_addr.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_hometown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setHome_town(edit_hometown.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_numberphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setNumber_phone(edit_numberphone.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setBirthday(edit_birthday.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_zodiac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.getPi().setZodiac(edit_zodiac.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
