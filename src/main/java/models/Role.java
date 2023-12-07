package models;

public class Role {
  private byte roleId;
  private String role;

  public Role() {
  }

  public Role(byte roleId, String role) {
    this.roleId = roleId;
    this.role = role;
  }

  public byte getRoleId() {
    return roleId;
  }

  public void setRoleId(byte roleId) {
    this.roleId = roleId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
