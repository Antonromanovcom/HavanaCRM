package com.antonromanov.havanacrm.model;

public class Option {


    private Integer id;
    private String optionName;
    private Integer planId;
    private Boolean temporary;



    public Option(Integer id, String optionName, Integer planId) {
        this.id = id;
        this.optionName = optionName;
        this.planId = planId;
        this.temporary = false;
    }

    public Option() {
        this.temporary = false;
    }

    public Option(String optionName, Integer id) {
        this.optionName = optionName;
        this.planId = id;
        this.temporary = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }
}
