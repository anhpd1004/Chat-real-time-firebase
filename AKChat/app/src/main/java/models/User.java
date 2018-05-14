package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell co on 3/25/2018.
 */

public class User {
    public static final String DEFAULT_AVATAR = "https://firebasestorage.googleapis.com/v0/b/akchat-43ed8.appspot.com/o/user_profiles%2Fdefault_avatar%2Fdefault_avatar.png?alt=media&token=94005d33-0bbb-43bb-a02b-7fd254236542";
    private String email;
    private String display_name;//tên hiển thị
    private String userId;
    private long online;//thoi diem online gan nhat
    private PersonalInformation pi;
    public User() {

    }

    public User(boolean b) {
        this.display_name = "name";
        this.email = "email";
        this.userId = "";
        this.pi = new PersonalInformation(true);
    }

    public User(String userId, String email, String display_name) {
        this.userId = userId;
        this.email = email;
        this.display_name = display_name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PersonalInformation getPi() {
        return pi;
    }

    public void setPi(PersonalInformation pi) {
        this.pi = pi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
