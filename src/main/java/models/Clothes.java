package models;

import java.math.BigDecimal;

public class Clothes {
  // clothes_id, Auto-incremented, primary key, not null
  private int clothesId;

  // clothes_name, not null
  private String clothesName;

  // price, not null
  private BigDecimal price;

  // discount, not null
  private int discount;

  // rating, not null
  private int rating;

  // stock_quantity, not null
  private Integer stockQuantity;

  // size, not null
  private String size;

  // is_hidden, not null
  private Boolean isHidden;

  // category_id, not null
  private int categoryId;

  public Clothes() {
  }

  public Clothes(int clothesId, String clothesName, BigDecimal price, int discount, int rating, Integer stockQuantity, String size, Boolean isHidden, int categoryId) {
    this.clothesId = clothesId;
    this.clothesName = clothesName;
    this.price = price;
    this.discount = discount;
    this.rating = rating;
    this.stockQuantity = stockQuantity;
    this.size = size;
    this.isHidden = isHidden;
    this.categoryId = categoryId;
  }

  public int getClothesId() {
    return clothesId;
  }

  public void setClothesId(int clothesId) {
    this.clothesId = clothesId;
  }

  public String getClothesName() {
    return clothesName;
  }

  public void setClothesName(String clothesName) {
    this.clothesName = clothesName;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getDiscount() {
    return discount;
  }

  public void setDiscount(int discount) {
    this.discount = discount;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public String getSize() {
    return size == null ? "null" : size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Boolean getHidden() {
    return isHidden;
  }

  public void setHidden(Boolean hidden) {
    isHidden = hidden;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }
}
