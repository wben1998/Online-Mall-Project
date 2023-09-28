package com.example.b07_store_project;

public class StoreOwner {
  String name;
  String email;
  String phone;
  Store store;

  public StoreOwner() {}

  public StoreOwner(String name, String email, String phone) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.store = null;
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof StoreOwner)) return false;
    StoreOwner other = (StoreOwner) obj;
    if (this.name.equals(other.name)
        && this.email.equals(other.email)
        && this.phone.equals(other.phone)
        && this.store.equals(other.store)) {
      return true;
    }
    return false;
  }
}
