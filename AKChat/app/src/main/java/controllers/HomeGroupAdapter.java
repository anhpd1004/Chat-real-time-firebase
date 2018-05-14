package controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Message;
import vn.edu.hust.student.duyanh.akchat.R;

/**
 * Created by dell co on 3/31/2018.
 */

public class HomeGroupAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Message> list_msg;


    public HomeGroupAdapter() {

    }

    public HomeGroupAdapter(Context context, int layout, ArrayList<Message> list_msg) {
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
        ViewHolder viewHolder;
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
        Message msg = list_msg.get(position);
//        viewHolder.getDisplay_name().setText(msg.getReceiver().getDisplay_name());
//        viewHolder.getQuote().setText((CharSequence) msg.getContent());
//        viewHolder.getTime().setText(msg.getTime().toString());
        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
