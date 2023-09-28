package com.example.b07_store_project;

public class MainActivityPresenter {
  MainActivityView view;
  MainActivityModel model;
  String accountType;

  public void setAccountType(String type) {
    this.accountType = type;
  }

  public MainActivityPresenter(MainActivityView view, MainActivityModel model, String accountType) {
    this.model = model;
    this.view = view;
    this.accountType = accountType;
  }

  public String checkAccountType(String UID) {
    return model.checkAcctType(UID);
  }
}
