package com.antonromanov.havanacrm.model;

import java.util.ArrayList;

/**
 * Класс описывающий действия при нажатии пунктов меню сайдбара
 */
public class Actions {


    private ArrayList<Action> actions;


    public Action findAction(int actionid) {

        //Action action = new Action();

        for (Action searchedAction : actions) {
            if (searchedAction.getId() == actionid) {
                //action = searchedAction;
                return searchedAction;
            }
        }
        return null;
    }
}
