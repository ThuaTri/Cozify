package daos;

import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends GenericDao<User> {
  public UserDao() {
    super();
  }

  @Override
  public List<User> getAll() {
    list = null;
    String sql = "select * from [user]";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getUserFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<User> getAllByRole(byte roleId) {
    list = null;
    String sql = "select * from [user] where role_id = ?";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setByte(1, roleId);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getUserFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  @Override
  public User getById(int id) {
    String sql = "select * from [user] where user_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getUserFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  private User getUserFromResultSetItem(ResultSet resultSet) throws SQLException {
    User user = new User();
    user.setUserId(resultSet.getInt("user_id"));
    user.setUsername(resultSet.getString("username"));
    user.setPassword(resultSet.getString("password"));
    user.setEmail(resultSet.getString("email"));
    user.setRoleId(resultSet.getByte("role_id"));
    user.setFirstName(resultSet.getString("first_name"));
    user.setLastName(resultSet.getString("last_name"));
    user.setPhoneNumber(resultSet.getString("phone_number"));
    user.setAddress(resultSet.getString("address"));
    return user;
  }

  @Override
  public int add(User user) {
    int result = 0;
    String sql = "insert into [user] (username, password, email, role_id, first_name, last_name) values (?, ?, ?, ?, ?, ?)";
    try {
      prepareStatementFromUser(user, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(User user) {
    int result = 0;
    String sql = "update [user] set username = ?, [password] = ?, email = ?, role_id = ?, first_name = ?, last_name = ?, phone_number = ?, address = ? where [user_id] = ?";
    try {
      prepareStatementFromUser(user, sql);
      preparedStatement.setString(7, user.getPhoneNumber());
      preparedStatement.setString(8, user.getAddress());
      preparedStatement.setInt(9, user.getUserId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromUser(User user, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, user.getUsername());
    preparedStatement.setString(2, user.getPassword());
    preparedStatement.setString(3, user.getEmail());
    preparedStatement.setByte(4, user.getRoleId());
    preparedStatement.setString(5, user.getFirstName());
    preparedStatement.setString(6, user.getLastName());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from [user] where user_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  public boolean login(String email, String password) throws SQLException {
    resultSet = null;
    String sql = "select * from [user] where email = ? and [password] = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, email);
      preparedStatement.setString(2, password);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return resultSet.next();
  }

  public boolean hasExistingUsername(String username) throws SQLException {
    resultSet = null;
    String sql = "select * from [user] where username = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return resultSet.next();
  }

  public boolean hasExistingEmail(String email) throws SQLException {
    resultSet = null;
    String sql = "select * from [user] where email = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, email);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return resultSet.next();
  }

  public User getUserByEmail(String email) {
    String sql = "select * from [user] where email = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, email);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getUserFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }
}
