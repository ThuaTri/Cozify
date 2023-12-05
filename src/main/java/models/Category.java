package models;

public class Category {
  // category_id, Auto-incremented, primary key, not null
  private int categoryId;

  // category_name, not null
  private String categoryName;

  // is_hidden, not null
  private boolean isHidden;

  private int itemCount;
  private int orderCount;
  private double totalProfit;

  public Category() {
  }

  public Category(String categoryName, boolean isHidden) {
    this.categoryName = categoryName;
    this.isHidden = isHidden;
  }

  public Category(int categoryId, String categoryName) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.isHidden = false;
  }

  public Category(int categoryId, String categoryName, boolean isHidden) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.isHidden = isHidden;
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

  public int getItemCount() {
    return itemCount;
  }

  public void setItemCount(int itemCount) {
    this.itemCount = itemCount;
  }

  public int getOrderCount() {
    return orderCount;
  }

  public void setOrderCount(int orderCount) {
    this.orderCount = orderCount;
  }

  public double getTotalProfit() {
    return totalProfit;
  }

  public void setTotalProfit(double totalProfit) {
    this.totalProfit = totalProfit;
  }

  public boolean getIsHidden() {
    return isHidden;
  }

  public void setIsHidden(boolean hidden) {
    isHidden = hidden;
  }
}

