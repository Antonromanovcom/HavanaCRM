package com.antonromanov.havanacrm.usersession;

import com.antonromanov.havanacrm.model.MenuItems;
import com.antonromanov.havanacrm.model.Sidebar;
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
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class MainBean {

    /*@EJB
    DAOUtils daoUtils;
    @EJB
    HttpSession session;
    @EJB
    Sidebar sidebar;

*/
   // private DAOUtils daoUtils;
   // private Sidebar sidebar;
    private MenuModel model;





    @PostConstruct
    private void initSampleData(){
        ///daoUtils = new DAOUtils();
        model = new DefaultMenuModel();
        Sidebar sidebar = new Sidebar();
        //HttpSession session = SessionUtils.getSession();
    //    sidebar.setSidebar(daoUtils.getSidebarMenuItems((Integer) session.getAttribute("id")));

        // DefaultSubMenu firstSubmenu = new DefaultSubMenu("Dynamic Submenu");
        DefaultMenuItem item = new DefaultMenuItem("External");
        item.setUrl("http://www.primefaces.org");
        //firstSubmenu.addElement(item);
        model.addElement(item);
    }

    public MainBean() {
    }

   // public Sidebar getSidebar() {
   //     return sidebar;
   // }

  // public void setSidebar(Sidebar sidebar) {
  //      this.sidebar = sidebar;
  //  }

    public MenuModel getModel() {
    //    this.parseModel();
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

   /* public MenuModel parseModel() {

        DefaultMenuItem item;
        //DefaultMenuItem item = new DefaultMenuItem();

        // if (sidebar!=null) {
        for (MenuItems itemfromDB : sidebar.getSidebar()) {
            item = new DefaultMenuItem(itemfromDB.getNote());
            //item.setTitle(itemfromDB.getText());
            item.setUrl("http://www.primefaces.org");
            System.out.println("ЭЛЕМЕНТ МЕНЮ = " + itemfromDB.getNote());
            model.addElement(item);
            //       }
        }

        return model;
    }*/

}
