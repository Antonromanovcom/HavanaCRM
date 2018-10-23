package com.antonromanov.havanacrm.model;

//import java.sql.Date;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Client {
    private Integer id;
    private String clientName;
    private String phone;
    private String email;
    private Date createdDate;
    private Date birthDay;
    //private String vk; - профиль vk


    public Client(Integer id, String clientN, String phone, String email, Date createdDate, Date birthDay) {
        this.id = id;
        this.clientName = clientN;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
        this.birthDay = birthDay;
    }
    public Client(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.clientName = rs.getString("name");
       // this.phone = rs.getString("name");
        //this.email = rs.getString("email");
        this.createdDate = rs.getDate("date_created");
        this.birthDay = rs.getDate("birthday");
    }
    public Client() {
    }



    public Client(JSONArray clients) {

        JSONObject client = clients.getJSONObject(0);
        System.out.println("Id  -  " + client.get("id"));


        this.id = (Integer) client.get("id");
        //this.id = 1;
        this.clientName = (String) client.get("name");
        //this.phone = "111";
        //this.email = "111";
        this.createdDate = null;
        this.birthDay = null;


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    private java.util.Date sqlDatetoDate(java.sql.Date sqlDate) {
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        System.out.println("Converted value of java.util.Date : " + utilDate);
        return utilDate;
    }

   /**
    //конвертим базовую дату, которая у нас в SQLDate в Java Utils Date
    public java.util.Date getCreatedJavaDate() {
        return this.sqlDatetoDate(createdDate);
    }

    //конвертим базовую дату, которая у нас в SQLDate в Java Utils Date
    public java.util.Date getJavaDeadline() {
        return this.sqlDatetoDate(deadline);
    }

*/

    @Override
    public java.lang.String toString() {
        return  clientName;
    }

    public void setAllToNull() {

        this.id = 0;
        this.clientName = "";
        this.phone = "";
        this.email = "";
        this.createdDate = null;
        this.birthDay = null;

    }
}
