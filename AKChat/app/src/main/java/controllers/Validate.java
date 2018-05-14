package controllers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell co on 3/25/2018.
 */

public class Validate {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]{1}[a-zA-Z0-9\\._]{5,29}@{1}[a-zA-Z0-9_]{2,12}(\\.[a-zA-Z0-9_]{2,12})+$";
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9_!@#$%^&*(),?<>*`~\\.]{8,30}$";
    private static final String PHONE_PATTERN = "^(\\+[1-9]{2})?[0-9]{9,12}$";

    private static Pattern pattern;
    private static Matcher matcher;

    //validate input email
    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //validate input password
    public static boolean validatePassword(String pass) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pass);
        return matcher.matches();
    }
    //validate input phone number
    public static boolean validatePhoneNumber(String phone_number) {
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone_number);
        return matcher.matches();
    }
}
