package com.example.b07_store_project;

public class Shopper {
  String name;
  String email;
  String phone;

  //  LinkedHashSet<Order> orders;

  // ArrayList cart;

  public Shopper() {}

  public Shopper(String name, String email, String phone) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    // this.orders = new LinkedHashSet<Order>();
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

  /*
    public LinkedHashSet<Order> getOrders() {
      return orders;
    }

    public void setOrders(LinkedHashSet<Order> orders) {
      this.orders = orders;
    }
  */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Shopper)) return false;
    Shopper other = (Shopper) obj;
    if (this.name.equals(other.name)
        && this.email.equals(other.email)
        && this.phone.equals(other.phone)) {
      //      && this.orders.equals(other.orders))
      return true;
    }
    return false;
  }
}
