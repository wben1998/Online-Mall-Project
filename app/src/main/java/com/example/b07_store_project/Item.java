package com.example.b07_store_project;

public class Item {
  private String storeOwnerID;
  private String name;
  private String desc;
  private String price;

  public Item() {}

  public Item(String storeOwnerID, String name, String desc, String price) {
    this.storeOwnerID = storeOwnerID;
    this.name = name;
    this.desc = desc;
    this.price = price;
  }

  public String getStoreOwnerID() {
    return storeOwnerID;
  }

  public void setStoreOwnerID(String storeOwnerID) {
    this.storeOwnerID = storeOwnerID;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return this.desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getPrice() {
    return this.price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result =
        1 * prime
            + (this.name == null ? 0 : this.name.hashCode())
            + (this.desc == null ? 0 : this.desc.hashCode())
            + (this.storeOwnerID == null ? 0 : this.storeOwnerID.hashCode())
            + (this.price == null ? 0 : this.price.hashCode());
    result = result * prime;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Item)) return false;
    Item other = (Item) obj;
    if (this.storeOwnerID.equals(other.storeOwnerID)
        && this.name.equals(other.name)
        && this.desc.equals(other.desc)
        && this.price.equals(other.price)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return name;
  }
}
