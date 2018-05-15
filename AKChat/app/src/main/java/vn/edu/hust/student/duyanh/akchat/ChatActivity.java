package vn.edu.hust.student.duyanh.akchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.MessageAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import models.Message;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton send, add;
    private CircleImageView chat_custom_bar;
    private EditText textbox;
    private ListView listView;
    private ArrayList<Message> listMessages;
    private MessageAdapter messageAdapter;

    //firebase
    private FirebaseUser mUser;
    private DatabaseReference mData;
    private FirebaseStorage mStorage;
    private String fUserId, fDisplayName, fProfile;

    public static final int GALERY_PIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //firebase
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference().getRoot();
        mStorage = FirebaseStorage.getInstance();

        final Intent intent = getIntent();
        fUserId = intent.getStringExtra("user_id");
        mData.child("Users").child(fUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fDisplayName = dataSnapshot.child("display_name").getValue().toString();
                fProfile = dataSnapshot.child("pi").child("profile").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(ChatActivity.this, R.layout.chat_single_message, listMessages);
        anhXa();
//        Picasso.with(ChatActivity.this).load(fProfile).into(chat_custom_bar);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(view);
        actionBar.setTitle(fDisplayName);


        listView.setAdapter(messageAdapter);
        listView.setDivider(null);
        listView.setClickable(false);
        loadMessages();

        textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.setSelection(listMessages.size() - 1);
                listView.smoothScrollToPosition(listMessages.size() - 1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listView.setSelection(listMessages.size() - 1);
                listView.smoothScrollToPosition(listMessages.size() - 1);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textbox.getText().toString();
                if(!TextUtils.isEmpty(msg)) {
                    sendMessage(msg);
                    textbox.setText("");
                }
            }
        });
        android.content.Context context = ChatActivity.this;
        final SinchClient sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey("f0e08bfa-0b9a-4e7d-99e0-ea486ad472e0")
                .applicationSecret("nzKsDUUacEOebnwN5vIHCg==")
                .environmentHost("sandbox.sinch.com")
                .userId("103652")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.start();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CallClient callClient = sinchClient.getCallClient();
//                callClient.callUser("");
//                callClient.addCallClientListener(new CallClientListener() {
//                    @Override
//                    public void onIncomingCall(CallClient callClient, Call call) {
//                        call.answer();
//                    }
//                });
                final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.add_view, null);
                builder.setView(view);


                final AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                wmlp.y = 5;
                dialog.getWindow().setAttributes(wmlp);
                dialog.show();
                view.findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALERY_PIC);
                        dialog.hide();
                    }
                });
                view.findViewById(R.id.add_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 10);
                        dialog.hide();
                    }
                });
                view.findViewById(R.id.add_call).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Xem anh
                        dialog.hide();
                    }
                });
            }
        });
        mData.child("chats").child(mUser.getUid()).child(fUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", System.currentTimeMillis());

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/" + mUser.getUid() + "/" + fUserId, chatAddMap);
                    chatUserMap.put("chats/" + fUserId + "/" + mUser.getUid(), chatAddMap);
                    mData.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null) {
                                Log.d("ERROR_LOG", databaseError.getMessage().toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && requestCode == GALERY_PIC && resultCode ==RESULT_OK) {
            Uri imageUri = data.getData();
            String key = mData.child("messages").child(mUser.getUid()).child(fUserId).push().getKey();
            final String currUserRef = "messages/" + mUser.getUid()+"/" +fUserId + "/"  + key;
            final String friendRef = "messages/"+fUserId+"/"+mUser.getUid() + "/" + key;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().getRoot();
            storageReference.child(currUserRef).child(System.currentTimeMillis()+"").child("send_image.jgp")
                    .putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    Message message = new Message();
                    message.setSeen(false);
                    message.setType("image");
                    message.setTo(fUserId);
                    message.setFrom(mUser.getUid());
                    message.setContent(downloadUri);
                    message.setTimestamp(System.currentTimeMillis());
                    Map map = new HashMap();
                    map.put(currUserRef, message);
                    map.put(friendRef, message);
                    mData.updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //todo something
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, "Send image error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if(data != null && requestCode == 10 && resultCode ==RESULT_OK) {
            Uri imageUri = data.getData();
            String key = mData.child("messages").child(mUser.getUid()).child(fUserId).push().getKey();
            final String currUserRef = "messages/" + mUser.getUid()+"/" +fUserId + "/"  + key;
            final String friendRef = "messages/"+fUserId+"/"+mUser.getUid() + "/" + key;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().getRoot();
            storageReference.child(currUserRef).child(System.currentTimeMillis()+"").child("send_image.jgp")
                    .putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    Message message = new Message();
                    message.setSeen(false);
                    message.setType("image");
                    message.setTo(fUserId);
                    message.setFrom(mUser.getUid());
                    message.setContent(downloadUri);
                    message.setTimestamp(System.currentTimeMillis());
                    Map map = new HashMap();
                    map.put(currUserRef, message);
                    map.put(friendRef, message);
                    mData.updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //todo something
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, "Send image error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendMessage(String msg) {

        String key = mData.child("messages").child(mUser.getUid()).child(fUserId).push().getKey();

        String currUserRef = "messages/" + mUser.getUid() + "/" + fUserId + "/" + key;
        String fUserRef = "messages/" + fUserId + "/" + mUser.getUid() + "/" + key;
        Map m = new HashMap();
        m.put(currUserRef + "/content", msg);
        m.put(currUserRef + "/type", "text");
        m.put(currUserRef + "/timestamp", ServerValue.TIMESTAMP);
        m.put(currUserRef + "/from", mUser.getUid());
        m.put(currUserRef + "/to", fUserId);
        m.put(currUserRef + "/seen", false);
        m.put(fUserRef + "/content", msg);
        m.put(fUserRef + "/type", "text");
        m.put(fUserRef + "/timestamp", ServerValue.TIMESTAMP);
        m.put(fUserRef + "/from", mUser.getUid());
        m.put(fUserRef + "/to", fUserId);
        m.put(fUserRef + "/seen", false);
        mData.updateChildren(m, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //todo something
            }
        });
    }
    private void loadMessages() {
        mData.child("messages").child(mUser.getUid()).child(fUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                listMessages.add(message);
                messageAdapter.notifyDataSetChanged();
                listView.setSelection(listMessages.size() - 1);
                listView.smoothScrollToPosition(listMessages.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void anhXa() {
        send = (ImageButton) findViewById(R.id.chat_send_button);
        add = (ImageButton) findViewById(R.id.chat_add_button);
        textbox = (EditText) findViewById(R.id.chat_textbox);
        listView = (ListView) findViewById(R.id.messages_listview);
        chat_custom_bar = (CircleImageView) findViewById(R.id.chat_custom_bar);
    }
}
