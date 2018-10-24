package com.antonromanov.havanacrm.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuItems {

    private String id;
    //private Action action;
    private String action;
    private Actions actions;
    private String text;
    private String note;
    private String icon;


    public MenuItems(ResultSet rs) throws SQLException {

        this.id = rs.getString("elementid");
        this.action = rs.getString("action");
        this.text = rs.getString("text");
        this.note = " " + rs.getString("note");
        this.icon = rs.getString("icon");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MenuItems(String id, String action, String text, String note) {
        this.id = id;
        this.action = action;
        this.text = text;
        this.note = note;
    }

    public MenuItems(String note) {
        this.id = "11";
        this.text = "222222";
        this.note = note;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
