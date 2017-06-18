package com.example.dragostrett.tripbud;

/**
 * Created by DragosTrett on 18.06.2017.
 */

public class DBConnection {
    public static String url="jdbc:mysql://35.187.169.134:3306/android";
    public static String user="user";
    public static String password="password";
    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DBConnection.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DBConnection.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DBConnection.password = password;
    }


}
