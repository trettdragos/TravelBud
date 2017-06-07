package com.example.dragostrett.tripbud;

/**
 * Created by DragosTrett on 29.05.2017.
 */

public class SecurityCheck {
    public static String isPasswordOk(String s){
        boolean ok = true;
        String response = "Password MUST:\n";
        String regex[] = {
                "(.*[0-9].*)",
                "(.*[a-z].*)",
                "(.*[A-Z].*)",
                "(.*[@#$%^&+=!].*)",
                "(\\S+$)",
                ".{8,}$"
        };
        if (!s.matches(regex[0])) {
            response += "Contain at least one digit [0-9]\n";
            ok = false;
        }
        if (!s.matches(regex[1])) {
            response += "Contain at least one lowercase letter [a-z]\n";
            ok = false;
        }
        if (!s.matches(regex[2])) {
            response += "Contain at least one uppercase letter [A-Z]\n";
            ok = false;
        }
        if (!s.matches(regex[3])) {
            response += "Contain at least one special character [@#$%^&+=!]\n";
            ok = false;
        }
        if (!s.matches(regex[4])) {
            response += "Not contain any whitespace\n";
            ok = false;
        }
        if (!s.matches(regex[5])) {
            response += "Be at least eight characters long\n";
            ok = false;
        }
        if (ok) response = "";
        return response;
    }
    public static String  isEmailOk(String s){
        String response = "";
        String regex[] = {
                "(.*[a-z].*)",
                "(.*[@].*)",
                "(.*[.].*)"
        };
        if (!s.matches(regex[0]) || !s.matches(regex[1]) || !s.matches(regex[2])) {
            response = "Invalid Email\n";
        }
        return response;
    }
    public static boolean isUsernameOk(String username){
        if(username.length()>5)
            return true;
        return false;
    }

}
