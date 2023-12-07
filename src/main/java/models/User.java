package models;

import daos.RoleDao;

import java.io.Serializable;

public class User implements Serializable {
    // user_id, auto-incremented, primary key, not null
    private int userId;

    // username, not null
    private String username;

    // password, not null
    private String password;

    // email, not null
    private String email;

    // role, not null
    private String role;
    private byte roleId;

    // first_name
    private String firstName;

    // last_name
    private String lastName;

    // phone_number
    private String phoneNumber;

    // address
    private String address;

    public User() {
    }

    public User(String username, String password, String email, byte roleId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.setRoleId(roleId);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte getRoleId() {
        return roleId;
    }

    public void setRoleId(byte roleId) {
        this.roleId = roleId;
        RoleDao roleDao = new RoleDao();
        Role role = roleDao.getById(roleId);
        this.setRole(role.getRole());
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
