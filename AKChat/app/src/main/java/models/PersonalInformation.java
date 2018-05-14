package models;

import android.net.Uri;

/**
 * Created by dell co on 3/25/2018.
 */

public class PersonalInformation {
    private String profile = User.DEFAULT_AVATAR;//anh dai dien(avatar)
    private String full_name;//tên đầy đủ
    private String address;//địa chỉ hiện tại
    private String home_town;//quê quán
    private String number_phone ;//số điện thoại
    private String birthday;//sinh nhật
    private String zodiac;//cung hoang dao
    private String description ;//mo ta ve ban than
    private int friends_number ;//số lượng bạn bè
    public PersonalInformation() {

    }
    public PersonalInformation(boolean state) {
        this.profile = User.DEFAULT_AVATAR;
        this.full_name = "Phạm Duy Anh";
        this.address = "Hoàng Mai, Hà Nội, Việt Nam";
        this.home_town = "Hải Phòng, Việt Nam";
        this.number_phone = "01238100497";
        this.birthday = "1-1-2018";
        this.zodiac = "Aries";
        this.description = "Xin chào. Cảm ơn đã ghé thăm!";//status
        this.friends_number = 0;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHome_town() {
        return home_town;
    }

    public void setHome_town(String home_town) {
        this.home_town = home_town;
    }

    public String getNumber_phone() {
        return number_phone;
    }

    public void setNumber_phone(String number_phone) {
        this.number_phone = number_phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFriends_number() {
        return friends_number;
    }

    public void setFriends_number(int friends_number) {
        this.friends_number = friends_number;
    }
}
