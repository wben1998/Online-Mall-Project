package com.example.b07_store_project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExampleUnitTest {
  @Mock MainActivityModel model;
  @Mock MainActivityView view;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void checkShopperType() {
    // Mock interactions with the view
    String type = "tFDZj7pUpKeTmaMmsYaLkLFJ4gk1";
    MainActivityPresenter presenter = new MainActivityPresenter(view, model, "");

    // Stub the behavior of the model's getAccountType method
    when(model.checkAcctType(type)).thenReturn("Shopper");

    // Call the checkAccountType method
    String result = presenter.checkAccountType(type);

    // Assert the captured value matches the expected value
    assertEquals("Shopper", result);
  }

  @Test
  public void checkOwnerType() {
    // Mock interactions with the view
    String type = "NON_SHOPPER_ID";
    MainActivityPresenter presenter = new MainActivityPresenter(view, model, "");

    // Stub the behavior of the model's getAccountType method
    when(model.checkAcctType(type)).thenReturn("Owner");

    // Call the checkAccountType method
    String result = presenter.checkAccountType(type);

    // Assert the captured value matches the expected value
    assertEquals("Owner", result);
  }
}
