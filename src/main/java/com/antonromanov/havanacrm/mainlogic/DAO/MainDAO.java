package com.antonromanov.havanacrm.mainlogic.DAO;

import com.antonromanov.havanacrm.mainlogic.utils.DataConnect;
import com.antonromanov.havanacrm.model.*;

import javax.ejb.Local;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Local
public interface MainDAO {

    public boolean loginCheck(String login, String password);
    public Integer getUserId(String login, String password);
    public ArrayList<MenuItems> getSidebarMenuItems(int userId);
    public ArrayList<Order> getOrders(String userId);
    public void addOrder(Order order);
    public void deleteOrder(Integer orderId);

    void editOrder(Order order);

    public String Test();

    ArrayList<Client> getClients(String s);

    public void deleteClient(Integer id);

    public void addClient(Client newClient);

    public void editClient(Client selectedClient);

    ArrayList<OrderType> getOrderTypeStatictic(int id);

    void addOrderType(OrderType newOrderType);

    void editOrderType(OrderType selectedOrderType);

    void deleteOrderType(Integer id);

    ArrayList<Status> getStatuses(int id);

    void deleteStatus(Integer id);

    void editStatus(Status selectedStatus);

    void addStatus(Status newStatus);

    Client findClientById(int i, ArrayList<Client> clients);

    Status findStatusById(int i, ArrayList<Status> statuses);
}
