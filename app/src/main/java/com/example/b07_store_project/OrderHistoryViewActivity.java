package com.example.b07_store_project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class OrderHistoryViewActivity extends AppCompatActivity {
  RecyclerView rv;
  FirebaseAuth auth;
  FirebaseUser user;
  ShopperOrderAdapter adapter;
  ArrayList<String> orders;

  public void readOrderData() {
    DatabaseReference dbRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference();
    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    dbRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            orders = new ArrayList<>();
            DataSnapshot order_snapshot = snapshot.child("Orders");
            if (order_snapshot.exists() && order_snapshot.hasChildren()) {
              for (DataSnapshot childSnapshot : order_snapshot.getChildren()) {
                String shopperID = childSnapshot.child("shopperID").getValue(String.class);
                boolean ordered = childSnapshot.child("isOrdered").getValue(boolean.class);
                if (shopperID.equals(user.getUid()) && ordered == true) {
                  String item = childSnapshot.child("item").child("name").getValue(String.class);
                  String price = childSnapshot.getValue(Order.class).formatPrice();
                  int quantity = childSnapshot.child("quantity").getValue(int.class);
                  boolean status = childSnapshot.child("isCompleted").getValue(boolean.class);

                  String ownerID =
                      childSnapshot.child("item").child("storeOwnerID").getValue(String.class);
                  DataSnapshot store_snapshot = snapshot.child("Stores");
                  String store_name =
                      store_snapshot.child(ownerID).child("name").getValue(String.class);

                  String isComplete;
                  if (status) {
                    isComplete = "Status: Complete";
                  } else {
                    isComplete = "Status: Incomplete";
                  }
                  String order_data =
                      "Store name: "
                          + store_name
                          + "\n"
                          + "Item: "
                          + item
                          + "\n"
                          + "Quantity: "
                          + quantity
                          + "\n"
                          + "Price: "
                          + price
                          + "\n"
                          + isComplete;
                  orders.add(order_data);
                }
              }
            }
            adapter = new ShopperOrderAdapter(OrderHistoryViewActivity.this, orders);
            rv.setAdapter(adapter);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopper_orders);
    rv = (RecyclerView) findViewById(R.id.order_history_rv);
    readOrderData();
    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
  }
}
