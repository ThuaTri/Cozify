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
        category.setIsHidden(resultSet.getBoolean("is_hidden"));
        list.add(category);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<Category> getAllAvailable() {
    list = null;
    String sql = "select * from category where is_hidden = 0";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setIsHidden(resultSet.getBoolean("is_hidden"));
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
        category.setIsHidden(resultSet.getBoolean("is_hidden"));
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
        category.setIsHidden(resultSet.getBoolean("is_hidden"));
        return category;
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

 public List<Category> getReportAll() {
   list = null;
   String sql = "select " +
       "    c.category_id," +
       "    c.category_name," +
       "    c.is_hidden," +
       "    count(distinct cl.clothes_id) as item_count," +
       "    count(distinct o.order_id) as order_count," +
       "    sum(oi.quantity * (cl.price - (cl.price * cl.discount / 100))) as total_profit " +
       "from category c" +
       "         left join (" +
       "           select  distinct clothes_id, category_id, price, discount from " +
       "            clothes where size = 'M' or size = '8' or size is null" +
       "          ) cl on c.category_id = cl.category_id " +
       "         left join order_item oi on cl.clothes_id = oi.clothes_id " +
       "         left join [order] o on oi.order_id = o.order_id " +
       "group by c.category_id, c.category_name, c.is_hidden;";
   try {
     list = new ArrayList<>();
     preparedStatement = connection.prepareStatement(sql);
     resultSet = preparedStatement.executeQuery();
     while (resultSet.next()) {
       Category category = new Category();
       category.setCategoryId(resultSet.getInt("category_id"));
       category.setCategoryName(resultSet.getString("category_name"));
       category.setIsHidden(resultSet.getBoolean("is_hidden"));
       category.setItemCount(resultSet.getInt("item_count"));
       category.setOrderCount(resultSet.getInt("order_count"));
       category.setTotalProfit(resultSet.getDouble("total_profit"));
       list.add(category);
     }
   } catch (SQLException e) {
     handleSQLException(e);
   }
   return list;
 }

  @Override
  public int add(Category category) {
    int result = 0;
    String sql = "insert into category (category_name, is_hidden) values (?, ?)";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, category.getCategoryName());
      preparedStatement.setBoolean(2, category.getIsHidden());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(Category category) {
    int result = 0;
    String sql = "update category set category_name = ?, is_hidden = ? where category_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, category.getCategoryName());
      preparedStatement.setBoolean(2, category.getIsHidden());
      preparedStatement.setInt(3, category.getCategoryId());
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
