package com.antonromanov.havanacrm.usersession.DAO;


import com.antonromanov.havanacrm.usersession.util.DataConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    static Logger logger = LoggerFactory.getLogger(LoginDAO.class);

    public static boolean validate(String user, String password) {



        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("Select name, pwd from public.users where login = ? and pwd = ?");
            ps.setString(1, user);
            ps.setString(2, password);

            logger.info("Логин - " + user);
            logger.info("Пароль - " + password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //System.out.println("USER = " + user);
                logger.info("Есть вхождение!");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return false;
        } finally {
            DataConnect.close(con);
        }
        return false;



/*

        try {

        } catch (Exception e) {

        }


        if (user.equals("123")) {
            System.out.println("TRUE");
            return true;

        } else {
            System.out.println("FALSE");
            return false;
        }

*/
    }

}
