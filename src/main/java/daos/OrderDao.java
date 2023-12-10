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

  public List<Order> getAllByUserId(int userId) {
    list = null;
    String sql = "select * from [order] where user_id = ?";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, userId);
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
    order.setUserId(resultSet.getInt("user_id"));
    order.setOrderTime(resultSet.getTimestamp("order_time"));
    order.setStatus(resultSet.getString("status"));
    order.setPaymentMethodId(resultSet.getByte("payment_method_id"));
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
    String sql = "insert into [order] (user_id, payment_method_id, first_name, last_name, [address], phone_number, email, total, note) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    String sql = "update [order] set user_id = ?, payment_method_id = ?, first_name = ?, last_name = ?, [address] = ?, phone_number = ?, email = ?, total = ?, note = ?, status = ? where order_id = ?";
    try {
      prepareStatementFromOrder(order, sql);
      preparedStatement.setString(10, order.getStatus());
      preparedStatement.setInt(11, order.getOrderId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromOrder(Order order, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setInt(1, order.getUserId());
    preparedStatement.setByte(2, order.getPaymentMethodId());
    preparedStatement.setString(3, order.getFirstName());
    preparedStatement.setString(4, order.getLastName());
    preparedStatement.setString(5, order.getAddress());
    preparedStatement.setString(6, order.getPhoneNumber());
    preparedStatement.setString(7, order.getEmail());
    preparedStatement.setBigDecimal(8, order.getTotal());
    preparedStatement.setString(9, order.getNote());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from [order] where order_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  public int cancel(int id) {
    int result = 0;
    String sql = "update [order] set status = 'cancelled' where order_id = ?";
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
