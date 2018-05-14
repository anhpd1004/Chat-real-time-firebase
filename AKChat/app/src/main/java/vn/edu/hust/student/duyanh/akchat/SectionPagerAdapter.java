package vn.edu.hust.student.duyanh.akchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dell co on 3/30/2018.
 */

class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new MessageFragment();
            case 1:
                return new RequestFragment();
            case 2:
                return new FriendFragment();
            case 3:
                return new GroupFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Message";
            case 1:
                return "Request";
            case 2:
                return "Friend";
            case 3:
                return "Group";
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 4;
    }
}
