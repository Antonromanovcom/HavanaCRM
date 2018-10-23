package com.antonromanov.havanacrm.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientSource {
    private final int userId;
    private String source;
    private Long id;

    public ClientSource(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.source = rs.getString("source");
        this.userId = rs.getInt("userId");

    }

    public int getUserId() {
        return userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
