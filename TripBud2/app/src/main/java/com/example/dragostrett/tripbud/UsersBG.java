package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class UsersBG extends AsyncTask<String, Integer, String> {

    Context context;
    View show;
    LinkedList<String> a = new LinkedList<>();
    public UsersBG(Context context,  View show){
        this.context=context;this.show = show;
    }

    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection("jdbc:mysql://35.187.169.134:3306/android", "user", "password");
            ps= (PreparedStatement) con.prepareStatement("SELECT * FROM table1 WHERE trip=?");
            ps.setString(1, UserInfo.getTrip());
            rs=ps.executeQuery();
            rs.next();
            do{
                a.push(rs.getString("username"));
            }while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        for(int j=0; j<a.size(); j++){
            TextView valueTV = new TextView(context);
            valueTV.setText(a.get(j));
            valueTV.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            valueTV.setPadding(5, 5, 5, 5);
            valueTV.setTextSize(23);
            valueTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ((LinearLayout) show).addView(valueTV);
        }
    }
}
