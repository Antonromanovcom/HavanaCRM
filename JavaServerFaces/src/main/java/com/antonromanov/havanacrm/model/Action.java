package com.antonromanov.havanacrm.model;

/**
 * Класс описывающий все возможные действия, эвенты, реакции на них.
 */
public class Action {

    private int id; //id действия. по нему будем искать и оно же будет храниться в базев поле action
    private ActionType type;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Action(int id, ActionType type) {
        this.id = id;
        this.type = type;
    }

    public Action() {
    }
}
