package com.antonromanov.havanacrm.usersession.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.antonromanov.havanacrm.model.MenuItems;
import com.antonromanov.havanacrm.usersession.utils.DataConnect;


public class DAOUtils {

    Connection con = null;

    public boolean loginCheck(String login, String password) {

        String callSQL = "SELECT public.usercheck(?,?)";
        Boolean result = false;
        String queryResult = "";

        try {
            con = DataConnect.getConnection();


            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, login);
            pstmt.setString(2, password);


            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //queryResult = rs.getString(1);
                //rs.getString()
                result = rs.getBoolean(1);
                System.out.println("хуй - " + rs.getString(1));
            }

//if (rs.getString(1).substring(0, 1);
            DataConnect.close(con);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Integer getUserId(String login, String password) {

        String callSQL = "SELECT public.userid(?,?)";
        Integer result = -1;

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
                System.out.println("user id - " + rs.getString(1));
            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<MenuItems> getSidebarMenuItems(Integer userId) {

        ArrayList<MenuItems> result = new ArrayList<>();
        String callSQL = "SELECT public.get_sidebarmenu(?)";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new MenuItems(rs));
                System.out.println("хуй - " + rs.getString(1));
            }

            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}