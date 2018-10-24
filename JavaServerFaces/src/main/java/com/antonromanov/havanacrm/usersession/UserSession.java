package com.antonromanov.havanacrm.usersession;

import com.antonromanov.havanacrm.usersession.DAO.LoginDAOimpl;
import com.antonromanov.havanacrm.usersession.utils.SessionUtils;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.registry.infomodel.User;

@ManagedBean
@SessionScoped
public class UserSession implements Serializable {


    private String pwd;
    private HttpSession session;
    private String user;
    private int id;



    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String validateUsernamePassword() {
        //--AR-TR -- boolean valid = LoginDAOimpl.validate(user, pwd);
        boolean valid = true;
        if (valid) {
            //HttpSession session = SessionUtils.getSession();
            session = SessionUtils.getSession();
            session.setAttribute("username", user);
            session.setAttribute("id", String.valueOf(LoginDAOimpl.getUserId(user, pwd)));
            //session.setAttribute("id", "1");
             this.setId(LoginDAOimpl.getUserId(user, pwd));
            //this.setId(1);
            return "admin";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Passowrd",
                            "Please enter correct username and Password"));
            return "login";
        }
    }
    public String logout() {
        //HttpSession session = SessionUtils.getSession();
        session = SessionUtils.getSession();
        session.invalidate();
        return "login";
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
