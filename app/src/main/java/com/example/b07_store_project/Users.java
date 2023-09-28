package com.example.b07_store_project;

public class Users {

  String userId, name, email;

  public Users(String userId, String name, String email) {
    this.userId = userId;
    this.name = name;
    this.email = email;
  }

  public Users() {}

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
