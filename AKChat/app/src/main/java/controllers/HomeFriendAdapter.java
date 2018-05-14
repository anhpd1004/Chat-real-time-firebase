package controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Friend;
import models.Message;
import models.User;
import vn.edu.hust.student.duyanh.akchat.R;

/**
 * Created by dell co on 3/31/2018.
 */

public class HomeFriendAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<User> list_friend;


    public HomeFriendAdapter() {

    }

    public HomeFriendAdapter(Context context, int layout, ArrayList<User> list_friend) {
        this.context = context;
        this.layout = layout;
        this.list_friend = list_friend;
    }

    @Override
    public int getCount() {
        return list_friend.size();
    }

    @Override
    public Object getItem(int position) {
        return list_friend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        private CircleImageView friend_profile;
        private TextView friend_fullname;

        public ViewHolder() {

        }

        public ViewHolder(CircleImageView friend_profile, TextView friend_fullname) {
            this.friend_profile = friend_profile;
            this.friend_fullname = friend_fullname;
        }

        public CircleImageView getFriend_profile() {
            return friend_profile;
        }

        public void setFriend_profile(CircleImageView friend_profile) {
            this.friend_profile = friend_profile;
        }

        public TextView getFriend_fullname() {
            return friend_fullname;
        }

        public void setFriend_fullname(TextView friend_fullname) {
            this.friend_fullname = friend_fullname;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            //anh xa
            viewHolder.setFriend_profile((CircleImageView) view.findViewById(R.id.friend_profile));
            viewHolder.setFriend_fullname((TextView) view.findViewById(R.id.friend_fullname));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final User friend = list_friend.get(position);
        Picasso.with(context).load(friend.getPi().getProfile()).into(viewHolder.getFriend_profile());
        viewHolder.getFriend_fullname().setText(friend.getDisplay_name());
        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
