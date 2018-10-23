package com.antonromanov.havanacrm.usersession;

import com.antonromanov.havanacrm.usersession.DAO.LoginDAOimpl;
import com.antonromanov.havanacrm.usersession.utils.SessionUtils;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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
    //validate login
    public String validateUsernamePassword() {
        boolean valid = LoginDAOimpl.validate(user, pwd);
        if (valid) {
            //HttpSession session = SessionUtils.getSession();
            session = SessionUtils.getSession();
            session.setAttribute("username", user);
            session.setAttribute("id", String.valueOf(LoginDAOimpl.getUserId(user, pwd)));
            this.setId(LoginDAOimpl.getUserId(user, pwd));
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
