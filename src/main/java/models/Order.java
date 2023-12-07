package models;

import daos.OrderDao;
import daos.PaymentMethodDao;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
  // order_id, Auto-incremented, primary key, not null
  private int orderId;

  // user_id, not null
  private int userId;

  // order_time, not null
  private Timestamp orderTime;

  // status, not null
  private String status;

  // payment_method, not null
  private String paymentMethod;
  private byte paymentMethodId;

  // first_name, not null
  private String firstName;

  // last_name, not null
  private String lastName;

  // address, not null
  private String address;

  // phone_number, not null
  private String phoneNumber;

  // email, not null
  private String email;

  // total, not null
  private BigDecimal total;

  // note
  private String note;

  public Order() {
  }

  public Order(int userId, byte paymentMethodId, String firstName, String lastName, String address, String phoneNumber, String email, BigDecimal total, String note) {
    this.userId = userId;
    this.setPaymentMethodId(paymentMethodId);
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.total = total;
    this.note = note;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Timestamp getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(Timestamp orderTime) {
    this.orderTime = orderTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public byte getPaymentMethodId() {
    return paymentMethodId;
  }

  public void setPaymentMethodId(byte paymentMethodId) {
    this.paymentMethodId = paymentMethodId;
    PaymentMethodDao paymentMethodDao = new PaymentMethodDao();
    PaymentMethod paymentMethod = paymentMethodDao.getById(paymentMethodId);
    this.setPaymentMethod(paymentMethod.getPaymentMethod());
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber.trim();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
