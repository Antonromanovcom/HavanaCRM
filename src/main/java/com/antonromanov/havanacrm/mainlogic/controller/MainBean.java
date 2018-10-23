package com.antonromanov.havanacrm.mainlogic.controller;

/**
 * Главный бин приложения, определяющий по сути всю логику
 *
 * TO DO
 *
 * todo 1) прикрутить нормальные скины и css
 * todo-main 2) прикрутить типы заказов
 * todo 3) отчеты
 * todo 4) мобильную версию
 * todo-m 5) регистрацию
 * todo 6) цены/пакеты
 * todo 7) контактную форму для WP
 * todo 8) плагин цен для WP
 * todo 9) календарь
 * todo 10) рассылку по др на почту
 * todo 11) админку
 * todo 12) всякие напоминалки в зависимости от дат и статусов
 *
 *
 *
 *
 */


import com.antonromanov.havanacrm.mainlogic.DAO.MainDAO;
import com.antonromanov.havanacrm.model.*;
import com.antonromanov.havanacrm.usersession.UserSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.MenuActionEvent;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;

import org.primefaces.model.menu.MenuItem;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@SessionScoped
public class MainBean {

    @EJB
    private MainDAO mainDAO;
    private ArrayList<String> tList;

    // ----- переменные для сайдбара -----
    private ArrayList<MenuItems> Sidebar;

    // ----- переменные для заказов -----
    private ArrayList<Order> orders;
    private Order selectedOrder;
    private Order newOrder;

    // ----- переменные для клиента -----
    private Client selectedClient;
    private ArrayList<Client> clients;
    private Client newClient;
    private Client selectedClientForOrder;
    private Map<String, String> clientList = new HashMap<String, String>(); // это такая херня специальная чтобы у нас работал список клиентов
    private String t; // переменная для работы списка выбора клиента при редактировании

    // ----- переменные для типа заказов -----
    private OrderType selectedOrderType;
    private ArrayList<OrderType> orderTypesStatistic;
    private OrderType newOrderType;

    // ----- переменные для типа статусов -----
    private Status selectedStatus;
    private ArrayList<Status> statuses;
    private Status newStatus;
    private String editedOrderStatus; // переменная для работы списка выбора статуса при редактировании
    private String addedOrderStatus; // переменная для работы списка выбора статуса при редактировании

    // ----- ФУНДАМЕНТАЛЬНЫЕ ПЕРЕМЕННЫЕ -----
    private String userid;
    private String currentModule;
    private Boolean isChanged;
    private String messageText;


    public MainBean() {
    }

    @PostConstruct
    public void init() {

        Sidebar = new ArrayList<>();
        Sidebar = getSideBarFromDB();
        this.userid = getUser();

        t = "";
        tList = new ArrayList<>();
        tList.add("11");
        tList.add("22");
        tList.add("33");


        // загружаем энтити
        this.orders = this.getAllOrdersForUser();
        this.clients = this.getAllClientsForUser();
        this.statuses = this.getAllStatusesForUser();
        this.orderTypesStatistic = this.getOrderTypeStatictic();

        currentModule = "orders";
        isChanged = false;

        // поля инициализации
        newOrder = new Order();
        newOrderType = new OrderType();
        newClient = new Client();
        newStatus = new Status();

    }

    private ArrayList<OrderType> getOrderTypeStatictic() {
        return mainDAO.getOrderTypeStatictic(this.getSession().getId());
    }

    public ArrayList<Client> getAllClientsForUser() {
        return mainDAO.getClients(String.valueOf(this.getSession().getId()));
    }

    public ArrayList<MenuItems> getSideBarFromDB() {
        return mainDAO.getSidebarMenuItems(this.getSession().getId());
    }

    public ArrayList<Order> getAllOrdersForUser() {
        return mainDAO.getOrders(String.valueOf(this.getSession().getId()));
    }

    private UserSession getSession() {
        return (UserSession) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("userSession");
    }

    public String getUser() {
        return String.valueOf(this.getSession().getId());
    }
    public String getUserName() {
        return String.valueOf(this.getSession().getUser());
    }

    public ArrayList<MenuItems> getSidebar() {
        return Sidebar;
    }

