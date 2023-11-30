package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GenericDao<T> {

  protected static final Logger LOGGER = Logger.getLogger(GenericDao.class.getName());
  protected Connection connection;
  protected PreparedStatement preparedStatement;
  protected ResultSet resultSet;
  protected List<T> list;

  public GenericDao() {
    connection = dbConnection.DbConnection.getConnection();
  }

  protected void handleSQLException(SQLException e) {
    e.printStackTrace(); // Log or handle the exception as needed
    throw new RuntimeException("Error executing SQL statement", e);
  }

  protected void handleSQLException(SQLException e, String msg) {
    e.printStackTrace(); // Log or handle the exception as needed
    LOGGER.log(Level.SEVERE, msg, e);
  }

  public abstract int add(T entity);
  public abstract int update(T entity);
  public abstract int delete(int id);

  public abstract T getById(int id);

  public abstract List<T> getAll();
}
