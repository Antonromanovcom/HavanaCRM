package com.antonromanov.havanacrm.mainlogic.DAO;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.antonromanov.havanacrm.model.*;
import com.antonromanov.havanacrm.mainlogic.utils.DataConnect;
import com.antonromanov.havanacrm.usersession.Login;

import javax.ejb.Stateless;


@Stateless
public class MainDAOimpl implements MainDAO {

    Connection con = null;

    //Пользовательская аутентификация
    public boolean loginCheck(String login, String password) {

        String callSQL = "SELECT public.usercheck(?,?)";
        Boolean result = false;
        String queryResult = "";

        try {
            con = DataConnect.getConnection();


            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, login);
            pstmt.setString(2, password);


            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //queryResult = rs.getString(1);
                //rs.getString()
                result = rs.getBoolean(1);
                //       System.out.println("хуй - " + rs.getString(1));
            }

//if (rs.getString(1).substring(0, 1);
            DataConnect.close(con);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Проверка только логина (нужно для регистрации
    public boolean onlyLoginCheck(String login) {

        String callSQL = "SELECT public.onlylogincheck(?)";
        Boolean result = false;
        String queryResult = "";

        try {
            con = DataConnect.getConnection();


            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getBoolean(1);
            }
            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Проверка только email (нужно для регистрации
    public boolean onlyEmailCheck(String email) {

        String callSQL = "SELECT public.onlyemailcheck(?)";
        Boolean result = false;
        String queryResult = "";

        try {
            con = DataConnect.getConnection();


            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getBoolean(1);
            }
            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    //получаем UserId
    public Integer getUserId(String login, String password) {

        String callSQL = "SELECT public.userid(?,?)";
        Integer result = -1;

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //загружаем сайдбар (элементы меню)
    public ArrayList<MenuItems> getSidebarMenuItems(int userId) {

        ArrayList<MenuItems> result = new ArrayList<>();
        String callSQL = "SELECT * FROM sidebarmenuitems WHERE sidebarmenuitems.user = ?";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, 2);
            ResultSet rs = pstmt.executeQuery();

            if (rs == null) {
            }

            while (rs.next()) {
                result.add(new MenuItems(rs));
            }

            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //вторая версия получения списка ордеров через хранимую процедуру
    public ArrayList<Order> getOrders(String userId) {

        ArrayList<Order> result = new ArrayList<>();
        //String callSQL = "SELECT public.gao()";
        String callSQL = "SELECT t1.id, t1.client, t1.order_short_name, t1.order_info, t1.order_type, t1.sub_type, t1.plan, t1.order_cost, t1.order_create_date, t1.order_deadline, t1.order_status FROM gao(?) t1";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, Integer.parseInt(userId));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new Order(rs));
            }

            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }


    @Override
    public void addOrder(Order order, Integer userId) {


        String callSQL = "INSERT INTO orders (client, order_short_name, order_info, order_type, order_sub_type, order_plan, order_cost, order_create_date, order_deadline, order_status, user_id)\n" +
                "        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);

            pstmt.setInt(1, order.getClientId());
            //pstmt.setInt(1, order.getClientTest().getId());
            pstmt.setString(2, order.getShortName());
            pstmt.setString(3, order.getInfo());
            pstmt.setInt(4, order.getType());
            pstmt.setInt(5, order.getSubtype());
            System.out.println("Добавляем ОрдерПлан   " + order.getPlan());
            pstmt.setInt(6, order.getPlan());
            pstmt.setInt(7, order.getCost());
            pstmt.setDate(8, this.convertJavaDateToSqlDate(order.getCreatedDate()));

            if (order.getDeadline() != null) {
                pstmt.setDate(9, this.convertJavaDateToSqlDate(order.getDeadline()));
            } else {
                pstmt.setDate(9, null);
            }

            pstmt.setInt(10, order.getOrderStatus().getId());
            pstmt.setInt(11, userId);


            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteOrder(Integer orderId) {

        String callSQL = "DELETE FROM orders WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void editOrder(Order order) {


        String callSQL = "UPDATE orders SET  client = ?, order_short_name = ?, order_info = ?, order_type = ?, order_sub_type = ?, order_plan = ?, order_cost = ?, order_create_date = ?, order_deadline = ?, \n" +
                "order_status = ? WHERE orders.id = ?";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, order.getClientId());
            System.out.println("Мы устанавливаем id клиента - " + order.getClientId());
            pstmt.setString(2, order.getShortName());
            pstmt.setString(3, order.getInfo());
            pstmt.setInt(4, order.getType());
            pstmt.setInt(5, order.getSubtype());
            pstmt.setInt(6, order.getPlan());
            pstmt.setInt(7, order.getCost());
            pstmt.setDate(8, this.dateToSqlDate(order.getCreatedDate()));
            pstmt.setDate(9, this.dateToSqlDate(order.getDeadline()));
            pstmt.setInt(10, order.getOrderStatusId());
            pstmt.setInt(11, order.getId());

            System.out.println("А orderId у нас....  - " + order.getId());
            System.out.println("А order status у нас....  - " + order.getOrderStatusId());

            pstmt.executeUpdate();

            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Конвертим SQL Date в Java Date
    private java.sql.Date dateToSqlDate(java.util.Date date) {

        java.sql.Date sqlDate = null;
        sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;
    }

    // Конвертим  Java Date в SQL Date
    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    @Override
    public String Test() {
        return "111111111111";
    }

    @Override
    public ArrayList<Client> getClients(String s) {

        ArrayList<Client> result = new ArrayList<>();
        String callSQL = "SELECT * FROM clients WHERE clients.user_id = ?";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, Integer.parseInt(s));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new Client(rs));
                System.out.println("А КЛИЕНТ У НАС - " + rs.getString(1));
            }

            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void deleteClient(Integer clientId) {

        String callSQL = "DELETE FROM clients WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, clientId);
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void addClient(Client newClient, Integer userId) {
        String callSQL = "INSERT INTO clients (name, phone, email, date_created, birthday, user_id)\n" +
                "        VALUES (?, ?, ?, ?,?, ?)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newClient.getClientName());
            pstmt.setString(2, newClient.getPhone());
            pstmt.setString(3, newClient.getEmail());
            pstmt.setDate(4, this.convertJavaDateToSqlDate(new java.util.Date()));

            if (newClient.getBirthDay() != null) {
                pstmt.setDate(5, this.convertJavaDateToSqlDate(newClient.getBirthDay()));
            } else {
                pstmt.setDate(5, null);
            }
            pstmt.setInt(6, userId);

            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void editClient(Client selectedClient) {

        String callSQL = "UPDATE clients SET  name = ?, phone = ?, email = ?,  birthday = ?, whereclientfrom = ? WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            System.out.println("Меняем имя клиента на -------" + selectedClient.getClientName());
            pstmt.setString(1, selectedClient.getClientName());
            pstmt.setString(2, selectedClient.getPhone());
            pstmt.setString(3, selectedClient.getEmail());
            if (selectedClient.getBirthDay() == null) {
                pstmt.setDate(4, null);
            } else {

                pstmt.setDate(4, this.dateToSqlDate(selectedClient.getBirthDay()));
            }

            pstmt.setLong(5, selectedClient.getWhereClientFrom());
            pstmt.setInt(6, selectedClient.getId());

            pstmt.executeUpdate();

            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<OrderType> getOrderTypeStatictic(int id) {
        ArrayList<OrderType> result = new ArrayList<>();
       /* String callSQL = "SELECT ordertypes.id, ordertypes.type, COUNT(ordertypes.type) AS test FROM ordertypes\n" +
                "LEFT JOIN orders ON ordertypes.id = orders.order_type   WHERE orders.user_id = ?\n" +
                "GROUP BY ordertypes.id, ordertypes.type\n";
                */

        String callSQL = "SELECT ordertypes.id, ordertypes.type  FROM ordertypes\n" +
                " WHERE ordertypes.user_id = ?\n" +
                "\n";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("id user'a ==== " + id);
                System.out.println("ПЕЧАТАЕМ СТАТИСТИКУ ТИПОВ ==== " + rs.getString("type"));
                result.add(new OrderType(rs));
            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void addOrderType(OrderType newOrderType, Integer userId) {

        String callSQL = "INSERT INTO ordertypes (type, user_id)\n" +
                "        VALUES (?, ?)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newOrderType.getType());
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            System.out.println("Добавляем тип заказа" + newOrderType.getType());
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void editOrderType(OrderType selectedOrderType) {
        String callSQL = "UPDATE ordertypes SET  type = ? WHERE ordertypes.id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, selectedOrderType.getType());
            pstmt.setInt(2, selectedOrderType.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteOrderType(Integer id) {

        String callSQL = "DELETE FROM ordertypes WHERE id = ?";
        System.out.println("Удаляем тип заказа с ID - " + id);

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Status> getStatuses(int userId) {

        ArrayList<Status> result = new ArrayList<>();
        String callSQL = "SELECT * FROM order_status WHERE order_status.user_id = ?";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new Status(rs));
            }
            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteStatus(Integer id) {

        String callSQL = "DELETE FROM order_status WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void editStatus(Status selectedStatus) {
        String callSQL = "UPDATE order_status SET  status = ? WHERE order_status.id=?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, selectedStatus.getStatus());
            pstmt.setInt(2, selectedStatus.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void addStatus(Status newStatus, Integer userId) {

        String callSQL = "INSERT INTO order_status (status, user_id)\n" +
                "        VALUES (?, ?)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newStatus.getStatus());
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //Ищем клиента по id в списке уже ранее загруженных из БД
    @Override
    public Client findClientById(int i, ArrayList<Client> clients) {
        int j = 0;
        int result = 0;
        while (clients.size() > j) {
            if (clients.get(j).getId() == i) {
                result = j;
            }
            j++;
        }

        return clients.get(result);
    }

    //Ищем статус по id в списке уже ранее загруженных из БД
    @Override
    public Status findStatusById(int i, ArrayList<Status> statuses) {
        int j = 0;
        int result = 0;
        while (statuses.size() > j) {
            if (statuses.get(j).getId() == i) {
                result = j;
            }
            j++;
        }

        return statuses.get(result);
    }

    //Ищем источник клиента по id в списке уже ранее загруженных из БД
    @Override
    public ClientSource findSourceById(Long i, ArrayList<ClientSource> sources) {
        int j = 0;
        int result = 0;
        while (sources.size() > j) {
            if (sources.get(j).getId() == i) {
                result = j;
            }
            j++;
        }

        return sources.get(result);
    }

    @Override
    public Map getOrderTypeMap(int id) {
        Map<String, String> result = new HashMap<String, String>();

        String callSQL = "SELECT ordertypes.id, ordertypes.type  FROM ordertypes\n" +
                " WHERE ordertypes.user_id = ?\n" +
                "\n";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Забираем ТИПЫ ЗАКАЗОВ в HASHMAP id user'a ==== " + id);
                System.out.println("ПЕЧАТАЕМ СТАТИСТИКУ ТИПОВ ==== " + rs.getString("type"));
                result.put(rs.getString("type"), String.valueOf(rs.getInt("id")));
            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<OrderSubType> getOrderSubType(int id) {
        ArrayList<OrderSubType> result = new ArrayList<>();
        String callSQL = "SELECT subtype.id, subtype.parent_type_id, subtype.subtype_name  FROM subtype\n" +
                " WHERE subtype.parent_type_id = ?\n" +
                "\n";
        System.out.println("Мы в getOrderSubType - " + id);
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Добавляем подтип заказа -> " + rs.getString("subtype_name"));
                result.add(new OrderSubType(rs));
            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<OrderSubType> getAllOrderSubType() {
        ArrayList<OrderSubType> result = new ArrayList<>();
        String callSQL = "SELECT subtype.id, subtype.parent_type_id, subtype.subtype_name  FROM subtype";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new OrderSubType(rs));

            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Plan> getPlansById(Integer id) {
        ArrayList<Plan> result = new ArrayList<>();

        String callSQL = "SELECT plans.id, plans.plan_name, plans.is_per_hour, plans.parent_subtype_id,  plans.cost,  plans.description  FROM plans\n" +
                " WHERE plans.parent_subtype_id = ?\n" +
                "\n";

        System.out.println("мы в getPlansById. Мы пришли с ID = " + id);
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Добавляем план -> " + rs.getString("plan_name"));
                result.add(new Plan(rs, 1));
            }
            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Plan> getAllPlansAndOptions(Integer id) {

        ArrayList<Plan> result = new ArrayList<>();
        String callSQL = "SELECT pl.id, pl.plan, pl.cost, pl.parent_subtype_id, pl.json FROM get_plans_2(?) pl";
        System.out.println(" мы вошли сюда с id....." + id);
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                //  System.out.println("[" + rs.getRow() + "]  -  " + "id {" + rs.getInt(1) + "} - " + "name {" + rs.getString(3) + "} - " + "client {" + rs.getString(2) + "}");
                result.add(new Plan(rs));
            }

            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;

    }

    @Override
    public void editOrderSubType(OrderSubType selectedSubType) {

        String callSQL = "UPDATE subtype SET  subtype_name = ? WHERE subtype.id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, selectedSubType.getSubtype());
            pstmt.setInt(2, selectedSubType.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void deleteOrderSubType(Integer id) {

        String callSQL = "DELETE FROM subtype WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void addOrderSubType(OrderSubType newOrdeSubType, Integer id) {

        String callSQL = "INSERT INTO subtype (subtype_name, parent_type_id)\n" +
                "        VALUES (?, ?)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newOrdeSubType.getSubtype());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Добавляем тип заказа" + newOrdeSubType.getSubtype());
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void editSelectedOption(Option selectedOption) {

        String callSQL = "UPDATE options SET  optionname = ?, plan_id = ? WHERE options.id = ?";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, selectedOption.getOptionName());
            pstmt.setInt(2, selectedOption.getPlanId());
            pstmt.setInt(3, selectedOption.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteSelectedOption(Option selectedOption) {


        String callSQL = "DELETE FROM options WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, selectedOption.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deletePlan(Plan plan) {

// сначала удаляем все опции внутри плана. Проверки сделаем потом
        String callSQL = "DELETE FROM options WHERE plan_id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, plan.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Теперь киляем сам план
        callSQL = "DELETE FROM plans WHERE id = ?";

        System.out.println("Мы удаляем план с id - " + plan.getId());
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, plan.getId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void editPlan(Plan selectedPlan) {

        String callSQL = "UPDATE plans SET  plan_name = ?, is_per_hour = ?,  parent_subtype_id = ?, cost = ?, description = ?  WHERE plans.id = ?";
        if (!selectedPlan.getPerHour()) {


            try {
                con = DataConnect.getConnection();
                PreparedStatement pstmt = con.prepareStatement(callSQL);
                pstmt.setString(1, selectedPlan.getPlanName());
                pstmt.setBoolean(2, false);
                pstmt.setInt(3, selectedPlan.getSubTypeId());
                System.out.println("А стоимость у нас - " + selectedPlan.getCost());
                pstmt.setInt(4, selectedPlan.getCost());
                pstmt.setString(5, "Какое-то описание");
                pstmt.setInt(6, selectedPlan.getId());
                pstmt.executeUpdate();
                DataConnect.close(con);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            if (selectedPlan.isEdited()) {

                for (Option option : selectedPlan.getOptions()) {
                    System.out.println("Добавляем опцию - " + option.getOptionName());
                    if (option.getTemporary()) {
                        this.addOption(option);
                        option.setTemporary(false);
                    }
                }
            } else {
                System.out.println("Новых опций нет ");
            }

        } else {


            //String callSQL = "UPDATE plans SET  plan_name = ?, is_per_hour = ?,  parent_subtype_id = ?, cost = ?, description = ?  WHERE plans.id = ?";

            try {
                con = DataConnect.getConnection();
                PreparedStatement pstmt = con.prepareStatement(callSQL);
                System.out.println("Это почасовой план. Записываем его как - " + selectedPlan.getPlanName());
                pstmt.setString(1, selectedPlan.getPlanName());
                pstmt.setBoolean(2, true);
                pstmt.setInt(3, selectedPlan.getSubTypeId());
                pstmt.setInt(4, selectedPlan.getCost());
                pstmt.setString(5, "Какое-то описание");
                pstmt.setInt(6, selectedPlan.getId());
                pstmt.executeUpdate();
                DataConnect.close(con);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            for (Option option : selectedPlan.getOptions()) {
                System.out.println("Удаляем опцию из плана, так как он почасовой - " + option.getOptionName());
                if (!option.getTemporary()) {
                    this.deleteSelectedOption(option);
                }
            }
// - А теперь добавляем опцию "Почасовая оплата"

            Option temporaryOption = new Option("Почасовая оплата", selectedPlan.getId());
            this.addOption(temporaryOption);
        }
    }

    @Override
    public void addPlan(Plan newPlan) {


        String callSQL = "INSERT INTO plans (plan_name, is_per_hour, parent_subtype_id, cost) VALUES (?, ?, ?, ?)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newPlan.getPlanName());
            pstmt.setBoolean(2, newPlan.getPerHour());
            pstmt.setInt(3, newPlan.getSubTypeId());
            pstmt.setInt(4, newPlan.getCost());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public String getClientSourceTextByClientId(Integer id) {

        String callSQL = "SELECT whereclientfrom.source FROM whereclientfrom RIGHT JOIN clients ON whereclientfrom.id = clients.whereclientfrom WHERE clients.id=?";
        String result = "";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result = rs.getString("source");
            }

            DataConnect.close(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    public void createNewUser(Login newUser) {


        String callSQL = "INSERT INTO users (login, pwd, email) VALUES (?, ?, ?)";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);

            pstmt.setString(1, newUser.getLogin());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getEmail());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<ClientSource> getSources(String s) {
        ArrayList<ClientSource> result = new ArrayList<>();

        String callSQL = "SELECT * FROM whereclientfrom WHERE userid=?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, Integer.parseInt(s));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new ClientSource(rs));
            }

            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }


    public void addOption(Option option) {
        String callSQL = "INSERT INTO options (optionname, plan_id) VALUES (?, ?)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, option.getOptionName());
            pstmt.setInt(2, option.getPlanId());
            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
