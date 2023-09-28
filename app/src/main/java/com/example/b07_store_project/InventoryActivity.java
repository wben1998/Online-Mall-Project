package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class InventoryActivity extends AppCompatActivity {
  EditText mName;
  EditText mPrice;
  EditText mDescription;
  Button mSend;
  DatabaseReference GDBref;

  FirebaseAuth auth;
  GoogleSignInClient gsc;
  GoogleSignInOptions gso;
  Button mCheck;
  Button logout;
  Button orders;

  boolean isFound = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);
    auth = FirebaseAuth.getInstance();
    mName = findViewById(R.id.name);
    mPrice = findViewById(R.id.price);
    mDescription = findViewById(R.id.description);
    mSend = findViewById(R.id.send);
    GDBref =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference()
            .child("Items");
    mCheck = findViewById(R.id.check_inventory);
    mSend.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            insertitemdata();
          }
        });

    mCheck.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            openRetrieveDataActivity();
          }
        });

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
            startActivity(new Intent(InventoryActivity.this, OrderViewActivity.class));
          }
        });

    // For Orders Button
  }

  void signOut() {
    gsc.signOut()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(Task<Void> task) {
                startActivity(new Intent(InventoryActivity.this, MainActivity.class));
              }
            });
  }

  public void openRetrieveDataActivity() {
    Intent intent = new Intent(this, RetrieveDataActivity.class);
    startActivity(intent);
  }

  private boolean isValidPriceString(String price) {
    final String valid = "0123456789.$€£¥";
    final String currency = "$€£¥";
    boolean isStartOrEndValid = false;
    for (char ch : price.toCharArray()) {
      if (!valid.contains("" + ch)) {
        return false;
      }
    }
    for (char ch : currency.toCharArray()) {
      if (price.startsWith("" + ch) && (!price.endsWith("" + ch))) {
        isStartOrEndValid = true;
      } else if ((!price.startsWith("" + ch)) && price.endsWith("" + ch)) {
        isStartOrEndValid = true;
      }
    }
    if (isStartOrEndValid) {
      return true;
    }
    return false;
  }

  private void insertitemdata() {
    String Name = mName.getText().toString();
    String Price = mPrice.getText().toString();
    String Description = mDescription.getText().toString();
    FirebaseUser user = auth.getCurrentUser();
    String storeOwnerid = user.getUid();
    if (Name.isEmpty() || Price.isEmpty() || Description.isEmpty() || storeOwnerid.isEmpty()) {
      Toast myToast =
          Toast.makeText(
              InventoryActivity.this,
              "Please provide the full details of your item",
              Toast.LENGTH_SHORT);
      myToast.show();
    } else if (!isValidPriceString(Price)) {
      Toast.makeText(
              InventoryActivity.this,
              "The price is not in a valid format. ($0.00 or 0.00$)",
              Toast.LENGTH_SHORT)
          .show();
    } else {

      GDBref.addListenerForSingleValueEvent(
          new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              isFound = false;
              if (dataSnapshot.exists()) {
                for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                  if (childsnapshot.child("name").getValue(String.class).equals(Name)
                      && childsnapshot
                          .child("storeOwnerID")
                          .getValue(String.class)
                          .equals(storeOwnerid)) {
                    isFound = true;
                    break;
                  }
                }
                if (isFound) {
                  Toast.makeText(
                          InventoryActivity.this,
                          "An item with the same name already exists",
                          Toast.LENGTH_SHORT)
                      .show();
                } else if (!isFound) {
                  Item item = new Item(storeOwnerid, Name, Description, Price);
                  GDBref.child(Name + "_" + storeOwnerid).setValue(item);
                }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
          });
    }
  }
}
