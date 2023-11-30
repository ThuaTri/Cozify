package daos;

import models.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends GenericDao<Category> {
  public CategoryDao() {
    super();
  }

  @Override
  public List<Category> getAll() {
    list = null;
    String sql = "select * from category";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        list.add(category);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  @Override
  public Category getById(int id) {
    String sql = "select * from category where category_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        return category;
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  public Category getByName(String name) {
    String sql = "select * from category where category_name = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, name);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        return category;
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  @Override
  public int add(Category category) {
    int result = 0;
    String sql = "insert into category (category_name) values (?)";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, category.getCategoryName());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(Category category) {
    int result = 0;
    String sql = "update category set category_name = ? where category_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, category.getCategoryName());
      preparedStatement.setInt(2, category.getCategoryId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from category where category_id = ?";
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
