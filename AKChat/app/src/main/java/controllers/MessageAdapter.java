package controllers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        ViewHolder viewHolder;

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
        if(mUserId.equals(message.getFrom())) {
            viewHolder.chat_friend_profile.setVisibility(View.GONE);
            viewHolder.message_textview.setText(message.getContent().toString());
            viewHolder.message_textview.setBackgroundResource(R.drawable.message_background);
            viewHolder.message_textview.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.message_textview.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            viewHolder.message_textview.setLayoutParams(lp);
            viewHolder.message_textview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            viewHolder.chat_friend_profile.setVisibility(View.VISIBLE);
            viewHolder.message_textview.setText(message.getContent().toString());
            viewHolder.message_textview.setBackgroundColor(Color.WHITE);
            viewHolder.message_textview.setTextColor(Color.BLACK);
            viewHolder.message_textview.setGravity(Gravity.LEFT);
        }

        return view;
    }
}
