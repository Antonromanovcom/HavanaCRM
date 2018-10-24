package com.antonromanov.havanacrm.usersession.DAO;


import com.antonromanov.havanacrm.mainlogic.DAO.MainDAOimpl;
import com.antonromanov.havanacrm.usersession.Login;
import org.apache.log4j.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class LoginDAOimpl {

    final static Logger logger = Logger.getLogger(MainDAOimpl.class);

    public static boolean validate(String user, String password) {

        Connection con = null;
        PreparedStatement ps = null;

            logger.info("Логин - " + user);
            logger.info("Пароль - " + password);
            MainDAOimpl daoUtils = new MainDAOimpl();
        return daoUtils.loginCheck(user, password);
    }

    public static boolean checkOnlyLogin(String user) {
        MainDAOimpl daoUtils = new MainDAOimpl();
        return daoUtils.onlyLoginCheck(user);
    }
    public static boolean checkOnlyEmail(String email) {
        MainDAOimpl daoUtils = new MainDAOimpl();
        return daoUtils.onlyEmailCheck(email);
    }

    public static int getUserId(String user, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        MainDAOimpl daoUtils = new MainDAOimpl();
        return daoUtils.getUserId(user, password);
    }

    public static void createNewUser(Login newUser) {

        MainDAOimpl daoUtils = new MainDAOimpl();
        daoUtils.createNewUser(newUser);
    }
}
