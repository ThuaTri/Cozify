package daos;

import models.Clothes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClothesDao extends GenericDao<Clothes> {
  public ClothesDao() {
    super();
  }

  @Override
  public List<Clothes> getAll() {
    list = null;
    String sql = "select * from clothes";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getClothesFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<Clothes> getRandom4() {
    list = null;
    String sql = "select top 4 * from clothes where stock_quantity > 0 and is_hidden = 0 and (size = 'M' or size = '8' or size is null) order by rand()";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getClothesFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<Clothes> getAllAvailable() {
    list = null;
    String sql = "select * from clothes where stock_quantity > 0 and is_hidden = 0";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getClothesFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  /**
   * Retrieves all distinct clothes from the database.
   *
   * @return A list of distinct clothes with default size M (if it has a size)
   */
  public List<Clothes> getAllDistinct() {
    list = null;
    String sql = "select * from clothes where size = 'M' or size = '8' or size is null";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getClothesFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<String> getAvailableSizes(Clothes clothes) {
    List<String> result = null;
    String sql = "select size from clothes where clothes_name = ? and stock_quantity > 0 " +
        "order by case size " +
        "when 'XS' then 1 " +
        "when 'S' then 2 " +
        "when 'M' then 3 " +
        "when 'L' then 4 " +
        "when 'XL' then 5 " +
        "when 'XXL' then 6 " +
        "else 7 end";
    try {
      result = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, clothes.getClothesName());
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        result.add(resultSet.getString("size"));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public Clothes getById(int id) {
    String sql = "select * from clothes where clothes_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getClothesFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  private Clothes getClothesFromResultSetItem(ResultSet resultSet) throws SQLException {
    Clothes clothes = new Clothes();
    clothes.setClothesId(resultSet.getInt("clothes_id"));
    clothes.setClothesName(resultSet.getString("clothes_name"));
    clothes.setPrice(resultSet.getBigDecimal("price"));
    clothes.setDiscount(resultSet.getInt("discount"));
    clothes.setRating(resultSet.getInt("rating"));
    clothes.setStockQuantity(resultSet.getInt("stock_quantity"));
    clothes.setSize(resultSet.getString("size"));
    clothes.setIsHidden(resultSet.getBoolean("is_hidden"));
    clothes.setCategoryId(resultSet.getInt("category_id"));
    return clothes;
  }

  public Clothes getByName(String name) {
    String sql = "select * from clothes where clothes_name = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, name);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getClothesFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  public Clothes getOtherClothesBySize(Clothes clothes, String size) {
    String sql = "select * from clothes where clothes_name = ? and size = ?";
    try {
      String name = clothes.getClothesName();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, size);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getClothesFromResultSetItem(resultSet);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  @Override
  public int add(Clothes clothes) {
    int result = 0;
    String sql = "insert into clothes (clothes_name, price, discount, rating, stock_quantity, size, is_hidden, category_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
    try {
      prepareStatementFromClothes(clothes, sql);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  @Override
  public int update(Clothes clothes) {
    int result = 0;
    String sql = "update clothes set clothes_name = ?, price = ?, discount = ?, rating = ?, stock_quantity = ?, size = ?, is_hidden = ?, category_id = ? where clothes_id = ?";
    try {
      prepareStatementFromClothes(clothes, sql);
      preparedStatement.setInt(9, clothes.getClothesId());
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  private void prepareStatementFromClothes(Clothes clothes, String sql) throws SQLException {
    preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, clothes.getClothesName());
    preparedStatement.setBigDecimal(2, clothes.getPrice());
    preparedStatement.setInt(3, clothes.getDiscount());
    preparedStatement.setInt(4, clothes.getRating());
    preparedStatement.setInt(5, clothes.getStockQuantity());
    preparedStatement.setString(6, clothes.getSize());
    preparedStatement.setBoolean(7, clothes.getIsHidden());
    preparedStatement.setInt(8, clothes.getCategoryId());
  }

  @Override
  public int delete(int id) {
    int result = 0;
    String sql = "delete from clothes where clothes_id = ?";
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return result;
  }

  public List<Clothes> getTop5ByName(String username) {
    list = null;
    String sql = "select top 5 * from clothes where clothes_name like ?";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getClothesFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }

  public List<Clothes> getAllDistinctByCategory(int categoryId) {
    list = null;
    String sql = "select * from clothes where category_id = ? and stock_quantity > 0 and is_hidden = 0 and (size = 'M' or size = '8' or size is null)";
    try {
      list = new ArrayList<>();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, categoryId);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        list.add(getClothesFromResultSetItem(resultSet));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return list;
  }
}
