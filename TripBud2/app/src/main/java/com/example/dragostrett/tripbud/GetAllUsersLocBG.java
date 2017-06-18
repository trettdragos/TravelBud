package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;

/**
 * Created by DragosTrett on 27.05.2017.
 */

public class GetAllUsersLocBG extends AsyncTask<String, Integer, String> {
    Context context;
    GoogleMap map;
    LinkedList<LatLng> a = new LinkedList<>();
    LinkedList<String> name = new LinkedList<>();
    LinkedList<Boolean> visibility = new LinkedList<>();
    public GetAllUsersLocBG(Context context, GoogleMap show){
        this.context=context;this.map = show;
    }
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("SELECT * FROM table1 WHERE trip=?");
            ps.setString(1, UserInfo.getTrip());
            rs=ps.executeQuery();
            rs.next();
            do{
                LatLng n= new LatLng(Double.parseDouble(rs.getString("latitudine")), Double.parseDouble(rs.getString("longitudine")));
                a.push(n);
                name.push(rs.getString("username"));
                String aux=rs.getString("visible");
                if(aux.equals("1"))
                    visibility.push(true);
                else visibility.push(false);
            }while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        for(int j=0; j<a.size(); j++){
            if(!name.get(j).equals(UserInfo.getUsername()) && !name.get(j).equals("") && (visibility.get(j)|| UserInfo.getType().equals("1")))
            map.addMarker(new MarkerOptions().position(a.get(j)).title(name.get(j)));
        }
    }
}
