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

public class StoreList extends AppCompatActivity {
  RecyclerView rv;
  Button logout;
  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button cart;
  Button order;

  public void readStoreData() {
    DatabaseReference storeRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference()
            .child("Stores");
    storeRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot storeSnapshot) {
            if (storeSnapshot.exists()) {
              ArrayList<String> storeName = new ArrayList<String>();
              ArrayList<String> keyList = new ArrayList<String>();
              for (DataSnapshot childSnapshot : storeSnapshot.getChildren()) {
                String store = childSnapshot.child("name").getValue(String.class);
                storeName.add(store);
                String key = childSnapshot.getKey();
                keyList.add(key);
                StoreAdapter adapter = new StoreAdapter(StoreList.this, storeName, keyList);
                rv.setAdapter(adapter);
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
    readStoreData();
    setContentView(R.layout.store);

    // Getting reference of recyclerView
    rv = (RecyclerView) findViewById(R.id.storeRecycler);
    // For Logout Button
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
            startActivity(new Intent(StoreList.this, CartViewActivity.class));
          }
        });

    // For order Button
    order = findViewById(R.id.ordersButton);
    order.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(StoreList.this, OrderHistoryViewActivity.class));
          }
        });

    // Setting the layout as linear
    // layout for vertical orientation
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    rv.setLayoutManager(linearLayoutManager);
  }

  void signOut() {
    gsc.signOut()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(Task<Void> task) {
                startActivity(new Intent(StoreList.this, MainActivity.class));
              }
            });
  }
}
