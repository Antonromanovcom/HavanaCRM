package com.antonromanov.havanacrm.model;

import com.antonromanov.havanacrm.mainlogic.DAO.MainDAOimpl;
import com.antonromanov.havanacrm.usersession.utils.SessionUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

//Named
@ManagedBean
@SessionScoped
public class Sidebar {

    private ArrayList<MenuItems> sidebar;
    private MenuModel model;
    private ArrayList<MenuItems> menu;

    //EJB
    private MainDAOimpl daoUtils;


    public ArrayList<MenuItems> getSidebar() {
        return sidebar;
    }

    public void setSidebar(ArrayList<MenuItems> sidebar) {
        this.sidebar = sidebar;
    }

    public Sidebar(ArrayList<MenuItems> sidebar) {
        this.sidebar = sidebar;
    }

    @PostConstruct
    private void initSampleData() {
        daoUtils = new MainDAOimpl();
        model = new DefaultMenuModel();
        HttpSession session = SessionUtils.getSession();
        int id = Integer.parseInt((String) session.getAttribute("id"));
        this.menu = daoUtils.getSidebarMenuItems(id);
        this.parseModel();
    }

    public Sidebar() {
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    public void parseModel() {

        DefaultMenuItem item;
        this.setSidebar(menu);
        if (sidebar != null) {
            for (MenuItems itemfromDB : this.getSidebar()) {
                item = new DefaultMenuItem(itemfromDB.getNote());
                //item.setTitle(itemfromDB.getNote());
                item.setCommand("#{mainBean.onMainMenuClick}");
                item.setParam("action", itemfromDB.getAction());
                item.setUpdate(":content");
                System.out.println("ЭЛЕМЕНТ МЕНЮ = " + itemfromDB.getNote() + "  :  " + itemfromDB.getAction());

                model.addElement(item);
            }
        } else {
            System.out.println("Sidebar is NULL");
        }

        //return model;
    }


}
