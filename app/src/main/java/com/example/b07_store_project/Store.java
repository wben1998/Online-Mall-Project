package com.example.b07_store_project;

import java.util.ArrayList;

public class Store {
  String name;
  String desc;
  ArrayList<Order> orders;

  public Store() {}

  public Store(String name) {
    this.name = name;
    this.orders = null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public ArrayList<Order> getOrders() {
    return orders;
  }

  public void setOrders(ArrayList<Order> orders) {
    this.orders = orders;
  }

  public void addOrder(Order new_order) {
    this.orders.add(new_order);
  }

  public void deleteOrder(Order designated_order) {
    int count = 0;
    for (Order order : this.orders) {
      if (order.equals(designated_order)) {
        this.orders.remove(count);
        break;
      }
      count++;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Store)) return false;
    Store other = (Store) obj;
    if (this.name.equals(other.name)
        && this.desc.equals(other.desc)
        && this.orders.equals(other.orders)) {
      return true;
    }
    return false;
  }
}
