package com.rays.pro4.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {

    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String DATE_REGEX = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/(19|20)\\d\\d$";
    public static final String LOGIN_REGEX = "^[a-zA-Z0-9._-]{3,}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    
    public static boolean isNull(String val) {
        if (val == null || val.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNull(Integer val) {
        if (val == null) {
            return true;
        } else {
            return false;
        }
    }
    
        public static boolean isNull(Long val) {
        if (val == null) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isInteger(String val) {
        if (isNull(val)) {
            return false;
        }
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
    
      public static boolean isLong(String val) {
        if (isNull(val)) {
            return false;
        }
        try {
            Long.parseLong(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean isEmail(String val) {
        if (isNull(val)) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();

    }
    
        public static boolean isLogin(String val) {
        if (isNull(val)) {
            return false;
        }
        Pattern pattern = Pattern.compile(LOGIN_REGEX);
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();

    }

    public static boolean isDate(String val) {

        if (isNull(val)) {
            return false;
        }
        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();
    }

    public static boolean isDate(Date date) {

        if (date.after(new Date())) {
            return false;
        }
        return true;
    }

    public static boolean isString(String val) {
        if(isNull(val)){
            return false;
        }
       return true;
    }
    
    public static boolean isPassword(String val) {
       if(val.length()<8){
        return false;
       }
       return true;
    }

    public static boolean isPassword(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        }
        return false;

    }
    
    public static boolean isName(String name) {
        String regex = "^[A-Za-z ]{1,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    
    public static boolean isValidMobile(String mobile) {
        String regex = "^[6-9]{1}[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
    
    public static boolean isPhoneNo(String number){
       if(isNull(number)){
        return false;
       }
       String regex = "^[0-9]{10}$";
       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(number);
       return m.matches();
    }
}