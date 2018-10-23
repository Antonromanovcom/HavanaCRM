package com.antonromanov.havanacrm.model;

import java.util.ArrayList;

public class Sidebar {

    private ArrayList<MenuItems> sidebar;


    public ArrayList<MenuItems> getSidebar() {
        return sidebar;
    }

    public void setSidebar(ArrayList<MenuItems> sidebar) {
        this.sidebar = sidebar;
    }

    public Sidebar(ArrayList<MenuItems> sidebar) {
        this.sidebar = sidebar;
    }

    public Sidebar() {

    }




}
