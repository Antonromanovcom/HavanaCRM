package com.antonromanov.havanacrm.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Plan {

    private Integer id;
    private String planName;
    private Integer subTypeId;
    private String description;
    private Integer cost;
    private Boolean isPerHour;
    private ArrayList<Option> options;
    private ArrayList<Option> selectedOptions;
    private String str;
    private Boolean edited;


    public Plan(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.planName = rs.getString("plan");
        this.options = new ArrayList<>();
        this.cost = rs.getInt("cost");

            if  (rs.getString("json")==null)  {

            System.out.println("НЕТ ОПЦИЙ");

        } else {
            this.options.addAll(this.stringToJSONArray(rs.getString("json")));
        }

        this.subTypeId = rs.getInt("parent_subtype_id");

    }

    public Plan() {

    }

    public Plan(ResultSet rs, int i) throws SQLException {
        this.id = rs.getInt("id");
        this.planName = rs.getString("plan_name");
        this.options = new ArrayList<>();
        this.subTypeId = rs.getInt("parent_subtype_id");
        this.isPerHour = rs.getBoolean("is_per_hour");
        this.cost = rs.getInt("cost");
    }

    private ArrayList<Option> stringToJSONArray(String strJson) {

        ArrayList<Option> optionsfromJson = new ArrayList<Option>();
        String formatedJSONString = "{ \"number\": " + strJson + "}";

        JSONObject obj = new JSONObject(formatedJSONString);
        JSONArray jsonMainArr = obj.getJSONArray("number");
        for (int i = 0; i < jsonMainArr.length(); i++) {
            JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
            String name = childJSONObject.getString("optionname");
            System.out.println("name - " + name);
            int id = childJSONObject.getInt("id");
            System.out.println("id - " + id);
            optionsfromJson.add(new Option(id, name, 2));
        }


        for (int i = 0; i < optionsfromJson.size(); i++) {
            System.out.println("Массив готовый - " + optionsfromJson.get(i).getOptionName());
        }


        return optionsfromJson;

    }

    public Plan(Integer id, String planName, Integer subTypeId, String description, Integer cost, Boolean isPerHour) {
        this.id = id;
        this.planName = planName;
        this.subTypeId = subTypeId;
        this.description = description;
        this.cost = cost;
        this.isPerHour = isPerHour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Integer subTypeId) {
        this.subTypeId = subTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getPerHour() {
        return isPerHour;
    }

    public void setPerHour(Boolean perHour) {
        isPerHour = perHour;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public void deleteOption(Integer id) {
        this.options.remove(id);
    }

    public void newOption(Option option) {
        this.options.add(option);
    }

    public Integer getLastId() {
        int max = 0;
        for(int i=0; i<this.options.size(); i++){
            if(this.options.get(i).getId() > max){
                max = this.options.get(i).getId();
            }
        }
        return max+1;
    }

    public ArrayList<Option> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(ArrayList<Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public Boolean isEdited() {

        Integer count = 0;

        for (Option option : this.options) {

            System.out.println(option.getOptionName() + " - " + option.getTemporary() + ". count = " + count);

            if (option.getTemporary()==true) {
                count=count+1;
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

}
