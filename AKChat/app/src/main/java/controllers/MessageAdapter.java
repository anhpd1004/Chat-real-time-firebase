package controllers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Message;
import models.User;
import vn.edu.hust.student.duyanh.akchat.R;

/**
 * Created by dell co on 5/2/2018.
 */

public class MessageAdapter extends BaseAdapter {

    private FirebaseUser mUser;
    private String mUserId;
    private Context context;
    private int layout;
    private ArrayList<Message> list_messages;
    private RelativeLayout relativeLayout;


    public MessageAdapter() {
        init();
    }

    private void init() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser.getUid();
    }

    public MessageAdapter(Context context, int layout, ArrayList<Message> list_messages) {
        init();
        this.context = context;
        this.layout = layout;
        this.list_messages = list_messages;
    }



    @Override
    public int getCount() {
        return list_messages.size();
    }

    @Override
    public Object getItem(int position) {
        return list_messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //ViewHolder class
    private class ViewHolder {
        public CircleImageView chat_friend_profile;
        public TextView message_textview, chat_friend_seen;

        public ViewHolder() {

        }

        public ViewHolder(CircleImageView chat_friend_profile, TextView message_textview, TextView chat_friend_seen) {
            this.chat_friend_profile = chat_friend_profile;
            this.message_textview = message_textview;
            this.chat_friend_seen = chat_friend_seen;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(list_messages.size()==0)
            return null;

        final ViewHolder viewHolder;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            //anhxa
            viewHolder.chat_friend_profile = (CircleImageView) view.findViewById(R.id.chat_friend_profile);
            viewHolder.message_textview = (TextView) view.findViewById(R.id.message_textview);
            viewHolder.chat_friend_seen = (TextView) view.findViewById(R.id.chat_friend_seen);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Message message = list_messages.get(position);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.message_textview.getLayoutParams();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        float[] radii1 = new float[] {50f, 50f, 10f, 10f, 10f, 10f, 50f, 50f};
        float[] radii2 = new float[] {10f, 10f, 50f, 50f, 50f, 50f, 10f, 10f};

        if(mUserId.equals(message.getFrom())) {
            viewHolder.chat_friend_profile.setVisibility(View.GONE);
            viewHolder.message_textview.setText(message.getContent().toString());
            viewHolder.message_textview.setTextColor(Color.WHITE);

            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if(position == 0) {
                lp.setMargins(0,50,24,4);
                radii1[2] = 50f;
                radii1[3] = 50f;
                if(list_messages.size() > 1) {
                    if (!message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                        lp.setMargins(0, 4, 24, 70);
                        radii1[4] = 50f;
                        radii1[5] = 50f;
                    }
                } else {
                    radii1[4] = 50f;
                    radii1[5] = 50f;
                }
            } else if(position == list_messages.size() - 1){
                lp.setMargins(0, 4, 24, 4);
                radii1[4] = 50f;
                radii1[5] = 50f;
                if(list_messages.size() > 1) {
                    if (!message.getFrom().equals(list_messages.get(position - 1).getFrom())) {
                        lp.setMargins(0, 4, 24, 4);
                        radii1[2] = 50f;
                        radii1[3] = 50f;
                    }
                } else {
                    radii1[2] = 50f;
                    radii1[3] = 50f;
                }
            } else {
                if (!message.getFrom().equals(list_messages.get(position - 1).getFrom())
                        && !message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                    lp.setMargins(0, 4, 24, 70);
                    radii1[2] = 50f;
                    radii1[3] = 50f;
                    radii1[4] = 50f;
                    radii1[5] = 50f;
                } else if(!message.getFrom().equals(list_messages.get(position - 1).getFrom())) {
                    lp.setMargins(0, 4, 24, 4);
                    radii1[2] = 50f;
                    radii1[3] = 50f;
                } else if(!message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                    lp.setMargins(0, 4, 24, 70);
                    radii1[4] = 50f;
                    radii1[5] = 50f;
                } else {
                    lp.setMargins(0, 4, 24, 4);
                }
            }
            drawable.setColor(Color.BLUE);
            drawable.setCornerRadii(radii1);
            viewHolder.message_textview.setBackground(drawable);
            viewHolder.message_textview.setLayoutParams(lp);
        } else {
            lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            viewHolder.message_textview.setText(message.getContent().toString());
            viewHolder.message_textview.setTextColor(Color.BLACK);
            if(position == 0) {
                lp.setMargins(24,4,0,4);
                radii2[0] = 50f;
                radii2[1] = 50f;
                if(list_messages.size() > 1) {
                    if (!message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                        lp.setMargins(24, 50, 0, 70);
                        radii2[6] = 50f;
                        radii2[7] = 50f;
                    }
                } else {
                    radii2[6] = 50f;
                    radii2[7] = 50f;
                }
            } else if(position == list_messages.size() - 1){
                lp.setMargins(24, 4, 0, 4);
                radii2[6] = 50f;
                radii2[7] = 50f;
                if(list_messages.size() > 1) {
                    if (!message.getFrom().equals(list_messages.get(position - 1).getFrom())) {
                        lp.setMargins(24, 4, 0, 4);
                        radii2[0] = 50f;
                        radii2[1] = 50f;
                    }
                } else {
                    radii2[0] = 50f;
                    radii2[1] = 50f;
                }
            } else {
                if (!message.getFrom().equals(list_messages.get(position - 1).getFrom())
                        && !message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                    lp.setMargins(24, 4, 0, 70);
                    radii2[0] = 50f;
                    radii2[1] = 50f;
                    radii2[6] = 50f;
                    radii2[7] = 50f;
                } else if(!message.getFrom().equals(list_messages.get(position - 1).getFrom())) {
                    lp.setMargins(24, 4, 0, 4);
                    radii2[0] = 50f;
                    radii2[1] = 50f;
                } else if(!message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                    lp.setMargins(24, 4, 0, 70);
                    radii2[6] = 50f;
                    radii2[7] = 50f;
                } else {
                    lp.setMargins(24, 4, 0, 4);
                }
            }
            drawable.setColor(Color.LTGRAY);
            drawable.setCornerRadii(radii2);
            viewHolder.message_textview.setBackground(drawable);
            FirebaseDatabase.getInstance().getReference().getRoot()
                    .child("Users").child(message.getFrom()).child("pi").child("profile")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Picasso.with(context).load(dataSnapshot.getValue().toString()).into(viewHolder.chat_friend_profile);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            if(position == list_messages.size() - 1) {
                viewHolder.chat_friend_profile.setVisibility(View.VISIBLE);
            } else {
                if(message.getFrom().equals(list_messages.get(position + 1).getFrom())) {
                    viewHolder.chat_friend_profile.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.chat_friend_profile.setVisibility(View.VISIBLE);
                }
            }
        }


//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.message_textview.getLayoutParams();
//        if(mUserId.equals(message.getFrom())) {
//            viewHolder.chat_friend_profile.setVisibility(View.GONE);
//            viewHolder.message_textview.setText(message.getContent().toString());
//            viewHolder.message_textview.setBackgroundResource(R.drawable.message_background);
//            viewHolder.message_textview.setTextColor(Color.WHITE);
//
//            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            lp.setMargins(0,2,12,2);
//            viewHolder.message_textview.setLayoutParams(lp);
//            viewHolder.message_textview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//        } else {
//            lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            lp.setMargins(0,2,0,2);
//            viewHolder.chat_friend_profile.setVisibility(View.VISIBLE);
//            viewHolder.message_textview.setText(message.getContent().toString());
//            viewHolder.message_textview.setBackgroundResource(R.drawable.friend_messages_background);
//            viewHolder.message_textview.setTextColor(Color.BLACK);
//            FirebaseDatabase.getInstance().getReference().getRoot()
//                    .child("Users").child(message.getFrom()).child("pi").child("profile")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Picasso.with(context).load(dataSnapshot.getValue().toString()).into(viewHolder.chat_friend_profile);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//        }
        return view;
    }
}
