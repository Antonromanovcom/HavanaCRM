package com.antonromanov.havanacrm.mainlogic.utils;

import com.antonromanov.havanacrm.mainlogic.DAO.MainDAOimpl;
import com.antonromanov.havanacrm.model.OrderNode;
import com.antonromanov.havanacrm.model.OrderSubType;
import com.antonromanov.havanacrm.model.OrderType;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeParse {

    //MainDAOimpl mainDAOimpl = new MainDAOimpl();
    ArrayList<TreeNode> treeBranches = new ArrayList<TreeNode>();
    TreeNode root = new DefaultTreeNode("root", null);
    HashMap<String, String> rowKey = new HashMap<>();
    private String Treekey;
    private String DBkey;
    private String DBSubKey;

    public void reInitialize() {
        root = new DefaultTreeNode("root", null);
    }
    public TreeNode doParse(ArrayList<OrderType> orderTypes, ArrayList<OrderSubType> orderSubTypes) {

//root = null;
        for (OrderType orderType : orderTypes) {

            TreeNode orderTypeNodes = new DefaultTreeNode("ORDERTYPE", new OrderNode(orderType.getId(), orderType.getType(), 0), root);
            Treekey = orderTypeNodes.getRowKey();
            DBkey = String.valueOf(orderType.getId());
            rowKey.put(DBkey + ":0", Treekey);

            System.out.println("Order type = " + orderType.getType());
            for (OrderSubType orderSubType : orderSubTypes) {
                if (orderSubType.getParentId() == orderType.getId()) {

                    TreeNode orderSubTypeNodes = new DefaultTreeNode("ORDERSUBTYPE", new OrderNode(orderSubType.getId(), orderSubType.getSubtype(), 1), orderTypeNodes);
                    Treekey = orderSubTypeNodes.getRowKey();
                    DBSubKey = String.valueOf(orderType.getId()) + ":" + String.valueOf(orderSubType.getId());
                    rowKey.put(DBSubKey, Treekey);
                    System.out.println("subtypes = " + orderSubType.getSubtype());
                } else {
                    //    System.out.println("ID НЕ СОВПАЛИ ----- " + orderSubType.getParentId() + " : " + orderType.getId());
                }
            }

            orderTypeNodes = null;
        }

        for (HashMap.Entry<String, String> entry : rowKey.entrySet()) {
            System.out.println(entry.getKey() + " < - > " + entry.getValue());
        }

        return root;
    }


    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public HashMap<String, String> getRowKey() {
        return rowKey;
    }

    public void setRowKey(HashMap<String, String> rowKey) {
        this.rowKey = rowKey;
    }

    public Integer getOrderTypeNodeByTreeRowKey(String key) {
        //OrderNode type = new OrderNode();
        Integer result = -1;

        for (HashMap.Entry<String, String> entry : rowKey.entrySet()) {
            //System.out.println(entry.getKey()+" < - > "+entry.getValue());

            if (key.equals(entry.getValue())) {

                System.out.println("Нашли - " + entry.getKey());
                System.out.println("Нашли - " + (entry.getKey()).substring(0, 1));

                int index = (entry.getKey()).indexOf(":");
                String lastChar = (entry.getKey()).substring(index + 1, index + 2);

                System.out.println("Нашли (right)- " + lastChar);

                if (Integer.parseInt(lastChar) == 0) {
                    System.out.println("ЭТО ТИП");
                    result = 0;
                } else {
                    System.out.println("ЭТО ПОДТИП");
                    result = -1;
                }
            }
        }
        return result;
    }

    public OrderType getOrderTypeByTreeRowKey(String key, ArrayList<OrderType> orderTypes) {

        OrderType result = new OrderType();
        for (HashMap.Entry<String, String> entry : rowKey.entrySet()) {
            if (key.equals(entry.getValue())) {
                int index = (entry.getKey()).indexOf(":");
                String keyFound = (entry.getKey()).substring(0, index);
                System.out.println("Нашли (beer)- " + keyFound);

                for (OrderType orderType : orderTypes) {
                    if (orderType.getId() == Integer.parseInt(keyFound)) {
                        result = orderType;
                    }
                }
            }
        }
        return result;
    }


    public OrderSubType getOrderSubTypeByTreeRowKey(String key, ArrayList<OrderSubType> orderSubTypes) {

        OrderSubType result = new OrderSubType();
        for (HashMap.Entry<String, String> entry : rowKey.entrySet()) {
            if (key.equals(entry.getValue())) {
                int index = (entry.getKey()).indexOf(":");
                String keyFound = (entry.getKey()).substring(index+1, (entry.getKey()).length());
                System.out.println("Нашли (vodkaaaaa)- " + keyFound);

                for (OrderSubType orderSubType : orderSubTypes) {
                    System.out.println("Наши САБТИПЫ - " + orderSubType.getId() + " - "+ orderSubType.getSubtype());
                    if (orderSubType.getId() == Integer.parseInt(keyFound)) {
                   //     if (orderSubType.getId() == 2) {
                        result = orderSubType;
                        System.out.println("Нашли (RUM)- " + orderSubType.getSubtype());
                    }
                }
            }
        }
        return result;

    }
}
