package daos;

import models.Voucher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoucherDao extends GenericDao<Voucher> {

  @Override
  public int add(Voucher voucher) {
    int result = 0;
    String sql = "insert into voucher (voucher_name, voucher_code, voucher_percent, voucher_quantity, start_date, end_date, is_hidden) values (?, ?, ?, ?, ?, ?, ?)";
    try {
      prepareStatementFromVoucher(voucher, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(Voucher voucher) {
    int result = 0;
    String sql = "update voucher set voucher_name = ?, voucher_code = ?, voucher_percent = ?, voucher_quantity = ?, start_date = ?, end_date = ?, is_hidden = ? where voucher_id = ?";
    try {
      prepareStatementFromVoucher(voucher, sql);
      preparedStatement.setInt(8, voucher.getVoucherId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromVoucher(Voucher voucher, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, voucher.getVoucherName());
    preparedStatement.setString(2, voucher.getVoucherCode());
    preparedStatement.setInt(3, voucher.getVoucherPercent());
    preparedStatement.setInt(4, voucher.getVoucherQuantity());
    preparedStatement.setTimestamp(5, voucher.getStartDate());
    preparedStatement.setTimestamp(6, voucher.getEndDate());
    preparedStatement.setBoolean(7, voucher.getIsHidden());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from voucher where voucher_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public Voucher getById(int id) {
    String sql = "select * from voucher where voucher_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getVoucherFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  public Voucher getByName(String name) {
    String sql = "select * from voucher where voucher_name = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, name);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getVoucherFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  public Voucher getByCode(String code) {
    String sql = "select * from voucher where voucher_code = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, code);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getVoucherFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  @Override
  public List<Voucher> getAll() {
    list = null;
    String sql = "select * from voucher";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getVoucherFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<Voucher> getAllAvailable() {
    list = null;
    String sql = "select * from voucher where is_hidden = 0";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getVoucherFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  private Voucher getVoucherFromResultSetItem(ResultSet resultSet) throws SQLException {
    Voucher voucher = new Voucher();
    voucher.setVoucherId(resultSet.getInt("voucher_id"));
    voucher.setVoucherName(resultSet.getString("voucher_name"));
    voucher.setVoucherCode(resultSet.getString("voucher_code"));
    voucher.setVoucherPercent(resultSet.getByte("voucher_percent"));
    voucher.setVoucherQuantity(resultSet.getInt("voucher_quantity"));
    voucher.setStartDate(resultSet.getTimestamp("start_date"));
    voucher.setEndDate(resultSet.getTimestamp("end_date"));
    voucher.setIsHidden(resultSet.getBoolean("is_hidden"));
    return voucher;
  }
}
