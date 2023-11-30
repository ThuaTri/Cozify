package daos;

import models.Clothes;
import models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao extends GenericDao<Order> {
  public OrderDao() {
    super();
  }
  
  @Override
  public List<Order> getAll() {
    list = null;
    String sql = "select * from [order]";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getOrderFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  @Override
  public Order getById(int id) {
    String sql = "select * from [order] where order_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getOrderFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  public Order getLatestOrder() {
    String sql = "select top 1 * from [order] order by order_id desc";
    try {
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getOrderFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  private Order getOrderFromResultSetItem(ResultSet resultSet) throws SQLException {
    Order order = new Order();
    order.setOrderId(resultSet.getInt("order_id"));
    order.setOrderTime(resultSet.getTimestamp("order_time"));
    order.setStatus(resultSet.getString("status"));
    order.setPaymentMethod(resultSet.getString("payment_method"));
    order.setFirstName(resultSet.getString("first_name"));
    order.setLastName(resultSet.getString("last_name"));
    order.setAddress(resultSet.getString("address"));
    order.setPhoneNumber(resultSet.getString("phone_number"));
    order.setEmail(resultSet.getString("email"));
    order.setTotal(resultSet.getBigDecimal("total"));
    order.setNote(resultSet.getString("note"));
    return order;
  }

  @Override
  public int add(Order order) {
    int result = 0;
    String sql = "insert into [order] (payment_method, first_name, last_name, [address], phone_number, email, total, note) values (?, ?, ?, ?, ?, ?, ?, ?)";
    try {
      prepareStatementFromOrder(order, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(Order order) {
    int result = 0;
    String sql = "update [order] set payment_method = ?, first_name = ?, last_name = ?, [address] = ?, phone_number = ?, email = ?, total = ?, note = ? where order_id = ?";
    try {
      prepareStatementFromOrder(order, sql);
      preparedStatement.setInt(9, order.getOrderId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromOrder(Order order, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, order.getPaymentMethod());
    preparedStatement.setString(2, order.getFirstName());
    preparedStatement.setString(3, order.getLastName());
    preparedStatement.setString(4, order.getAddress());
    preparedStatement.setString(5, order.getPhoneNumber());
    preparedStatement.setString(6, order.getEmail());
    preparedStatement.setBigDecimal(7, order.getTotal());
    preparedStatement.setString(8, order.getNote());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from order where order_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }
}
