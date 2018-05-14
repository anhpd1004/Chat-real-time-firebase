package vn.edu.hust.student.duyanh.akchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import models.Friend;
import models.Request;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView profile_image_view;
    private TextView profile_display_name, profile_description, profile_friend_number;
    private Button profile_send_request;
    private int friendState = 0;
    private int requestState = 0;
    private String requestDate;
    private String frienDate;
    private Button profile_decline_friend_request;


    //firebase
    private DatabaseReference mDataref;
    private FirebaseUser mUser;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //nhan du lieu tu AllUsers gui sang qua intent
        Intent intent = getIntent();
        String displayName = intent.getStringExtra("display_name");
        final String userId = intent.getStringExtra("user_id");
        String status = intent.getStringExtra("description");
        String friendNumber = intent.getStringExtra("friend_number");
        String profile = intent.getStringExtra("profile");

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Personal page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //anh xa
        anhXa();

        //gan gia tri
        displayInfo(displayName, status, friendNumber);

        //firebase
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDataref = FirebaseDatabase.getInstance().getReference().getRoot();

        setState(userId);

        //Set profile to imageview
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(profile);
        storageReference.getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                profile_image_view.setImageBitmap(bm);
            }
        });


//        //su kien voi send request
        profile_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---UNFRIEND---
                if(friendState == Friend.IS_FRIEND) {
                    DatabaseReference f = mDataref.child("friends").child(mUser.getUid()).child(userId);
                    f.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mDataref.child("friends").child(userId).child(mUser.getUid())
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    profile_send_request.setText("Send request");
                                    friendState = Friend.NOT_FRIEND;
                                    Toast.makeText(ProfileActivity.this, "Unfriend successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    //----CANCEL FRIEND REQUEST----
                } else if(requestState == Request.SENDER_REQUEST) {
                    mDataref.child("requests").child(mUser.getUid()).child(userId)
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mDataref.child("requests").child(userId).child(mUser.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            profile_send_request.setText("send request");
                                            requestState = Request.NO_REQUEST;
                                            Toast.makeText(ProfileActivity.this, "Cancel friend request successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                    //----ACCEPT FRIEND REQUEST
                } else if(requestState == Request.RECEIVER_REQUEST) {



                    //----SEND FRIEND REQUEST
                } else {
                    final Request request = new Request();
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    request.setFromDate(sdf.format(date).toString());
                    request.setRequestState(Request.RECEIVER_REQUEST);
                    mDataref.child("requests").child(userId).child(mUser.getUid()).setValue(request)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    request.setRequestState(Request.SENDER_REQUEST);
                                    mDataref.child("requests").child(mUser.getUid()).child(userId).setValue(request)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    profile_send_request.setText("Cancel request");
                                                    requestState = Request.SENDER_REQUEST;
                                                    Toast.makeText(ProfileActivity.this, "Sent friend request successful", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                }
            }
        });

    }
    private void displayInfo(String displayName, String des, String fnums) {
        profile_display_name.setText(displayName);
        profile_description.setText(des);
        profile_friend_number.setText(fnums);
    }
    private void anhXa() {
        profile_display_name = (TextView) findViewById(R.id.profile_display_name);
        profile_description = (TextView) findViewById(R.id.profile_description);
        profile_friend_number = (TextView) findViewById(R.id.profile_friend_number);
        profile_image_view = (ImageView) findViewById(R.id.profile_image_view);
        profile_send_request = (Button) findViewById(R.id.profile_send_request);
        profile_decline_friend_request = (Button) findViewById(R.id.profile_decline_friend_request);
    }
    //set friend and request state
    private void setState(final String fUserId) {
        DatabaseReference fff = mDataref.child("friends").child(mUser.getUid()).child(fUserId);
        fff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Log.d("AA", "ISFRIEND");
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    friendState = Friend.IS_FRIEND;
                    frienDate = friend.getFromDate();
                    profile_send_request.setText("Unfriend");
                    profile_decline_friend_request.setVisibility(View.INVISIBLE);
                } else {
                    friendState = Friend.NOT_FRIEND;
                    DatabaseReference ffff = mDataref.child("requests").child(mUser.getUid()).child(fUserId);
                    ffff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                requestState = Request.NO_REQUEST;
                                profile_send_request.setText("Send request");
                                profile_decline_friend_request.setVisibility(View.INVISIBLE);
                            } else {
                                Log.d("AA", "IS REQUESTED");
                                Request request = dataSnapshot.getValue(Request.class);
                                requestState = request.getRequestState();
                                requestDate = request.getFromDate();
                                if(requestState == Request.RECEIVER_REQUEST) {
                                    profile_send_request.setText("Accept request");
                                    profile_decline_friend_request.setVisibility(View.VISIBLE);
                                } else {
                                    profile_send_request.setText("Cancel request");
                                    profile_decline_friend_request.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
