package com.antonromanov.havanacrm.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuItems {

    private String id;
    private Action action;
    private Actions actions;
    private String text;
    private String note;


    public MenuItems(ResultSet rs) throws SQLException {

        this.id = rs.getString("elementid");
        this.action = actions.findAction(rs.getInt("action"));
        this.text = rs.getString("text");
        this.note = rs.getString("note");;
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

    public MenuItems(String id, Action action, String text, String note) {
        this.id = id;
        this.action = action;
        this.text = text;
        this.note = note;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
