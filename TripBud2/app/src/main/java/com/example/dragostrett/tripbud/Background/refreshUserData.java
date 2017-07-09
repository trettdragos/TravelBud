package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;

import com.example.dragostrett.tripbud.BasicInfo.MeetInfo;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created by DragosTrett on 08.07.2017.
 */

public class refreshUserData extends AsyncTask<String, Integer, String> {
    Context context;
    GoogleMap mapP;
    public refreshUserData(Context context, GoogleMap mapP){
        this.context=context;
        this.mapP=mapP;
    }
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("SELECT * FROM table1 WHERE username=?");
            ps.setString(1, UserInfo.getUsername());
            rs=ps.executeQuery();
            if(!rs.isBeforeFirst()){
                UserInfo.setLogedIn(false);
            }
            else{
                UserInfo.setLogedIn(true);
                while(rs.next()){
                    UserInfo.setTrip(rs.getString("trip"));
                    UserInfo.setType(rs.getString("type"));
                    String aux=rs.getString("visible");
                    if(aux.equals("1"))
                        UserInfo.setVisible(true);
                    else UserInfo.setVisible(false);
                    UserInfo.setNotification(rs.getString("notificare"));
                    UserInfo.setLocation(true);
                }
            }
            if(UserInfo.getTrip().equals("")){
                TripInfo.setMeet("");
                TripInfo.setInATrip(false);
            }
            else{
                ps= (PreparedStatement) con.prepareStatement("SELECT * FROM trips WHERE name='"+UserInfo.getTrip()+"'");
                rs=ps.executeQuery();
                TripInfo.setInATrip(true);
                while(rs.next()){
                    TripInfo.setCircleCenter(new LatLng(Double.parseDouble(rs.getString("LatitudineCentru")), Double.parseDouble(rs.getString("LongitudineCentru"))));
                    TripInfo.setCircleRange(Integer.parseInt(rs.getString("circleRange")));
                    TripInfo.setIdTrip(rs.getString("id"));
                    TripInfo.setNameTrip(rs.getString("name"));
                    TripInfo.setPlace(rs.getString("place"));
                    TripInfo.setOrganizator(rs.getString("organizator"));
                    TripInfo.setMeet(rs.getString("meet"));
                    TripInfo.setNumber_users(rs.getString("number_users"));
                }
            }
            if(!TripInfo.getMeet().equals("")){
                ps= (PreparedStatement) con.prepareStatement("SELECT * FROM meet WHERE  moment='"+TripInfo.getMeet()+"'");
                rs=ps.executeQuery();
                while(rs.next()){
                    MeetInfo.setLongitudine(rs.getString("longitudine"));
                    MeetInfo.setLatitudine(rs.getString("latitudine"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "done";
    }
}
