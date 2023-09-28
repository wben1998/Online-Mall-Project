package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ItemPreviewList extends AppCompatActivity {
  RecyclerView recycler;
  Button logout;
  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button order;
  Button cart;

  public void readStoreData(String positionKey) {
    DatabaseReference storeRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference()
            .child("Items");

    storeRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
            if (itemSnapshot.exists()) {
              ArrayList<Item> itemData = new ArrayList<Item>();
              ArrayList<String> itemIDList = new ArrayList<String>();
              for (DataSnapshot childrenSnapshot : itemSnapshot.getChildren()) {
                String storeID = childrenSnapshot.child("storeOwnerID").getValue(String.class);
                if (storeID.equals(positionKey)) {
                  Item oneItem = childrenSnapshot.getValue(Item.class);
                  itemData.add(oneItem);
                  String ID = childrenSnapshot.getKey();
                  itemIDList.add(ID);
                  ItemListAdapter adapter =
                      new ItemListAdapter(ItemPreviewList.this, itemData, itemIDList);
                  recycler.setAdapter(adapter);
                }
              }
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.items);
    String positionKey;
    // Getting reference of recyclerView
    recycler = (RecyclerView) findViewById(R.id.recyclerView);
    ;
    Bundle bundle = getIntent().getExtras();
    if (savedInstanceState == null) {
      if (bundle == null) {
        positionKey = null;
      } else {
        positionKey = bundle.getString("key");
      }
    } else {
      positionKey = (String) savedInstanceState.getSerializable("key");
    }
    readStoreData(positionKey);

    gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    gsc = GoogleSignIn.getClient(this, gso);
    logout = findViewById(R.id.logoutButton);
    logout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            signOut();
          }
        });

    // For View Cart Button
    cart = findViewById(R.id.viewCartButton);
    cart.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(ItemPreviewList.this, CartViewActivity.class));
          }
        });

    // For order Button
    order = findViewById(R.id.ordersButton);
    order.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(ItemPreviewList.this, OrderHistoryViewActivity.class));
          }
        });

    // Setting the layout as linear
    // layout for vertical orientation
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    recycler.setLayoutManager(linearLayoutManager);
  }

  void signOut() {
    gsc.signOut()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(Task<Void> task) {
                startActivity(new Intent(ItemPreviewList.this, MainActivity.class));
              }
            });
  }
}
