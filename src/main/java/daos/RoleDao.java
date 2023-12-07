package daos;

import models.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDao extends GenericDao<Role>{
  @Override
  public int add(Role role) {
    int result = 0;
    String sql = "insert into role (role) values (?)";
    try {
      prepareStatementFromRole(role, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(Role role) {
    int result = 0;
    String sql = "update payment_method set payment_method = ? where payment_method_id = ?";
    try {
      prepareStatementFromRole(role, sql);
      preparedStatement.setByte(2, role.getRoleId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromRole(Role role, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, role.getRole());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from role where role_id = ?";
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
  public Role getById(int id) {
    String sql = "select * from role where role_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setByte(1, (byte) id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getRoleFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  @Override
  public List<Role> getAll() {
    list = null;
    String sql = "select * from role";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getRoleFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  private Role getRoleFromResultSetItem(ResultSet resultSet) throws SQLException {
    Role role = new Role();
    role.setRoleId(resultSet.getByte("role_id"));
    role.setRole(resultSet.getString("role"));
    return role;
  }
}
