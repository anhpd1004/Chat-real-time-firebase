package vn.edu.hust.student.duyanh.akchat;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.MessageAdapter;
import models.Message;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton send, add;
    private EditText textbox;
    private ListView listView;
    private ArrayList<Message> listMessages;
    private MessageAdapter messageAdapter;

    //firebase
    private FirebaseUser mUser;
    private DatabaseReference mData;
    private FirebaseStorage mStorage;
    private String fUserId, fDisplayName, fProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent intent = getIntent();
        fUserId = intent.getStringExtra("user_id");
        fDisplayName = intent.getStringExtra("display_name");
        fProfile = intent.getStringExtra("profile");
        listMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(ChatActivity.this, R.layout.chat_single_message, listMessages);
        anhXa();

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(fDisplayName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(view);


        //firebase
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference().getRoot();
        mStorage = FirebaseStorage.getInstance();


        listView.setAdapter(messageAdapter);
        listView.setSelection(listMessages.size());
        listView.smoothScrollToPosition(listMessages.size());
        loadMessages();


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
                CallClient callClient = sinchClient.getCallClient();
                callClient.callUser("");
                callClient.addCallClientListener(new CallClientListener() {
                    @Override
                    public void onIncomingCall(CallClient callClient, Call call) {
                        call.answer();
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
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

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
    }
}
