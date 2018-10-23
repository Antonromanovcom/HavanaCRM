package com.antonromanov.havanacrm.mainlogic.DAO;

import com.antonromanov.havanacrm.model.*;
import com.antonromanov.havanacrm.usersession.Login;

import javax.ejb.Local;
import java.util.ArrayList;
import java.util.Map;

@Local
public interface MainDAO {

    public boolean loginCheck(String login, String password);
    public Integer getUserId(String login, String password);
    public ArrayList<MenuItems> getSidebarMenuItems(int userId);
    public ArrayList<Order> getOrders(String userId);
    void addOrder(Order order, Integer userId);
    public void deleteOrder(Integer orderId);
    void editOrder(Order order);
    public String Test();
    ArrayList<Client> getClients(String s);
    public void deleteClient(Integer id);
    void addClient(Client newClient, Integer userId);

    public void editClient(Client selectedClient);

    ArrayList<OrderType> getOrderTypeStatictic(int id);

    void addOrderType(OrderType newOrderType, Integer userId);

    void editOrderType(OrderType selectedOrderType);

    void deleteOrderType(Integer id);

    ArrayList<Status> getStatuses(int id);

    void deleteStatus(Integer id);

    void editStatus(Status selectedStatus);

    void addStatus(Status newStatus, Integer userId);

    Client findClientById(int i, ArrayList<Client> clients);

    Status findStatusById(int i, ArrayList<Status> statuses);

    //Ищем источник клиента по id в списке уже ранее загруженных из БД
    ClientSource findSourceById(Long i, ArrayList<ClientSource> statuses);

    Map getOrderTypeMap(int id);

    ArrayList<OrderSubType> getOrderSubType(int id);

    ArrayList<OrderSubType> getAllOrderSubType();

    ArrayList<Plan> getPlansById(Integer id);

    ArrayList<Plan> getAllPlansAndOptions(Integer id);

    void editOrderSubType(OrderSubType selectedNodeObject);

    void deleteOrderSubType(Integer id);

    void addOrderSubType(OrderSubType newOrdeSubType, Integer id);

    void editSelectedOption(Option selectedOption);

    void deleteSelectedOption(Option selectedOption);

    void deletePlan(Plan plan);

    void editPlan(Plan selectedPlan);

    void addPlan(Plan newPlan);

    String getClientSourceTextByClientId(Integer id);

    void createNewUser(Login newUser);

    ArrayList<ClientSource> getSources(String s);
}
