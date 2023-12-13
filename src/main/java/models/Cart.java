package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
  private List<OrderItem> orderItems;
  private BigDecimal total;

  public Cart() {
    this.orderItems = new ArrayList<>();
    this.total = new BigDecimal(0);
  }

  public Cart(List<OrderItem> orderItems, BigDecimal total) {
    this.orderItems = orderItems;
    this.total = total;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public int getNumberOfItems() {
    return orderItems.size();
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
