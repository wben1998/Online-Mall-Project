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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class RetrieveDataActivity extends AppCompatActivity {
  FirebaseAuth auth;

  ArrayList<Item> setitem;
  DatabaseReference database;

  Button plus;
  Button logout;
  GoogleSignInClient gsc;
  GoogleSignInOptions gso;
  Button orders;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_retrieve_data);
    auth = FirebaseAuth.getInstance();
    RecyclerView recyclerView = findViewById(R.id.recyclerItem);
    recyclerView.setLayoutManager(new LinearLayoutManager(RetrieveDataActivity.this));

    database =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference()
            .child("Items");
    setitem = new ArrayList<>();

    // For Add Button
    plus = findViewById(R.id.addButton);
    Recycleradapter adapter = new Recycleradapter(RetrieveDataActivity.this, setitem);
    recyclerView.setAdapter((adapter));
    plus.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            openInventoryActivity();
          }
        });

    // For Logout Button
    gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    gsc = GoogleSignIn.getClient(this, gso);
    logout = findViewById(R.id.logoutButton);
    logout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            signOut();
          }
        });
    // For Orders Button
    orders = findViewById(R.id.ordersButton);
    orders.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(RetrieveDataActivity.this, OrderViewActivity.class));
          }
        });

    database.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            setitem.clear();
            FirebaseUser user = auth.getCurrentUser();
            String storeOwnerid = user.getUid();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              Item item = dataSnapshot.getValue(Item.class);

              assert item != null;
              if (item.getStoreOwnerID().equals(storeOwnerid)) {
                setitem.add(item);
              }
            }
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  void signOut() {
    gsc.signOut()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(Task<Void> task) {
                startActivity(new Intent(RetrieveDataActivity.this, MainActivity.class));
              }
            });
  }

  public void openInventoryActivity() {
    Intent intent = new Intent(RetrieveDataActivity.this, InventoryActivity.class);
    startActivity(intent);
  }
}
