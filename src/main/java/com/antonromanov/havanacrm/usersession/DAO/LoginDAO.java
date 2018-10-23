package com.antonromanov.havanacrm.usersession.DAO;


import com.antonromanov.havanacrm.usersession.utils.DAOUtils;
import com.antonromanov.havanacrm.usersession.utils.DataConnect;
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

            logger.info("Логин - " + user);
            logger.info("Пароль - " + password);
            DAOUtils daoUtils = new DAOUtils();
        return daoUtils.loginCheck(user, password);
    }

    public static int getUserId(String user, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        DAOUtils daoUtils = new DAOUtils();
        return daoUtils.getUserId(user, password);
    }

}