    public void setSidebar(ArrayList<MenuItems> sidebar) {
        Sidebar = sidebar;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public void onRowSelect(ActionEvent actionEvent) {

        // Редактирование заказа (выделенного)

        // проверяем - выделено ли вообще чо-та....
        if (this.selectedOrder == null) {

            // если ноль (ничего не выделено - ругаемся
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Выделите заказ!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        } else {

            Integer ss = selectedOrder.getId();
            System.out.println("Order id = " + ss);
            this.setCurrentModule("EditOrder");
        }


    }

    public String getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(String currentModule) {
        this.currentModule = currentModule;
    }

    public Boolean getChanged() {
        return isChanged;
    }

    public void setChanged(Boolean changed) {
        isChanged = changed;
    }

    public void formChanged() {
        //  isChanged = true;

        System.out.println("У нас id = " + this.orders.get(1).getShortName());

        if (selectedOrder.getShortName().equals("Съемка еды")) {
            isChanged = false;
        } else {
            isChanged = true;
        }
    }

    public void editOrder(ActionEvent actionEvent) {
        System.out.println("=============== МЫ ПРАВИМ ЗАКАЗ: КЛИЕНТ ===========" + selectedOrder.getClientTest());
        System.out.println("=============== МЫ ПРАВИМ ЗАКАЗ6 СТАТУС ===========" + selectedOrder.getOrderStatus());
        this.mainDAO.editOrder(selectedOrder);
    }

    public void addOrder(ActionEvent actionEvent) {

        if ((newOrder.getShortName() == null) || (newOrder.getClientTest() == null) || (newOrder.getOrderStatus() == null)||
                ("000".equals(this.t)) || ("000".equals(this.addedOrderStatus))) {
            //if ((newOrder.getShortName()==null)){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Заполните поля", "Ошибка добавления заказа");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else {
            this.mainDAO.addOrder(newOrder);

            System.out.println("id клиента (новое) -> " + newOrder.getClientId());
            orders = mainDAO.getOrders(String.valueOf(this.getSession().getId()));
            this.setCurrentModule("orders");
            this.newOrder.setAllToNull();
        }

    }

    public void onOrderEditBack(ActionEvent actionEvent) {
        this.newOrder.setAllToNull();
        this.setCurrentModule("orders");
    }

    public void onClientsBack(ActionEvent actionEvent) {
        this.setCurrentModule("clients");
    }

    public void addOrderForm(ActionEvent actionEvent) {
        this.newOrder.setCreatedDate(new Date());
        this.setCurrentModule("AddOrder");
    }

    public Order getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Order newOrder) {
        this.newOrder = newOrder;
    }

    public void deleteOrder(ActionEvent actionEvent) {
        this.mainDAO.deleteOrder(selectedOrder.getId());
        this.orders = this.getAllOrdersForUser();
    }

    public void update(ActionEvent actionEvent) {
        //clients = mainDAO.getClients(String.valueOf(this.getSession().getId()));
        orders = mainDAO.getOrders(String.valueOf(this.getSession().getId()));
    }

    public void onMainMenuClick(ActionEvent actionEvent) {

        MenuItem menuItemm = ((MenuActionEvent) actionEvent).getMenuItem();
        String screen = menuItemm.getParams().get("action").get(0);
        System.out.println("Параметр - " + screen);
        this.setCurrentModule(screen);


    }

