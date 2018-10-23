package com.antonromanov.havanacrm.usersession.DAO;


import com.antonromanov.havanacrm.mainlogic.DAO.MainDAOimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LoginDAOimpl {

 //   static Logger logger = LoggerFactory.getLogger(LoginDAOimpl.class);

    public static boolean validate(String user, String password) {

        Connection con = null;
        PreparedStatement ps = null;

         //   logger.info("Логин - " + user);
        //    logger.info("Пароль - " + password);
            MainDAOimpl daoUtils = new MainDAOimpl();
        return daoUtils.loginCheck(user, password);
    }

    public static int getUserId(String user, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        MainDAOimpl daoUtils = new MainDAOimpl();
        return daoUtils.getUserId(user, password);
    }

}
