package models;

public class Category {
  // category_id, Auto-incremented, primary key, not null
  private int categoryId;

  // category_name, not null
  private String categoryName;

  public Category() {
  }

  public Category(int categoryId, String categoryName) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}

