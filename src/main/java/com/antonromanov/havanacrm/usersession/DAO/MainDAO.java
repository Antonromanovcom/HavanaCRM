package com.antonromanov.havanacrm.usersession.DAO;

import com.antonromanov.havanacrm.model.MenuItems;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.ArrayList;

@Remote
public interface MainDAO {

    public boolean loginCheck(String login, String password);
    public Integer getUserId(String login, String password);
    public ArrayList<MenuItems> getSidebarMenuItems(String userId);
    public String Test();

}
