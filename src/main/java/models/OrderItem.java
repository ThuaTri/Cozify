package models;

import daos.ClothesDao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderItem implements Serializable {
  // order_item_id, Auto-incremented, primary key, not null
  private int orderItemId;

  // order_id, foreign key, not null
  private int orderId;

  // clothes_id, foreign key, not null
  private int clothesId;

  // Additional property for ease of access
  private Clothes clothes;

  // quantity, not null
  private Integer quantity;

  // subtotal
  private BigDecimal subtotal;

  private List<String> availableSizes;

  public OrderItem() {
  }

  public OrderItem(int clothesId, Integer quantity, BigDecimal subtotal) {
    this.clothesId = clothesId;
    this.quantity = quantity;
    this.subtotal = subtotal;
    ClothesDao clothesDao = new ClothesDao();
    this.clothes = clothesDao.getById(clothesId);
    this.availableSizes = clothesDao.getAvailableSizes(clothes);
  }

  public int getOrderItemId() {
    return orderItemId;
  }

  public void setOrderItemId(int orderItemId) {
    this.orderItemId = orderItemId;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getClothesId() {
    return clothesId;
  }

  public void setClothesId(int clothes) {
    this.clothesId = clothes;
  }

  public Clothes getClothes() {
    return clothes;
  }

  public void setClothes(Clothes clothes) {
    this.clothes = clothes;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public List<String> getAvailableSizes() {
    return availableSizes;
  }

  public void setAvailableSizes(List<String> availableSizes) {
    this.availableSizes = availableSizes;
  }
}
