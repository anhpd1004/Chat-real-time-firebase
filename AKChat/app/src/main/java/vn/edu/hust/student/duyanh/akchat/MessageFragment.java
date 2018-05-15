package vn.edu.hust.student.duyanh.akchat;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controllers.HomeMessageAdapter;
import models.Message;
import models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private HomePageActivity home_page;
    private Context context = null;
    private ArrayList<Message> list_msg;
    private ArrayList<String> list_friend_id;
    private HomeMessageAdapter homeMessageAdapter;
    private ListView frag_msg_list_view;
    private DatabaseReference mDataRef;
    private FirebaseUser currentUser;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRef = FirebaseDatabase.getInstance().getReference().getRoot();
        list_friend_id = new ArrayList<>();
        try {
            context = getActivity();
            home_page = (HomePageActivity) getActivity();
        }catch (Exception e) {
            Toast.makeText(home_page, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout message_layout = (FrameLayout) inflater.inflate(R.layout.fragment_message, container, false);
        frag_msg_list_view = (ListView) message_layout.findViewById(R.id.frag_msg_list_view);
        list_msg = new ArrayList<>();
        list_msg.add(new Message(true));
        homeMessageAdapter = new HomeMessageAdapter(context, R.layout.message_view_line, list_msg);
        frag_msg_list_view.setAdapter(homeMessageAdapter);
        loadList_msg();
        frag_msg_list_view.setSelection(0);
        frag_msg_list_view.smoothScrollToPosition(0);
        frag_msg_list_view.setDivider(null);
        frag_msg_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(home_page)
                        .setTitle("Remove message?")
                        .setMessage("Would you like to remove this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Message msg = list_msg.get(position);
                                String friend = msg.getFrom()==currentUser.getUid() ? msg.getTo() : msg.getFrom();
                                DatabaseReference delete = mDataRef.child("messages").child(currentUser.getUid()).child(friend);
                                delete.removeValue();
                                list_msg.remove(position);
                                homeMessageAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //nothing
                            }
                        })
                        .show();
                return false;
            }
        });
        frag_msg_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_msg.get(position).setSeen(true);
                homeMessageAdapter.notifyDataSetChanged();
                mDataRef.child("messages").child(currentUser.getUid())
                        .child(list_friend_id.get(position - 1))
                        .limitToLast(1).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getRef().child("seen").setValue(true);
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
                final Intent chat_intent = new Intent(home_page, ChatActivity.class);
                chat_intent.putExtra("user_id", list_friend_id.get(position - 1));
                startActivity(chat_intent);
            }
        });

        return message_layout;
    }
    private void loadList_msg() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = currentUser.getUid();
        mDataRef.child("messages").child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().limitToLast(1).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        int i;
                        for(i = 1; i < list_msg.size(); i++) {
                            Message m = list_msg.get(i);
                            if(m.getFrom().equals(message.getFrom()) && m.getTo().equals(message.getTo())
                                    || m.getFrom().equals(message.getTo()) && m.getTo().equals(message.getFrom())) {
                                list_msg.set(i, message);
                                break;
                            }
                        }
                        if(i >= list_msg.size()) {
                            list_msg.add(message);
                            list_friend_id.add(message.getFrom().equals(userId)?message.getTo() : message.getFrom());
                        }
                        homeMessageAdapter.notifyDataSetChanged();
                        frag_msg_list_view.setSelection(1);
                        frag_msg_list_view.smoothScrollToPosition(1);
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
}
