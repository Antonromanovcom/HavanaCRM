package com.antonromanov.havanacrm.model;

import com.antonromanov.havanacrm.usersession.utils.DAOUtils;
import com.antonromanov.havanacrm.usersession.utils.SessionUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
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
    private DAOUtils daoUtils;


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
        daoUtils = new DAOUtils();
        model = new DefaultMenuModel();
        System.out.println("МЫ ТУТ ");
        HttpSession session = SessionUtils.getSession();
        this.menu = daoUtils.getSidebarMenuItems((String) session.getAttribute("id"));
        this.parseModel();
    }

    public Sidebar() {
        //daoUtils = new DAOUtils();

        //this.parseModel(daoUtils.getSidebarMenuItems((String) session.getAttribute("id")))
        //this.parseModel();
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
                item.setUrl("http://www.primefaces.org");
                System.out.println("ЭЛЕМЕНТ МЕНЮ = " + itemfromDB.getNote());
                model.addElement(item);
            }
        } else {
            System.out.println("Sidebar is NULL");
        }

        //return model;
    }


}
