package com.antonromanov.havanacrm.mainlogic.controller;

/**
 * Главный бин приложения, определяющий по сути всю логику
 * <p>
 * TO DO
 * <p>
 * todo-main 1) прикрутить нормальные скины и css
 * todo-done 2) прикрутить типы заказов
 * todo 3) отчеты
 * todo 4) мобильную версию
 * todo-done 5) регистрацию
 * todo-done 6) цены/пакеты
 * todo 7) контактную форму для WP
 * todo 8) плагин цен для WP
 * todo 9) календарь
 * todo 11) админку
 * todo 12) каскадное удаление статусов/типов клиентов - решить чо с этим делать
 * todo 12) всякие напоминалки в зависимости от дат и статусов
 * todo 13) почистить код
 */


import com.antonromanov.havanacrm.mainlogic.DAO.MainDAO;
import com.antonromanov.havanacrm.mainlogic.utils.TreeParse;
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
import javax.xml.transform.Source;
import java.util.*;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.MenuItem;

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
    private String testT; // переменная для работы списка выбора клиента при редактировании
    private ArrayList<ClientSource> clientsSources; //типы источников клиентов


    // ----- переменные для типа заказов -----
    private OrderType selectedOrderType;
    private OrderType orderType4Add;
    private OrderType selectedInAddSubOrderOrderType;
    private ArrayList<OrderType> orderTypesStatistic;
    private OrderType newOrderType;
    private String selectedMainOrderType;
    private int selectedMainOrderTypeId;
    private HashMap MainOrderTypeBusket;
    private TreeParse treeParse;
    private TreeNode ordersTypeTreeNode;
    private TreeNode selectedNode;
    private OrderNode selectedNodeObject;

    // ----- переменные для подтипа заказов -----
    private OrderSubType selectedOrderSubType;
    private ArrayList<OrderSubType> orderSubTypes;
    private ArrayList<OrderSubType> allOrderSubTypes;
    private OrderSubType newOrdeSubType;
    private OrderSubType editableOrderSubType;
    private OrderSubType OrderSubType4Add;

    // ----- переменные для планов -----
    private Plan selectedPlan;
    private ArrayList<Plan> plans;
    private Plan newPlan;
    private Plan orderPlan4Add;
    Plan selected4AddPlan;

    // ----- переменные для опций планов -----
    private List<Option> options;
    private Option selectedOption;
    private Option newOption;

    //@ManagedProperty("#{optionService}")
    // private OptionService service;

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
    private Integer hoursCount; // количество часов съемки для подсчета стоимости заказа


    public MainBean() {
    }

    @PostConstruct
    public void init() {

        Sidebar = new ArrayList<>();
        Sidebar = getSideBarFromDB();
        this.userid = getUser();

        testT = "";
        selectedMainOrderType = "";
        tList = new ArrayList<>();
        tList.add("11");
        tList.add("22");
        tList.add("33");
        hoursCount = 0;


        // загружаем энтити
        this.orders = this.getAllOrdersForUser();
        this.clients = this.getAllClientsForUser();
        this.fillClientsSources();
        this.statuses = this.getAllStatusesForUser();
        this.orderTypesStatistic = this.getOrderTypeStatictic();
        this.clientsSources = this.getClientsSources();
        this.orderSubTypes = new ArrayList<>();

        this.plans = new ArrayList<>();
        this.allOrderSubTypes = this.getAllOrderSubTypes(); // это типа все подтипы. пока грузим полностью все, но потом надо будет конечно фильтровать по типу заказа
        this.MainOrderTypeBusket = new HashMap<String, String>();
        this.MainOrderTypeBusket.putAll(this.getOrderTypeMap());

        currentModule = "orders";
        isChanged = false;

        // поля инициализации
        newOrder = new Order();
        OrderSubType4Add = new OrderSubType();
        newPlan = new Plan();
        selectedNodeObject = new OrderNode();
        selectedPlan = new Plan();
        orderPlan4Add = new Plan();
        selected4AddPlan = new Plan();
        selectedPlan.setEdited(false);
        selectedOption = new Option();
        selectedInAddSubOrderOrderType = new OrderType();
        newOrderType = new OrderType();
        orderType4Add = new OrderType();
        newOrdeSubType = new OrderSubType();
        newOption = new Option();
        newClient = new Client();
        newStatus = new Status();
        treeParse = new TreeParse();
        ordersTypeTreeNode = treeParse.doParse(orderTypesStatistic, allOrderSubTypes);
    }

    private void fillClientsSources() {
        if (!this.clients.isEmpty()) {
            for (Client client : this.clients) {
                client.setWhereClientFromText(mainDAO.getClientSourceTextByClientId(client.getId()));
            }
        }
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

            // Пихаем значение в список ТИП
            this.setOrderType4Add(this.getOrderTypebyId(orderTypesStatistic, this.selectedOrder));

            System.out.println("А чему у нас равен selectedOrder.getType() - " + selectedOrder.getType());

            // На основании выбранного типа грузим подтипы так как они у нас в ПостКонструкт не подгружаются
            this.orderSubTypes = this.mainDAO.getOrderSubType(selectedOrder.getType());


            if (this.orderSubTypes.isEmpty()) {
                System.out.println("Хахахахааха..... Пиздец.... ");
                this.setOrderSubType4Add(null);
            } else {

                // Пихаем значение в список ПодТип
                this.setOrderSubType4Add(this.getOrderSubTypebyId(orderSubTypes, this.selectedOrder));
                System.out.println("мы ставим подтип - " + this.getOrderSubType4Add().getSubtype());

            }


            // На основании выбранного подтипа грузим Планы так как они у нас в ПостКонструкт не подгружаются
            if ((selectedOrder.getSubtype() == null) || (selectedOrder.getSubtype() == 0)) {

                this.plans = this.mainDAO.getPlansById(this.orderSubTypes.get(1).getId());
                System.out.println("мы пытаемся подставить любой первый ПЛАН из БД (из подтипа с индексом 1).");
            } else {

                this.plans = this.mainDAO.getPlansById(selectedOrder.getSubtype());
            }


            // Пихаем значение в список Планы
            this.orderPlan4Add = this.getOrderPlanbyId(this.plans, this.selectedOrder);
            //          this.selected4AddPlan = new Plan();


            this.setCurrentModule("EditOrder");
        }


    }

    private Plan getOrderPlanbyId(ArrayList<Plan> plans, Order selectedOrder) {
        Plan result = new Plan();

        if ((selectedOrder.getPlan() == null) || (selectedOrder.getPlan() == 0)) {

            System.out.println("ПОДТИП - ноль");
            System.out.println("РАЗМЕР - " + plans.size());
            result = plans.get(plans.size() - 1);

        } else {
            for (Plan plan : plans) {
                System.out.println("Order subtype from db = " + selectedOrder.getPlan());
                if (plan.getId() == selectedOrder.getPlan()) {
                    System.out.println("Нашли plan = " + plan.getPlanName());
                    result = plan;
                }

                System.out.println("Order plan in cycle = " + plan.getId());
            }

        }
        return result;
    }

    private OrderSubType getOrderSubTypebyId(ArrayList<OrderSubType> orderSubTypes, Order selectedOrder) {
        OrderSubType result = new OrderSubType();


        if ((selectedOrder.getSubtype() == null) || (selectedOrder.getSubtype() == 0)) {

            System.out.println("ПОДТИП - ноль");
            result = orderSubTypes.get(1);

        } else {
            for (OrderSubType orderSubType : orderSubTypes) {
                System.out.println("Order subtype from db = " + selectedOrder.getSubtype() + " : " + selectedOrder.getSubtype());
                if (orderSubType.getId() == selectedOrder.getSubtype()) {
                    System.out.println("Нашли под тип= " + orderSubType.getSubtype());
                    result = orderSubType;
                }

                System.out.println("Order subtype in cycle = " + orderSubType.getId());
            }
        }
        return result;
    }

    private OrderType getOrderTypebyId(ArrayList<OrderType> orderTypesStatistic, Order selectedOrder) {
        OrderType result = new OrderType();

        for (OrderType orderType : orderTypesStatistic) {
            // System.out.println("Order type from db = " + selectedOrder.getType() + " : "+ selectedOrder.getType());

            if (orderType.getId() == selectedOrder.getType()) {
                //   System.out.println("Нашли = " + orderType.getType());
                result = orderType;
            }

            //  System.out.println("Order type in cycle = " + orderType.getId());
        }

        return result;
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
        System.out.println("=============== МЫ ПРАВИМ ЗАКАЗ СТАТУС ===========" + selectedOrder.getOrderStatus());
        System.out.println("=============== МЫ ПРАВИМ ЗАКАЗ ПЛАН ===========" + this.selected4AddPlan.getPlanName());

        if (this.selected4AddPlan.getPlanName() == null) {
            if (this.orderPlan4Add != null) {
                this.selected4AddPlan = this.orderPlan4Add;
            }
        }


        selectedOrder.setType(this.getOrderType4Add().getId());
        selectedOrder.setSubtype(this.getOrderSubType4Add().getId());
        selectedOrder.setPlan(this.selected4AddPlan.getId());


        this.mainDAO.editOrder(selectedOrder);

        this.setCurrentModule("orders");
        this.selectedOrder.setAllToNull();

        this.setOrderType4Add(null);
        this.setOrderSubType4Add(null);
        this.orderPlan4Add = new Plan();
        this.selected4AddPlan = new Plan();

    }

    public void addOrder(ActionEvent actionEvent) {


        if ((newOrder.getShortName() == null) || (newOrder.getClientTest() == null) || (newOrder.getOrderStatus() == null) ||
                ("000".equals(this.testT)) || ("000".equals(this.addedOrderStatus))) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Заполните поля", "Ошибка добавления заказа");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        } else {

            newOrder.setType(this.getOrderType4Add().getId());
            newOrder.setSubtype(this.getOrderSubType4Add().getId());
            newOrder.setPlan(this.selected4AddPlan.getId());
            this.mainDAO.addOrder(newOrder, ((this.getSession().getId())));

            System.out.println("id клиента (новое) -> " + newOrder.getClientId());

            this.setCurrentModule("orders");

            // ОЧИЩАЕМ ВСЕ ПОЛЯ, КОТОРЫЕ БЫЛИ НУЖНЫ ПРИ ДОБАВЛЕНИИ
            this.newOrder.setAllToNull();
            this.setOrderType4Add(null);
            this.setOrderSubType4Add(null);
            this.orderPlan4Add = new Plan();
            this.selected4AddPlan = new Plan();
            this.addedOrderStatus = new String();
            this.hoursCount = null;



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
        this.fillClientsSources();
        this.setCurrentModule("clients");
    }

    public void addClient(ActionEvent actionEvent) {
        this.mainDAO.addClient(newClient, this.getSession().getId());
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

    public void addOrderSubTypeForm(ActionEvent actionEvent) {

        Integer type;

        // надо определить, что мы выбрали: тип заказа, подтип заказа
        type = this.treeParse.getOrderTypeNodeByTreeRowKey(this.selectedNode.getRowKey());

        if (type == 0) {
            selectedOrderType = this.treeParse.getOrderTypeByTreeRowKey(this.selectedNode.getRowKey(), this.orderTypesStatistic);
            this.setCurrentModule("AddOrderSubType");
        } else {
            System.out.println("Тут типа должно быть сообшение а-ля выбирете тип заказа, а не подтип");
        }
    }

    public OrderType getNewOrderType() {
        return newOrderType;
    }

    public void setNewOrderType(OrderType newOrderType) {
        this.newOrderType = newOrderType;
    }

    public void addOrderType(ActionEvent actionEvent) {
        this.mainDAO.addOrderType(newOrderType, this.getSession().getId());
        this.setCurrentModule("orders");

    }

    public void addOrderSubType(ActionEvent actionEvent) {
        this.mainDAO.addOrderSubType(this.newOrdeSubType, this.selectedOrderType.getId());
        this.setCurrentModule("orderstype");

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

        Integer type;

        // надо определить, что мы выбрали: тип заказа, подтип заказа
        type = this.treeParse.getOrderTypeNodeByTreeRowKey(this.selectedNode.getRowKey());

        if (type == 0) {
            selectedOrderType = this.treeParse.getOrderTypeByTreeRowKey(this.selectedNode.getRowKey(), this.orderTypesStatistic);
            this.setCurrentModule("EditOrderType");
        } else {
            selectedOrderSubType = this.treeParse.getOrderSubTypeByTreeRowKey(this.selectedNode.getRowKey(), this.allOrderSubTypes);
            this.setCurrentModule("EditOrderSubType");
        }
    }

    public void onOrderTypeBack(ActionEvent actionEvent) {
        this.setCurrentModule("orderstype");
    }

    public void editOrderType(ActionEvent actionEvent) {
        this.mainDAO.editOrderType(selectedOrderType);
        this.setCurrentModule("orderstype");
        selectedOrderType = null;
    }

    public void editOrderSubType(ActionEvent actionEvent) {
        this.mainDAO.editOrderSubType(selectedOrderSubType);
        this.setCurrentModule("orderstype");
        selectedOrderSubType = null;

    }

    public void deleteOrderType(ActionEvent actionEvent) {
        // this.mainDAO.deleteOrderType(selectedOrderType.getId());
        // this.orderTypesStatistic = this.mainDAO.getOrderTypeStatictic(this.getSession().getId());

        Integer type;

        // надо определить, что мы выбрали: тип заказа, подтип заказа
        type = this.treeParse.getOrderTypeNodeByTreeRowKey(this.selectedNode.getRowKey());

        if (type == 0) {
            selectedOrderType = this.treeParse.getOrderTypeByTreeRowKey(this.selectedNode.getRowKey(), this.orderTypesStatistic);
            this.mainDAO.deleteOrderType(selectedOrderType.getId());

            ordersTypeTreeNode = null;
            treeParse.reInitialize();
            this.orderTypesStatistic = this.mainDAO.getOrderTypeStatictic(this.getSession().getId());
            this.allOrderSubTypes = mainDAO.getAllOrderSubType();
            ordersTypeTreeNode = treeParse.doParse(orderTypesStatistic, allOrderSubTypes);
            System.out.println("удалили тип");

        } else {
            selectedOrderSubType = this.treeParse.getOrderSubTypeByTreeRowKey(this.selectedNode.getRowKey(), this.allOrderSubTypes);
            this.mainDAO.deleteOrderSubType(selectedOrderSubType.getId());

            ordersTypeTreeNode = null;
            treeParse.reInitialize();
            this.orderTypesStatistic = this.mainDAO.getOrderTypeStatictic(this.getSession().getId());
            this.allOrderSubTypes = mainDAO.getAllOrderSubType();
            ordersTypeTreeNode = treeParse.doParse(orderTypesStatistic, allOrderSubTypes);
            System.out.println("удалили подтип");

        }

    }

    public void updateOrderTypes(ActionEvent actionEvent) {

        ordersTypeTreeNode = null;
        treeParse.reInitialize();
        this.orderTypesStatistic = this.mainDAO.getOrderTypeStatictic(this.getSession().getId());
        this.allOrderSubTypes = mainDAO.getAllOrderSubType();
        ordersTypeTreeNode = treeParse.doParse(orderTypesStatistic, allOrderSubTypes);

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
        System.out.println("Новый статус" + newStatus);
        System.out.println("Юзер" + this.getSession().getId());
        this.mainDAO.addStatus(newStatus, this.getSession().getId());
        this.newOrder.setAllToNull();
        this.setCurrentModule("statuses");
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

    //Реакция списка источников клиента при редактировании клиента
    public void sourceChangeForClientEdit(ValueChangeEvent e) {

        Long s = (Long) e.getNewValue();
        selectedClient.setWhereClientFrom(this.mainDAO.findSourceById(s, clientsSources).getId());

    }



    public void clientForNewOrderChange(ValueChangeEvent e) {


        Integer i = ((Client) e.getNewValue()).getId();


        this.newOrder.setClient(i);


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

    public ArrayList<Plan> getPlans() {
        return plans;
    }

    public String getAddedOrderStatus() {
        return addedOrderStatus;
    }

    public void setAddedOrderStatus(String addedOrderStatus) {
        this.addedOrderStatus = addedOrderStatus;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Plan getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(Plan selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    public String getSelectedMainOrderType() {
        return selectedMainOrderType;
    }

    public void setSelectedMainOrderType(String selectedMainOrderType) {
        this.selectedMainOrderType = selectedMainOrderType;
    }

    public int getSelectedMainOrderTypeId() {
        return selectedMainOrderTypeId;
    }

    public void setSelectedMainOrderTypeId(int selectedMainOrderTypeId) {
        this.selectedMainOrderTypeId = selectedMainOrderTypeId;
    }

    public HashMap getMainOrderTypeBusket() {
        return MainOrderTypeBusket;
    }

    public void setMainOrderTypeBusket(HashMap mainOrderTypeBusket) {
        MainOrderTypeBusket = mainOrderTypeBusket;
    }

    public Map getOrderTypeMap() {
        return mainDAO.getOrderTypeMap(this.getSession().getId());
    }

    public void onMainOrderTypeRowSelect(SelectEvent event) {
        //int rownum = orderTypesStatistic.indexOf((OrderType) event.getObject());
        //int rownum = orderTypesStatistic.indexOf((OrderType) event.getObject());
        int rownum = orderTypesStatistic.get(orderTypesStatistic.indexOf((OrderType) event.getObject())).getId();
        System.out.println("Order id = " + rownum);
        //  FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Выделено", String.valueOf(rownum));
        //  RequestContext.getCurrentInstance().showMessageInDialog(message);
        this.orderSubTypes = this.getOrderSubType(rownum);
    }

    public ArrayList<OrderSubType> getOrderSubType(Integer selectedOrderType) {

        return mainDAO.getOrderSubType(selectedOrderType);
    }

    public ArrayList<OrderSubType> getOrderSubTypes() {
        return orderSubTypes;
    }

    public OrderSubType getSelectedOrderSubType() {
        return selectedOrderSubType;
    }

    public void setSelectedOrderSubType(OrderSubType selectedOrderSubType) {
        this.selectedOrderSubType = selectedOrderSubType;
    }

    public TreeNode getOrdersTypeTreeNode() {
        return ordersTypeTreeNode;
    }

    public void onTreeNodeOrdersTypeSelect(ActionEvent actionEvent) {

        System.out.println("Order id = " + selectedNodeObject.getType());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Выделено", selectedNodeObject.getType());
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        //this.orderSubTypes = this.getOrderSubType(rownum);
    }

    public ArrayList<OrderSubType> getAllOrderSubTypes() {
        return mainDAO.getAllOrderSubType();
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public OrderNode getSelectedNodeObject() {
        return selectedNodeObject;
    }

    public void setSelectedNodeObject(OrderNode selectedNodeObject) {
        this.selectedNodeObject = selectedNodeObject;
    }

    public String editAction() {


        if (selectedNodeObject.getSub() == 1) {
            this.plans = this.mainDAO.getAllPlansAndOptions(selectedNodeObject.getId());
            System.out.println("А id у нас - " + selectedNodeObject.getId());

        } else {
            System.out.println("А id у нас - " + selectedNodeObject.getId());
            System.out.println("А ТИП У НАС  - " + selectedNodeObject.getSub());
        }
        return testT;
    }

    private ArrayList<Plan> getPlansById(Integer id) {
        return mainDAO.getPlansById(id);
    }

    public OrderType getSelectedInAddSubOrderOrderType() {
        return selectedInAddSubOrderOrderType;
    }

    public void setSelectedInAddSubOrderOrderType(OrderType selectedInAddSubOrderOrderType) {
        this.selectedInAddSubOrderOrderType = selectedInAddSubOrderOrderType;
    }

    public OrderSubType getNewOrdeSubType() {
        return newOrdeSubType;
    }

    public void setNewOrdeSubType(OrderSubType newOrdeSubType) {
        this.newOrdeSubType = newOrdeSubType;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String editOption() {
        this.setCurrentModule("EditOption");
        return "1";
    }

    public void editSelectedOption(ActionEvent actionEvent) {
        this.mainDAO.editSelectedOption(this.selectedOption);
        this.setCurrentModule("orderstype");
    }

    public void deleteOption(ActionEvent actionEvent) {
        this.mainDAO.deleteSelectedOption(this.selectedOption);
        this.setCurrentModule("orderstype");
    }

    public void deletePlan(Plan plan) {
        this.mainDAO.deletePlan(plan);
        System.out.println("Мы хотим удалить план - " + plan.getPlanName());
        this.plans = this.mainDAO.getAllPlansAndOptions(selectedNodeObject.getId());
//        this.setCurrentModule("orderstype");
    }

    public void editPlan(Plan plan) {

        this.setSelectedPlan(plan);
        this.setCurrentModule("EditPlan");
    }

    public Option getNewOption() {
        return newOption;
    }

    public void setNewOption(Option newOption) {
        this.newOption = newOption;
    }

    public void editExistingPlan(ActionEvent actionEvent) {

        this.mainDAO.editPlan(this.selectedPlan);
        this.selectedPlan = new Plan();
        this.newOption = new Option();

//}else {

//}

        //this.newOrder.setAllToNull();
        this.setCurrentModule("orderstype");
    }

    public void addNewOptionDuringEditingPlan(ActionEvent actionEvent) {
        newOption.setTemporary(true);
        newOption.setPlanId(this.selectedPlan.getId());
        newOption.setId(this.selectedPlan.getLastId());
        this.selectedPlan.newOption(newOption);

        if (this.selectedPlan.isEdited()) this.selectedPlan.setEdited(true);


        this.newOption = new Option();
    }

    public void deleteOptionDuringEditingPlan(ActionEvent actionEvent) {

        for (Option option : this.selectedPlan.getSelectedOptions()) {
            System.out.println(option.getOptionName());
            this.mainDAO.deleteSelectedOption(option);
        }
        // /this.selectedPlan.newOption(newOption);
        //this.newOption = new Option();
    }

    public void addNewPlan(ActionEvent actionEvent) {

        Integer type;

        // надо определить, что мы выбрали: тип заказа, подтип заказа
        type = this.treeParse.getOrderTypeNodeByTreeRowKey(this.selectedNode.getRowKey());

        if (type == 0) {
            // ничо не делаем. Это не субтип
        } else {
            selectedOrderSubType = this.treeParse.getOrderSubTypeByTreeRowKey(this.selectedNode.getRowKey(), this.allOrderSubTypes);
            this.newPlan.setSubTypeId(selectedOrderSubType.getId());
            this.setCurrentModule("AddPlan");
        }
    }

    public Plan getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(Plan newPlan) {
        this.newPlan = newPlan;
    }

    public void addPlan(ActionEvent actionEvent) {

        this.mainDAO.addPlan(newPlan);
        this.newPlan = new Plan();
        this.setCurrentModule("orderstype");

    }

    public OrderType getOrderType4Add() {
        return orderType4Add;
    }

    public void setOrderType4Add(OrderType orderType4Add) {
        this.orderType4Add = orderType4Add;
    }

    public OrderSubType getOrderSubType4Add() {
        return OrderSubType4Add;
    }

    public void setOrderSubType4Add(OrderSubType orderSubType4Add) {
        OrderSubType4Add = orderSubType4Add;
    }

    //Реакция списка типа заказов при добавлении заказа
    public void selectOrderType4NewOrder(ValueChangeEvent e) {
        //public void selectOrderType4NewOrder(AjaxBehaviorEvent event) {


        String s = ((OrderType) e.getNewValue()).getType();
        Integer i = ((OrderType) e.getNewValue()).getId();

        System.out.println("мы выбрали тип=  " + s);
        System.out.println("мы выбрали тип (id)=  " + i);

        this.orderSubTypes = this.mainDAO.getOrderSubType(i);
    }

    public Plan getOrderPlan4Add() {
        return orderPlan4Add;
    }

    //Реакция списка типа заказов при добавлении заказа
    public void selectPlan4NewOrder(ValueChangeEvent e) {

        String s = ((OrderSubType) e.getNewValue()).getSubtype();
        Integer i = ((OrderSubType) e.getNewValue()).getId();

        System.out.println("мы выбрали ПОД-тип=  " + s);
        System.out.println("мы выбрали ПОД-тип (id)=  " + i);

        this.plans = this.mainDAO.getPlansById(i);
    }

    public void setOrderPlan4Add(Plan orderPlan4Add) {
        this.orderPlan4Add = orderPlan4Add;
    }

    //Реакция списка планов при добавлении заказа (финальный подсчет стоимости)
    public void selectFinalOrderTypeWithCost4NewOrder(ValueChangeEvent e) {

        String s = ((Plan) e.getNewValue()).getPlanName();
        Integer i = ((Plan) e.getNewValue()).getId();
        selected4AddPlan = ((Plan) e.getNewValue());

        //System.out.println("мы выбрали Plan =  " + s);
        System.out.println("мы выбрали Plan (id)=  " + i);
        System.out.println("мы выбрали Plan =  " + selected4AddPlan.getPlanName());

        // сначала надо определить он почасовой или нет ?

        if (selected4AddPlan.getPerHour()) {
            System.out.println("ПОЧАСОВОЙ");

            RequestContext.getCurrentInstance().execute("PF('dlg2').show()");


        } else {
            System.out.println("НЕ ПОЧАСОВОЙ");
            Integer i1 = this.selected4AddPlan.getCost();
            System.out.println("ПЕЧАТАЕМ ЦЕНУ - " + i1);
            this.newOrder.setCost(i1 * 10);
            //RequestContext context = RequestContext.getCurrentInstance();
        }

        //this.plans = this.mainDAO.getPlansById(i);
    }

    public Integer getHoursCount() {
        return hoursCount;
    }

    public void hoursCountChange() {
        this.hoursCount = this.hoursCount + 0;
        Integer i1 = this.hoursCount;
        Integer i2 = this.selected4AddPlan.getCost();
        this.newOrder.setCost((i1) * (i2));
        RequestContext.getCurrentInstance().execute("PF('dlg2').hide()");

    }

    public void setHoursCount(Integer hoursCount) {
        this.hoursCount = hoursCount;
    }


    public String getTestT() {
        return testT;
    }

    public void setTestT(String testT) {
        this.testT = testT;
    }


    public Plan getSelected4AddPlan() {
        return selected4AddPlan;
    }

    public void setSelected4AddPlan(Plan selected4AddPlan) {
        this.selected4AddPlan = selected4AddPlan;
    }

    public ArrayList<ClientSource> getClientsSources() {
        return mainDAO.getSources(String.valueOf(this.getSession().getId()));

    }
}


