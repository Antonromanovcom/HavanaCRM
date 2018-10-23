package com.antonromanov.havanacrm.model;

//import java.sql.Date;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class Order {
    private Integer id;
    private Integer client;
    private String clientTestName;
    private Client clientTest;
    private String shortName;
    private String info;
    private Integer type;
    private Integer subtype;
    private Integer plan;
    private int cost;
    private Date createdDate;
    private Date deadline;
    private Integer status;
    private Status orderStatus;


    public Order(Integer id, Integer client, String shortName, String info, Integer type, int cost, Date createdDate, Date deadline, Integer status) {
        this.id = id;
        this.client = client;
        this.shortName = shortName;
        this.info = info;
        this.type = type;
        this.cost = cost;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.status = status;
    }

    public Order(ResultSet rs) throws SQLException {

        /**
         *  ------------------- This is OLD block ------------------------------------
         * this.id = this.IntFromJSON(rs.getString(1), "id");
         this.shortName = this.StringFromJSON(rs.getString(1), "order_short_name");
         this.info = this.StringFromJSON(rs.getString(1), "order_info");
         this.cost = this.IntFromJSON(rs.getString(1), "order_cost");
         //  this.createdDate = rs.getDate("order_create_date");
         // this.deadline = rs.getDate("order_deadline");
         this.clientTest = this.ClientFromJSON(rs.getString(1));
         // this.type = rs.getInt("order_type");
         // this.status = this.IntFromJSON(rs.getString(1), "order_status");
         this.orderStatus = this.StatusFromJSON(rs.getString(1));
         */

        //"t1.id, t1.client, t1.order_short_name, t1.order_info, t1.order_type, t1.sub_type, t1.plan, t1.order_cost, t1.order_create_date, t1.order_deadline, t1.order_status";
        //   1          2           3                     4               5            6          7           8                    9                 10                 11

        this.id = rs.getInt(1);
        this.clientTest = this.ClientFromJSON(rs.getString(2));
        this.shortName = rs.getString(3);
        this.info = rs.getString(4);
        this.type = rs.getInt(5);
        this.subtype = rs.getInt(6);
        this.plan = rs.getInt(7);
        this.cost = rs.getInt(8);
        this.createdDate = rs.getDate(9);
        this.deadline = rs.getDate(10);
        this.orderStatus = this.StatusFromJSON(rs.getString(11));

    }

    private Status StatusFromJSON(String jsonFromDB) {

        Status status = null;
        try {
            status = new Status(this.stringToJSONArray(jsonFromDB));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public Order(String clientTestName) {
        this.clientTestName = clientTestName;

        this.id = 1;
        this.client = 1;
        this.shortName = "111111";
        this.info = "111111";
        this.type = 1;
        this.cost = 1;
        this.createdDate = new Date();
        this.deadline = new Date();
        this.status = 1;
    }

    private JSONArray stringToJSONArray(String strJson) {

        String operateString = "[" + strJson + "]";
        JSONArray jsonArr = new JSONArray(operateString);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject client = jsonArr.getJSONObject(i);
        }
        return jsonArr;

    }

    private Integer IntFromJSON(String strJson, String keyName) {
        JSONObject dataJsonObj = null;
        Integer returnedInt = null;
        try {
            dataJsonObj = new JSONObject(strJson);
            returnedInt = dataJsonObj.getInt(keyName);

            // напечатаем ка временно все
            if ("id".equals(keyName)) {
                System.out.println("------------- Мы вытаскиваем ID из запроса -------------" + returnedInt);
            } else if ("order_cost".equals(keyName)) {
                System.out.println("------------- Мы вытаскиваем ЦЕНУ из запроса -------------" + returnedInt);
            }

            //-------------------------

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedInt;
    }

    private String StringFromJSON(String strJson, String keyName) {
        JSONObject dataJsonObj = null;
        String returnedStr = null;
        try {
            dataJsonObj = new JSONObject(strJson);
            returnedStr = dataJsonObj.getString(keyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedStr;
    }

    private Client ClientFromJSON(String strJson) {

        Client client = null;
        try {
            client = new Client(this.stringToJSONArray(strJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return client;
    }

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    private java.util.Date sqlDatetoDate(java.sql.Date sqlDate) {
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        System.out.println("Converted value of java.util.Date : " + utilDate);
        return utilDate;
    }

    /**
     * //конвертим базовую дату, которая у нас в SQLDate в Java Utils Date
     * public java.util.Date getCreatedJavaDate() {
     * return this.sqlDatetoDate(createdDate);
     * }
     * <p>
     * //конвертим базовую дату, которая у нас в SQLDate в Java Utils Date
     * public java.util.Date getJavaDeadline() {
     * return this.sqlDatetoDate(deadline);
     * }
     */

    public Integer getClientId() {
        return this.getClientTest().getId();
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Client getClientTest() {

        return clientTest;
    }

    public void setClientTest(Client clientTest) {
        this.clientTest = clientTest;
    }

    public String getClientTestName() {
        return clientTestName;
    }

    public void setClientTestName(String clientTestName) {
        this.clientTestName = clientTestName;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderStatusId() {
        return this.orderStatus.getId();
    }

    public void setAllToNull() {

        this.id = 0;
        this.client = null;
        this.shortName = "";
        this.info = "";
        this.type = null;
        this.cost = 0;
        this.createdDate = null;
        this.deadline = null;
        this.status = null;
        this.clientTestName = "";
        this.clientTest = null;
        this.subtype = null;
        this.plan = null;
        this.orderStatus = null;
    }

    public Integer getSubtype() {
        return subtype;
    }

    public void setSubtype(Integer subtype) {
        this.subtype = subtype;
    }

    public Integer getPlan() {
        return plan;
    }

    public void setPlan(Integer plan) {
        this.plan = plan;
    }
}
