package vn.edu.hust.student.duyanh.akchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
import models.User;

public class AllUserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ArrayList<User> list_friend;
    private HomeFriendAdapter homeFriendAdapter;
    private ListView all_user_listview;

    //firebase
    private DatabaseReference mDataRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase
        mDataRef = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //list data
        all_user_listview = (ListView) findViewById(R.id.all_user_listview);
        list_friend = new ArrayList<>();
        homeFriendAdapter = new HomeFriendAdapter(AllUserActivity.this, R.layout.friend_view_line, list_friend);
        loadAllUsers();
        all_user_listview.setAdapter(homeFriendAdapter);
        all_user_listview.setSelection(0);
        all_user_listview.smoothScrollToPosition(0);
        all_user_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(AllUserActivity.this, ProfileActivity.class);
//                User user = list_friend.get(position);
//                intent.putExtra("user_id", user.getUserId());
//                intent.putExtra("display_name", user.getDisplay_name());
//                intent.putExtra("description", user.getPi().getDescription());
//                String fnums = (user.getPi().getFriends_number() < 2) ? (user.getPi().getFriends_number() + " friend") : (user.getPi().getFriends_number() + " friends");
//                intent.putExtra("friend_number", fnums);
//                intent.putExtra("profile", user.getPi().getProfile());
//                startActivity(intent);
                //test chat
//                Intent intent = new Intent(AllUserActivity.this, ChatActivity.class);
//                User user = list_friend.get(position);
//                intent.putExtra("user_id", user.getUserId());
//                intent.putExtra("display_name", user.getDisplay_name());
//                intent.putExtra("profile", user.getPi().getProfile());
//                startActivity(intent);
                final int xxx = position;
                CharSequence options[] = new CharSequence[]{"Open profile", "Send message"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AllUserActivity.this);
                builder.setTitle("Select options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i == 0) {
                            Intent intent = new Intent(AllUserActivity.this, ProfileActivity.class);
                            User user = list_friend.get(xxx);
                            intent.putExtra("user_id", user.getUserId());
                            intent.putExtra("display_name", user.getDisplay_name());
                            intent.putExtra("description", user.getPi().getDescription());
                            String fnums = (user.getPi().getFriends_number() < 2) ? (user.getPi().getFriends_number() + " friend") : (user.getPi().getFriends_number() + " friends");
                            intent.putExtra("friend_number", fnums);
                            intent.putExtra("profile", user.getPi().getProfile());
                            startActivity(intent);
                        } else if(i == 1) {
                            Intent intent = new Intent(AllUserActivity.this, ChatActivity.class);
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
    }
    private void loadAllUsers() {
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals(mUser.getUid()))
                        continue;
                    User user = ds.getValue(User.class);
                    list_friend.add(user);
                    homeFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AllUserActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
