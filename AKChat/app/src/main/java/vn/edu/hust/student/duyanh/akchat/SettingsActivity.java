package vn.edu.hust.student.duyanh.akchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.BitSet;
import java.util.Map;

import controllers.CircleTransform;
import de.hdodenhof.circleimageview.CircleImageView;
import models.User;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CircleImageView settings_profile;
    private ImageButton settings_edit_info;
    private User thisuser;
    private StorageReference myProfile;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrUser;
    private DatabaseReference mDataRef;
    private StorageReference mStorage;
    private final int ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mCurrUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);

        settings_profile = (CircleImageView) findViewById(R.id.settings_profile);
        settings_edit_info = (ImageButton) findViewById(R.id.settings_edit_info);
        settings_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.edit_profile, null);
                builder.setView(view);


                final AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                wmlp.y = 5;
                dialog.getWindow().setAttributes(wmlp);
                dialog.show();
                view.findViewById(R.id.settings_cancel_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                view.findViewById(R.id.settings_edit_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setBorderCornerColor(Color.CYAN)
                                .setGuidelinesColor(Color.BLUE)
                                .start(SettingsActivity.this);
                        dialog.hide();
                    }
                });
                view.findViewById(R.id.settings_watch_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Xem anh
                        dialog.hide();
                    }
                });
            }
        });
        settings_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = mCurrUser.getUid();
                ProgressDialog pd = new ProgressDialog(SettingsActivity.this);
                pd.setMessage("Loading ...");
                pd.show();
                Intent update_info_intent = new Intent(SettingsActivity.this, UpdateInformationActivity.class);
                startActivity(update_info_intent);
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseReference mUser = mDataRef.child("Users").child(mCurrUser.getUid());
        mUser.keepSynced(true);
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisuser = dataSnapshot.getValue(User.class);
                displayInfo(thisuser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                StorageReference file = mStorage.child("user_profiles").child(mCurrUser.getUid()).child("profile.png");
                UploadTask uploadTask = file.putFile(resultUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "Failure to upload file.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        settings_profile.setImageURI(resultUri);
                        DatabaseReference x = mDataRef.getRoot().child("Users").child(mCurrUser.getUid()).child("pi").child("profile");
                        x.setValue(taskSnapshot.getDownloadUrl().toString());
                    }
                });
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {

        }
    }
    private void displayInfo(final User user) {
//        myProfile = FirebaseStorage.getInstance().getReferenceFromUrl(user.getPi().getProfile());
//        myProfile.getBytes(5 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inMutable = true;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//                settings_profile.setImageBitmap(bitmap);
//            }
//        });
        Picasso.with(SettingsActivity.this).load(user.getPi().getProfile())
                .networkPolicy(NetworkPolicy.OFFLINE).into(settings_profile, new Callback() {
            @Override
            public void onSuccess() {
                //todo something
            }

            @Override
            public void onError() {
                Picasso.with(SettingsActivity.this).load(user.getPi().getProfile())
                        .into(settings_profile);
            }
        });
        TextView des = (TextView) findViewById(R.id.settings_description);
        des.setText(user.getPi().getDescription());
        TextView fullname = (TextView) findViewById(R.id.settings_full_name);
        fullname.setText(user.getPi().getFull_name());
        TextView display_name = (TextView) findViewById(R.id.settings_display_name);
        display_name.setText(user.getDisplay_name());
        TextView addr = (TextView) findViewById(R.id.settings_address);
        addr.setText(user.getPi().getAddress());
        TextView homeTown = (TextView) findViewById(R.id.settings_home_town);
        homeTown.setText(user.getPi().getHome_town());
        TextView numberPhone = (TextView) findViewById(R.id.settings_number_phone);
        numberPhone.setText(user.getPi().getNumber_phone());
        TextView birthday = (TextView) findViewById(R.id.settings_birthday);
        birthday.setText(user.getPi().getBirthday());
        TextView zodiac = (TextView) findViewById(R.id.settings_zodiac);
        zodiac.setText(user.getPi().getZodiac());
        TextView numFriend = (TextView) findViewById(R.id.settings_friends_number);
        numFriend.setText(user.getPi().getFriends_number() + "");
    }
}
