package com.example.b07_store_project;

import java.text.DecimalFormat;

public class Order {
  boolean isCompleted;
  boolean isOrdered;
  String shopperID;
  Item item;
  int quantity;

  public Order() {}

  public Order(String shopperID, Item item, int quantity) {
    this.shopperID = shopperID;
    this.isCompleted = false;
    this.isOrdered = false;
    this.item = item;
    this.quantity = quantity;
  }

  public String formatPrice() {
    final DecimalFormat df = new DecimalFormat("0.00");
    double ret_price;
    char currency = '$';
    final String valid = "1234567890.";
    String price_string = String.valueOf(this.getItem().getPrice());
    String new_price_string;
    boolean start_or_end;
    if (valid.contains("" + price_string.charAt(0)) == false) {
      start_or_end = false;
      currency = price_string.charAt(0);
      price_string = price_string.replace(price_string.charAt(0), ' ');
    } else {
      start_or_end = true;
      currency = price_string.charAt(price_string.length() - 1);
      price_string = price_string.replace(price_string.charAt(price_string.length() - 1), ' ');
    }
    price_string = price_string.trim();
    ret_price = Double.parseDouble(price_string);

    if (start_or_end) {
      return df.format(ret_price * this.quantity) + currency;
    }
    return currency + df.format(ret_price * this.quantity);
  }

  @Override
  public String toString() {
    if (this.getIsCompleted()) {
      return "Completed";
    }
    return "Incomplete";
  }

  public boolean getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(boolean completed) {
    isCompleted = completed;
  }

  public boolean getIsOrdered() {
    return isOrdered;
  }

  public void setIsOrdered(boolean ordered) {
    isOrdered = ordered;
  }

  public String getShopperID() {
    return shopperID;
  }

  public void setShopperID(String shopperID) {
    this.shopperID = shopperID;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Order)) return false;
    Order other = (Order) obj;
    if (this.shopperID.equals(other.shopperID)
        && this.quantity == other.quantity
        && this.isCompleted == other.isCompleted
        && this.item.equals(other.item)
        && this.isOrdered == other.isOrdered) {
      return true;
    }
    return false;
  }
}
