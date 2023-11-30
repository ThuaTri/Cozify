package daos;

import models.OrderItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDao extends GenericDao<OrderItem> {
  public OrderItemDao() {
    super();
  }
  
  @Override
  public List<OrderItem> getAll() {
    list = null;
    String sql = "select * from order_item";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getOrderItemFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  @Override
  public OrderItem getById(int id) {
    String sql = "select * from order_item where order_item_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getOrderItemFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  private OrderItem getOrderItemFromResultSetItem(ResultSet resultSet) throws SQLException {
    OrderItem orderItem = new OrderItem();
    orderItem.setOrderId(resultSet.getInt("order_id"));
    orderItem.setClothesId(resultSet.getInt("clothes_id"));
    orderItem.setQuantity(resultSet.getInt("quantity"));
    orderItem.setSubtotal(resultSet.getBigDecimal("subtotal"));
    return orderItem;
  }

  @Override
  public int add(OrderItem orderItem) {
    int result = 0;
    String sql = "insert into order_item (order_id, clothes_id, quantity, subtotal) values (?, ?, ?, ?)";
    try {
      prepareStatementFromOrderItem(orderItem, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(OrderItem orderItem) {
    int result = 0;
    String sql = "update order_item set order_id = ?, clothes_id = ?, quantity = ?, subtotal = ? where order_item_id = ?";
    try {
      prepareStatementFromOrderItem(orderItem, sql);
      preparedStatement.setInt(5, orderItem.getOrderItemId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromOrderItem(OrderItem orderItem, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setInt(1, orderItem.getOrderId());
    preparedStatement.setInt(2, orderItem.getClothesId());
    preparedStatement.setInt(3, orderItem.getQuantity());
    preparedStatement.setBigDecimal(4, orderItem.getSubtotal());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from order_item where order_Item_id = ?";
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
