package com.antonromanov.havanacrm.usersession;


import com.antonromanov.havanacrm.usersession.DAO.LoginDAOimpl;
import org.primefaces.context.RequestContext;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@SessionScoped
public class Registration {

    private Login newUser;
    public String message="";

    @PostConstruct
    public void init() {
        newUser = new Login();
        //message = "1234";
    }


    public void dialogOk() {

        RequestContext.getCurrentInstance().execute("PF('dlg1').hide()");

    }

    public String RegisterUser() {

        String result = "signin";

        if (this.newUser.getLogin().isEmpty() || this.newUser.getEmail().isEmpty() || this.newUser.getPassword().isEmpty()) {

            System.out.println("pizdec !!!!");
            message = "Заполните поля Логин, Пароль и Email!";
            RequestContext.getCurrentInstance().execute("PF('dlg1').show()");


        } else {


            boolean validLogin = LoginDAOimpl.checkOnlyLogin(this.newUser.getLogin());
            boolean validEmail = LoginDAOimpl.checkOnlyEmail(this.newUser.getEmail());

            if (validLogin) {

                System.out.println("Такой логин уже есть");
                message = "Такой логин уже есть!";
                RequestContext.getCurrentInstance().execute("PF('dlg1').show()");

            } else if (validEmail) {

                message = "Такой email уже есть!";
                RequestContext.getCurrentInstance().execute("PF('dlg1').show()");


            } else {

                LoginDAOimpl.createNewUser(this.newUser);
                result = "login";
                return result;

            }
        }

        return "signin";
    }

    public Login getNewUser() {
        return newUser;
    }

    public void setNewUser(Login newUser) {
        this.newUser = newUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
