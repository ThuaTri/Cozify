package models;

public class PaymentMethod {
  private byte paymentMethodId;
  private String paymentMethod;

  public PaymentMethod() {
  }

  public PaymentMethod(byte paymentMethodId, String paymentMethod) {
    this.paymentMethodId = paymentMethodId;
    this.paymentMethod = paymentMethod;
  }

  public byte getPaymentMethodId() {
    return paymentMethodId;
  }

  public void setPaymentMethodId(byte paymentMethodId) {
    this.paymentMethodId = paymentMethodId;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}
