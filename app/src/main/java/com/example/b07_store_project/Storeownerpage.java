package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Storeownerpage extends AppCompatActivity {
  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button logout;
  Button order, inventory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_storeownerpage);
    gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    gsc = GoogleSignIn.getClient(this, gso);
    logout = findViewById(R.id.logoutButton);
    order = findViewById(R.id.ordersButton);
    inventory = findViewById(R.id.inventoryButton);
    inventory.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            openinventory();
          }
        });
    order.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            openOrders();
          }
        });
    logout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            signOut();
          }
        });
  }

  void openinventory() {
    Intent intent = new Intent(Storeownerpage.this, RetrieveDataActivity.class);
    startActivity(intent);
  }

  void openOrders() {
    Intent intent = new Intent(Storeownerpage.this, OrderViewActivity.class);
    startActivity(intent);
  }

  void signOut() {
    gsc.signOut()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(Task<Void> task) {
                startActivity(new Intent(Storeownerpage.this, MainActivity.class));
              }
            });
  }
}
