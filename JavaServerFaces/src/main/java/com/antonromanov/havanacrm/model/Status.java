package com.antonromanov.havanacrm.model;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Status {
    private Integer id;
    private String status;
    private int userId;

    public Status(Integer id, String status) {
        this.id = id;
        this.status = status;

    }
    public Status(Integer id, String status, int userId) {
        this.id = id;
        this.status = status;
        this.userId = userId;

    }
    public Status(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.status = rs.getString("status");
        this.userId = rs.getInt("user_id");
    }
    public Status() {
    }
    public Status(JSONArray statusFromJSON) {

        JSONObject statusToReturn = statusFromJSON.getJSONObject(0);
        System.out.println("Id  -  " + statusToReturn.get("id"));
        this.id = (Integer) statusToReturn.get("id");
        this.status = (String) statusToReturn.get("status");
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    @Override
    public java.lang.String toString() {
        return  this.status;
    }


}
