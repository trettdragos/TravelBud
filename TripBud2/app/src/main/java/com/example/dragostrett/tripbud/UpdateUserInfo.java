package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class UpdateUserInfo extends AsyncTask<String, Integer, String> {
    Context context;
    public UpdateUserInfo(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        String username=params[0];
        String password=params[1];
        String email=params[2];
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection("jdbc:mysql://35.187.169.134:3306/android", "user", "password");
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET username=?, password=?, email=? WHERE username=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, UserInfo.getUsername());
            ps.executeUpdate();
            UserInfo.setUsername(username);
            UserInfo.setPassword(password);
            UserInfo.setEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){

            Toast.makeText(context, "Changes saved",
                    Toast.LENGTH_SHORT).show();

    }
}
