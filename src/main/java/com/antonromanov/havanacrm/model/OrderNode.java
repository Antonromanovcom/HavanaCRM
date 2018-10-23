package com.antonromanov.havanacrm.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderNode {

    private Integer id;
    private String type;
    private int sub; // тип или подтип: 0 - тип, 1 - подтип



    public OrderNode(Integer id, String type, Integer sub) {
        this.id = id;
        this.type = type;
        this.sub = sub;
    }

    public OrderNode() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }
}
