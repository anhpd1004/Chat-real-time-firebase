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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import controllers.HomeFriendAdapter;
import controllers.HomeMessageAdapter;
import models.Friend;
import models.Message;
import models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private HomePageActivity home_page;
    private Context context = null;
    private ArrayList<User> list_friend;
    private HomeFriendAdapter homeFriendAdapter;
    private ListView frag_friend_list_view;

    //firebase
    private DatabaseReference mDataRef;
    private DatabaseReference mUser;
    private FirebaseUser currentUser;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            home_page = (HomePageActivity) getActivity();
            list_friend = new ArrayList<>();
            loadListFriend();
        }catch (Exception e) {
            Toast.makeText(home_page, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout friend_layout = (FrameLayout) inflater.inflate(R.layout.fragment_friend, container, false);
        frag_friend_list_view = (ListView) friend_layout.findViewById(R.id.frag_friend_list_view);

        homeFriendAdapter = new HomeFriendAdapter(context, R.layout.friend_view_line, list_friend);
        frag_friend_list_view.setAdapter(homeFriendAdapter);
        frag_friend_list_view.setSelection(0);
        frag_friend_list_view.smoothScrollToPosition(0);
        frag_friend_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int xxx = position;
                CharSequence options[] = new CharSequence[]{"Open profile", "Send message"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i == 0) {
                            Intent intent = new Intent(getContext(), ProfileActivity.class);
                            User user = list_friend.get(xxx);
                            intent.putExtra("user_id", user.getUserId());
                            intent.putExtra("display_name", user.getDisplay_name());
                            intent.putExtra("description", user.getPi().getDescription());
                            String fnums = (user.getPi().getFriends_number() < 2) ? (user.getPi().getFriends_number() + " friend") : (user.getPi().getFriends_number() + " friends");
                            intent.putExtra("friend_number", fnums);
                            intent.putExtra("profile", user.getPi().getProfile());
                            startActivity(intent);
                        } else if(i == 1) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            User user = list_friend.get(xxx);
                            intent.putExtra("user_id", user.getUserId());
                            intent.putExtra("display_name", user.getDisplay_name());
                            intent.putExtra("profile", user.getPi().getProfile());
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });
        return friend_layout;
    }
    private void loadListFriend() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
           return;
        }
        String userId = currentUser.getUid();
        mDataRef = FirebaseDatabase.getInstance().getReference().getRoot().child("friends").child(userId);
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String fUserid = ds.getKey();
                    Log.d("DA", fUserid);
                    mUser = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(fUserid);
                    mUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dtSnap) {
                            User user = dtSnap.getValue(User.class);
                            list_friend.add(user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            Toast.makeText(home_page, "Error: " + databaseError.getMessage() + ":\n\t" + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
                Toast.makeText(home_page, "Error: " + databaseError.getMessage() + ":\n\t" + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
