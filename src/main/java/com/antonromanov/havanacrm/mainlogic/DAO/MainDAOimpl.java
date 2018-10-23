package com.antonromanov.havanacrm.mainlogic.DAO;

import java.sql.*;
import java.util.ArrayList;

import com.antonromanov.havanacrm.model.*;
import com.antonromanov.havanacrm.mainlogic.utils.DataConnect;

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
                System.out.println("хуй - " + rs.getString(1));
            }

//if (rs.getString(1).substring(0, 1);
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
                System.out.println("user id - " + rs.getString(1));
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
        System.out.println("User id приняли - " + userId);

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            //pstmt.setString(1, userId);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs == null) {
                System.out.println("rs - пустой ");
            }

            while (rs.next()) {
                result.add(new MenuItems(rs));
                System.out.println("хуй - " + rs.getString(1));
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
        String callSQL = "SELECT t1.id, t1.client, t1.order_short_name, t1.order_info, t1.order_type, t1.order_cost, t1.order_create_date, t1.order_deadline, t1.order_status FROM gao() t1";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            //  CallableStatement pstmt = con.prepareCall(callSQL);
            //pstmt.setInt(1, Integer.parseInt(userId));
            // pstmt.setInt(1, 2);
            ResultSet rs = pstmt.executeQuery();
            //  System.out.println("[First result set]");
            while (rs.next()) {
                //  System.out.println("ХУЙ, ЕЩЕ КАКОЙ ХУЙ - " + rs.getInt(1));
                //System.out.println("getALLOrders " + "[" + rs.getRow() + "]"  + rs.getString(1));
                System.out.println("[" + rs.getRow() + "]  -  " + "id {" + rs.getInt(1) + "} - " + "name {" + rs.getString(3) + "} - " + "client {" + rs.getString(2) + "}");
                result.add(new Order(rs));
            }

            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //      result.add(new Order("hjfbvfkjv"));


        return result;
    }

    public ArrayList<Order> getOrders4(String userId) {

        ArrayList<Order> result = new ArrayList<>();
        String callSQL = "SELECT public.gao2()";
        //String callSQL = "select t1.id, t1.client from gao() t1";
        try {
            con = DataConnect.getConnection();
            //  PreparedStatement pstmt = con.prepareStatement(callSQL);
            CallableStatement pstmt = con.prepareCall(callSQL);
            //pstmt.setInt(1, Integer.parseInt(userId));
            // pstmt.setInt(1, 2);
            ResultSet rs = pstmt.executeQuery();
            //  System.out.println("[First result set]");
            while (rs.next()) {
                //  System.out.println("ХУЙ, ЕЩЕ КАКОЙ ХУЙ - " + rs.getInt(1));
                System.out.println("ХУЙ " + "[" + rs.getRow() + "]" + rs.getString(1));
                result.add(new Order(rs));
            }

            DataConnect.close(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //      result.add(new Order("hjfbvfkjv"));


        return result;
    }

    //загружаем заказы для юзера
    public ArrayList<Order> getOrders3(String userId) {

        ArrayList<Order> result = new ArrayList<>();
        String callSQL = "SELECT * FROM orders WHERE orders.user_id = ?";


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
    public void addOrder(Order order) {


        String callSQL = "INSERT INTO orders (client, order_short_name, order_info, order_type, order_cost, order_create_date, order_deadline, order_status, user_id)\n" +
                "        VALUES (?, ?, ?, 1, ?, ?, ?, ? ,2 )";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);

            pstmt.setInt(1, order.getClientId());
            pstmt.setInt(1, order.getClientTest().getId());
            pstmt.setString(2, order.getShortName());
            pstmt.setString(3, order.getInfo());
            pstmt.setInt(4, order.getCost());
            pstmt.setDate(5, this.convertJavaDateToSqlDate(order.getCreatedDate()));

            if (order.getDeadline() != null) {
                pstmt.setDate(6, this.convertJavaDateToSqlDate(order.getDeadline()));
            } else {
                pstmt.setDate(6, null);
            }


            //pstmt.setInt(7, order.getOrderStatusId());
            pstmt.setInt(7, order.getOrderStatus().getId());
           // System.out.println("Мы устанавливаем id клиента - " + order.getClientId());
           // System.out.println("Мы устанавливаем id статуса - " + order.getOrderStatusId());
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


        String callSQL = "UPDATE orders SET  client = ?, order_short_name = ?, order_info = ?, order_cost = ?, order_create_date = ?, order_deadline = '2017-03-18', \n" +
                "order_status = ? WHERE orders.id = ?";

        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setInt(1, order.getClientId());
            System.out.println("Мы устанавливаем id клиента - " + order.getClientId());
            pstmt.setString(2, order.getShortName());
            pstmt.setString(3, order.getInfo());
            pstmt.setInt(4, order.getCost());
            pstmt.setDate(5, this.dateToSqlDate(order.getCreatedDate()));
            pstmt.setInt(6, order.getOrderStatusId());
            pstmt.setInt(7, order.getId());

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
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
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
    public void addClient(Client newClient) {
        String callSQL = "INSERT INTO clients (name, phone, email, date_created, birthday, user_id)\n" +
                "        VALUES (?, ?, ?, ?,?, 2)";
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

            pstmt.executeUpdate();
            DataConnect.close(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void editClient(Client selectedClient) {
        String callSQL = "UPDATE clients SET  name = ?, phone = ?, email = ?,  date_created = '2017-03-18', birthday = ? WHERE id = ?";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            System.out.println("Меняем имя клиента на -------" + selectedClient.getClientName());
            pstmt.setString(1, selectedClient.getClientName());
            pstmt.setString(2, selectedClient.getPhone());
            pstmt.setString(3, selectedClient.getEmail());
            pstmt.setDate(4, this.dateToSqlDate(selectedClient.getBirthDay()));
            pstmt.setInt(5, selectedClient.getId());

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
    public void addOrderType(OrderType newOrderType) {

        String callSQL = "INSERT INTO ordertypes (type, user_id)\n" +
                "        VALUES (?, 2)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newOrderType.getType());
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
    public void addStatus(Status newStatus) {

        String callSQL = "INSERT INTO order_status (status, user_id)\n" +
                "        VALUES (?, 2)";
        try {
            con = DataConnect.getConnection();
            PreparedStatement pstmt = con.prepareStatement(callSQL);
            pstmt.setString(1, newStatus.getStatus());
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
}