    public void setName(String name) {
        this.setCurrentModule(name);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public Client getNewClient() {
        return newClient;
    }

    public void setNewClient(Client newClient) {
        this.newClient = newClient;
    }

    public void addClientForm(ActionEvent actionEvent) {
        this.setCurrentModule("AddClient");
    }

    public void deleteClient(ActionEvent actionEvent) {

        // System.out.println("selectedOrder.getId()" + selectedOrder.getId());
        this.mainDAO.deleteClient(selectedClient.getId());
        this.clients = this.getAllClientsForUser();
    }

    public void editClientForm(ActionEvent actionEvent) {
        this.setCurrentModule("EditClient");
        // this.setChanged(false);
    }

    public void editClient(ActionEvent actionEvent) {
        this.mainDAO.editClient(selectedClient);
        System.out.println("Мы поменяли имя на...." + selectedClient.getClientName());
        this.setChanged(false);
        this.clients = this.getAllClientsForUser();
        this.setCurrentModule("clients");
    }

    public void addClient(ActionEvent actionEvent) {
        this.mainDAO.addClient(newClient);
        this.clients = this.getAllClientsForUser();
        this.setCurrentModule("clients");
        newClient = new Client();
    }

    public ArrayList<OrderType> getOrderTypesStatistic() {
        return orderTypesStatistic;
    }

    public OrderType getSelectedOrderType() {
        return selectedOrderType;
    }

    public void setSelectedOrderType(OrderType selectedOrderType) {
        this.selectedOrderType = selectedOrderType;
    }

    public void addOrderTypeForm(ActionEvent actionEvent) {
        this.setCurrentModule("AddOrderType");
    }

    public OrderType getNewOrderType() {
        return newOrderType;
    }

    public void setNewOrderType(OrderType newOrderType) {
        this.newOrderType = newOrderType;
    }

    public void addOrderType(ActionEvent actionEvent) {
        this.mainDAO.addOrderType(newOrderType);
        this.setCurrentModule("orders");

    }

    public void nullOrderType(ActionEvent actionEvent) {
        // this.mainDAO.addOrderType(newOrderType);
        newOrderType.setType("");
    }

    public void nullNewClient(ActionEvent actionEvent) {

        newClient.setAllToNull();
    }

    public void nullEditClient(ActionEvent actionEvent) {

        selectedClient.setAllToNull();
    }

    public void editOrderTypeForm(ActionEvent actionEvent) {
        this.setCurrentModule("EditOrderType");
        // this.setChanged(false);
    }

    public void editOrderType(ActionEvent actionEvent) {
        this.mainDAO.editOrderType(selectedOrderType);
        //this.setChanged(false);
    }

    public void deleteOrderType(ActionEvent actionEvent) {
        this.mainDAO.deleteOrderType(selectedOrderType.getId());
        this.orderTypesStatistic = this.mainDAO.getOrderTypeStatictic(this.getSession().getId());
    }

    public void updateOrderTypes(ActionEvent actionEvent) {
        this.orderTypesStatistic = this.mainDAO.getOrderTypeStatictic(this.getSession().getId());
    }

    public ArrayList<Status> getAllStatusesForUser() {
        return mainDAO.getStatuses(this.getSession().getId());
    }

    public Status getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(Status selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public Status getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Status newStatus) {
        this.newStatus = newStatus;
    }

    public void deleteStatus(ActionEvent actionEvent) {
        this.mainDAO.deleteStatus(selectedStatus.getId());
        this.statuses = this.getAllStatusesForUser();
    }

    public void editStatusForm(ActionEvent actionEvent) {
        this.setCurrentModule("EditStatus");
        // this.setChanged(false);
    }

    public void updateStatuses(ActionEvent actionEvent) {
        this.statuses = this.getAllStatusesForUser();
    }

    public void editStatus(ActionEvent actionEvent) {
        this.mainDAO.editStatus(selectedStatus);
        //this.setChanged(false);
    }

    public void nullStatusAdd(ActionEvent actionEvent) {
        newStatus.setStatus("");
    }

    public void nullStatus(ActionEvent actionEvent) {
        selectedStatus.setStatus("");
    }

    public void addStatusForm(ActionEvent actionEvent) {
        this.setCurrentModule("AddStatus");
    }

    public void addStatus(ActionEvent actionEvent) {
        this.mainDAO.addStatus(newStatus);
    }

    //Реакция списка клиентов при редактировании заказа
    public void clientChange(ValueChangeEvent e) {
        System.out.println("мы выбрали элемент списка с id =  " + e.getNewValue());
        String s = (String) e.getNewValue();
        selectedOrder.setClientTest(this.mainDAO.findClientById(Integer.parseInt(s), clients));
        System.out.println("мы выбрали =  " + selectedOrder.getClientTest().getClientName());
    }

    //Реакция списка клиентов при редактировании заказа
    public void statusChangeForOrderEdit(ValueChangeEvent e) {
        System.out.println("мы выбрали статус с id =  " + e.getNewValue());
        String s = (String) e.getNewValue();
        selectedOrder.setOrderStatus(this.mainDAO.findStatusById(Integer.parseInt(s), statuses));
        System.out.println("мы выбрали статус=  " + selectedOrder.getOrderStatus().getStatus());
    }

    public void clientForNewOrderChange(ValueChangeEvent e) {
        System.out.println("Добавляем новый заказ. Мы выбрали элемент списка с id =  " + e.getNewValue());
        String s = (String) e.getNewValue();
        newOrder.setClientTest(this.mainDAO.findClientById(Integer.parseInt(s), clients));
        System.out.println("Добавляем новый заказ. Мы выбрали =  " + newOrder.getClientTest().getClientName());
        System.out.println("Добавляем новый заказ. Мы выбрали (id)=  " + newOrder.getClientTest().getId());
    }

    //Реакция списка статусов при добавлении заказа
    public void statusChangeForOrderAdd(ValueChangeEvent e) {
        System.out.println("мы выбрали статус с id =  " + e.getNewValue());
        String s = (String) e.getNewValue();
        newOrder.setOrderStatus(this.mainDAO.findStatusById(Integer.parseInt(s), statuses));
        System.out.println("мы выбрали статус=  " + newOrder.getOrderStatus().getStatus());
    }

    public Client getSelectedClientForOrder() {
        return selectedClientForOrder;
    }

    public void setSelectedClientForOrder(Client selectedClientForOrder) {
        this.selectedClientForOrder = selectedClientForOrder;
    }

    public ArrayList<String> gettList() {
        return tList;
    }

    public void settList(ArrayList<String> tList) {
        this.tList = tList;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getEditedOrderStatus() {
        return editedOrderStatus;
    }

    public void setEditedOrderStatus(String editedOrderStatus) {
        this.editedOrderStatus = editedOrderStatus;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setNewOrderNull(ActionEvent actionEvent) {
        newOrder.setShortName("");
        newOrder.setInfo("");
        newOrder.setCost(0);
        newOrder.setClientTest(null);
        newOrder.setDeadline(null);
        newOrder.setCreatedDate(new Date());
        newOrder.setOrderStatus(null);
    }

    public String getAddedOrderStatus() {
        return addedOrderStatus;
    }

    public void setAddedOrderStatus(String addedOrderStatus) {
        this.addedOrderStatus = addedOrderStatus;
    }
}
