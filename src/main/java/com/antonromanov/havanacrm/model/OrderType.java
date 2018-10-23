package com.antonromanov.havanacrm.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OrderType {
    private Integer id;
    private String type;
  //  private int currentCount; //типа текущая статистика по используемым тегам (количество), которую мы тыбзим из базы


    public OrderType(Integer id, String type) {
        this.id = id;
        this.type = type;

    }

    public OrderType(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.type = rs.getString("type");
        //this.currentCount = rs.getInt("test");
    }

    public OrderType() {
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

   // public int getCurrentCount() {
  //      return currentCount;
  //  }

  //  public void setCurrentCount(int currentCount) {
  //      this.currentCount = currentCount;
  //  }
}
