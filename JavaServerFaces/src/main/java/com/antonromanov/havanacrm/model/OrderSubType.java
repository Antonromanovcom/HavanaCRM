package com.antonromanov.havanacrm.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderSubType {

    private Integer id;
    private Integer parentId;
    private String subtype;

    public OrderSubType(Integer id, Integer parentId, String subtype) {
        this.id = id;
        this.parentId = parentId;
        this.subtype = subtype;
    }
    public OrderSubType(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.subtype = rs.getString("subtype_name");
        this.parentId = rs.getInt("parent_type_id");
    }
    public OrderSubType() {
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSubtype() {
        return subtype;
    }
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}
