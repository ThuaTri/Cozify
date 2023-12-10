package models;

import java.sql.Timestamp;

public class Voucher {
  private int voucherId;
  private String voucherName;
  private String voucherCode;
  private byte voucherPercent;
  private int voucherQuantity;
  private Timestamp startDate;
  private Timestamp endDate;
  private boolean isHidden;

  public Voucher() {
  }

  public int getVoucherId() {
    return voucherId;
  }

  public void setVoucherId(int voucherId) {
    this.voucherId = voucherId;
  }

  public String getVoucherName() {
    return voucherName;
  }

  public void setVoucherName(String voucherName) {
    this.voucherName = voucherName;
  }

  public String getVoucherCode() {
    return voucherCode;
  }

  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  public byte getVoucherPercent() {
    return voucherPercent;
  }

  public void setVoucherPercent(byte voucherPercent) {
    this.voucherPercent = voucherPercent;
  }

  public int getVoucherQuantity() {
    return voucherQuantity;
  }

  public void setVoucherQuantity(int voucherQuantity) {
    this.voucherQuantity = voucherQuantity;
  }

  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  public Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

  public boolean getIsHidden() {
    return isHidden;
  }

  public void setIsHidden(boolean hidden) {
    isHidden = hidden;
  }
}
