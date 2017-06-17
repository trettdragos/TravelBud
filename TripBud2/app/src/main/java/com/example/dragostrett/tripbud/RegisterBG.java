package com.example.dragostrett.tripbud;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class RegisterBG extends AsyncTask<String, Integer, String> {
    Context context;
    public RegisterBG(Context context){this.context=context;}
    @Override
    protected String doInBackground(String... params) {
        String username=params[0];
        String password=params[1];
        String email=params[2];
        LogInActivity.pass=password;
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection("jdbc:mysql://35.187.169.134:3306/android", "user", "password");
            ps= (PreparedStatement) con.prepareStatement("INSERT INTO table1 (username, password, email, trip, type, longitudine, latitudine) VALUES (?,?,?, ?, ?, '0', '0')");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, "");
            if(RegisterActivity.admin)
            ps.setString(5, "1");
            else ps.setString(5, "0");
            ps.executeUpdate();
            UserInfo.setLogedIn(true);
            UserInfo.setUsername(username);
            UserInfo.setPassword(password);
            UserInfo.setEmail(email);
            TripInfo.setInATrip(false);
            if(RegisterActivity.admin)
            UserInfo.setType("1");
            else UserInfo.setType("0");
            TripInfo.setMeet("");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        if(UserInfo.isLogedIn()){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
        else
            Toast.makeText(context, "Error registering, please try again",
                    Toast.LENGTH_SHORT).show();
    }
}
