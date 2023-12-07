package daos;

import models.PaymentMethod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDao extends GenericDao<PaymentMethod> {
  @Override
  public int add(PaymentMethod paymentMethod) {
    int result = 0;
    String sql = "insert into payment_method (payment_method) values (?)";
    try {
      prepareStatementFromPaymentMethod(paymentMethod, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(PaymentMethod paymentMethod) {
    int result = 0;
    String sql = "update payment_method set payment_method = ? where payment_method_id = ?";
    try {
      prepareStatementFromPaymentMethod(paymentMethod, sql);
      preparedStatement.setByte(2, paymentMethod.getPaymentMethodId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromPaymentMethod(PaymentMethod paymentMethod, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, paymentMethod.getPaymentMethod());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from payment_method where payment_method_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setByte(1, (byte) id);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public PaymentMethod getById(int id) {
    String sql = "select * from payment_method where payment_method_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setByte(1, (byte) id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getPaymentMethodFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  @Override
  public List<PaymentMethod> getAll() {
    list = null;
    String sql = "select * from payment_method";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getPaymentMethodFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  private PaymentMethod getPaymentMethodFromResultSetItem(ResultSet resultSet) throws SQLException {
    PaymentMethod paymentMethod = new PaymentMethod();
    paymentMethod.setPaymentMethodId(resultSet.getByte("payment_method_id"));
    paymentMethod.setPaymentMethod(resultSet.getString("payment_method"));
    return paymentMethod;
  }
}
