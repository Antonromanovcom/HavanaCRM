package com.antonromanov.havanacrm.usersession;

import com.antonromanov.havanacrm.model.MenuItems;
import com.antonromanov.havanacrm.model.Sidebar;
import com.antonromanov.havanacrm.usersession.DAO.MainDAO;
import com.antonromanov.havanacrm.usersession.utils.DAOUtils;
import com.antonromanov.havanacrm.usersession.utils.SessionUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
public class MainBean {

    @EJB
    private MainDAO mainDAO;

    private String t;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }




    //   private Sidebar sidebar;
  //  private MenuModel model;
 ///   private ArrayList<MenuItems> testa;


    @PostConstruct
    private void initSampleData() {

   t = mainDAO.Test();
     //   daoUtils = new DAOUtils();
   //     model = new DefaultMenuModel();
    //    sidebar = new Sidebar();
        HttpSession session = SessionUtils.getSession();
    //    testa = daoUtils.getSidebarMenuItems((String) session.getAttribute("id"));
    //    this.parseModel();
    }

    public MainBean() {
    }

  //  public Sidebar getSidebar() {
  //      return sidebar;
 //   }

 //  public void setSidebar(Sidebar sidebar) {
  //      this.sidebar = sidebar;
  //  }

 //   public MenuModel getModel() {
  //      return model;
  //  }

  //  public void setModel(MenuModel model) {
   //     this.model = model;
//    }

  /*  public MenuModel parseModel() {

        DefaultMenuItem item;
        sidebar.setSidebar(testa);
        if (sidebar != null) {
            for (MenuItems itemfromDB : sidebar.getSidebar()) {
                item = new DefaultMenuItem(itemfromDB.getNote());
                //item.setTitle(itemfromDB.getText());
                item.setUrl("http://www.primefaces.org");
                System.out.println("ЭЛЕМЕНТ МЕНЮ = " + itemfromDB.getNote());
                model.addElement(item);
            }
        } else {
            System.out.println("Sidebar is NULL");
        }

        return model;
    }
*/



}
