package controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Message;
import models.User;
import vn.edu.hust.student.duyanh.akchat.HomePageActivity;
import vn.edu.hust.student.duyanh.akchat.R;

/**
 * Created by dell co on 3/31/2018.
 */

public class HomeMessageAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Message> list_msg;


    public HomeMessageAdapter() {

    }

    public HomeMessageAdapter(Context context, int layout, ArrayList<Message> list_msg) {
        this.context = context;
        this.layout = layout;
        this.list_msg = list_msg;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public List<Message> getList_msg() {
        return list_msg;
    }

    public void setList_msg(ArrayList<Message> list_msg) {
        this.list_msg = list_msg;
    }

    @Override
    public int getCount() {
        return list_msg.size();
    }

    @Override
    public Object getItem(int position) {
        return list_msg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder {
        private CircleImageView message_profile;
        private CircleImageView message_profile_seen;
        private TextView display_name, quote, time;

        public ViewHolder() {

        }

        public ViewHolder(CircleImageView message_profile, CircleImageView message_profile_seen, TextView display_name, TextView quote, TextView time) {
            this.message_profile = message_profile;
            this.message_profile_seen = message_profile_seen;
            this.display_name = display_name;
            this.quote = quote;
            this.time = time;
        }

        public CircleImageView getMessage_profile() {
            return message_profile;
        }

        public void setMessage_profile(CircleImageView message_profile) {
            this.message_profile = message_profile;
        }

        public CircleImageView getMessage_profile_seen() {
            return message_profile_seen;
        }

        public void setMessage_profile_seen(CircleImageView message_profile_seen) {
            this.message_profile_seen = message_profile_seen;
        }

        public TextView getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(TextView display_name) {
            this.display_name = display_name;
        }

        public TextView getQuote() {
            return quote;
        }

        public void setQuote(TextView quote) {
            this.quote = quote;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(list_msg.size() == 0)
            return new View(getContext());
        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            //anh xa
            viewHolder.setMessage_profile((CircleImageView) view.findViewById(R.id.message_profile));
            viewHolder.setMessage_profile_seen((CircleImageView) view.findViewById(R.id.message_profile_seen));
            viewHolder.setDisplay_name((TextView) view.findViewById(R.id.message_display_name));
            viewHolder.setTime((TextView) view.findViewById(R.id.message_time));
            viewHolder.setQuote((TextView) view.findViewById(R.id.message_message_quote));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String currUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Message msg = list_msg.get(position);
        if(position == 0) {
            Picasso.with(getContext()).load(User.DEFAULT_AVATAR).into(viewHolder.getMessage_profile());
            Picasso.with(getContext()).load(User.DEFAULT_AVATAR).into(viewHolder.getMessage_profile_seen());
            viewHolder.getTime().setText(GetTimeAgo.getTimeAgo(System.currentTimeMillis(), getContext()));
            viewHolder.getQuote().setText("");
            viewHolder.getDisplay_name().setText("");
        } else {
            boolean me = (currUserId.equals(msg.getFrom()));
            String content = (me) ? ("Bạn: " + msg.getContent()) : (msg.getContent() + "");
            viewHolder.getQuote().setText(content + "");
            String notMe = (me) ? msg.getTo() : msg.getFrom();
            if(!msg.getSeen())
                viewHolder.getQuote().setTextColor(Color.BLACK);
            else
                viewHolder.getQuote().setTextColor(Color.GRAY);
            DatabaseReference getFriendProfile = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(notMe);
            getFriendProfile.keepSynced(true);
            getFriendProfile.child("pi").child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Picasso.with(getContext()).load(dataSnapshot.getValue().toString())
                            .networkPolicy(NetworkPolicy.OFFLINE).into(viewHolder.message_profile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext()).load(dataSnapshot.getValue().toString()).into(viewHolder.message_profile);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(notMe).child("display_name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    viewHolder.getDisplay_name().setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String time = GetTimeAgo.getTimeAgo(msg.getTimestamp(), getContext()) + "";
            viewHolder.getTime().setText(time);
        }
        if(position == 0) {
            viewHolder.getMessage_profile().setVisibility(View.GONE);
            viewHolder.getDisplay_name().setVisibility(View.GONE);
            viewHolder.getTime().setVisibility(View.GONE);
            viewHolder.getQuote().setVisibility(View.GONE);
            viewHolder.getMessage_profile_seen().setVisibility(View.GONE);
        } else {
            viewHolder.getMessage_profile().setVisibility(View.VISIBLE);
            viewHolder.getDisplay_name().setVisibility(View.VISIBLE);
            viewHolder.getTime().setVisibility(View.VISIBLE);
            viewHolder.getQuote().setVisibility(View.VISIBLE);
            viewHolder.getMessage_profile_seen().setVisibility(View.GONE);
        }
        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
