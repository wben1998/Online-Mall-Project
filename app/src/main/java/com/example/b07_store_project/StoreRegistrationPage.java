package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoreRegistrationPage extends AppCompatActivity {

  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button logout;

  Button submit1;

  TextView title, subheader;
  EditText inputStoreName, inputStoreDesc;

  DatabaseReference dbRef;

  FirebaseAuth auth;

  FirebaseUser user;

  String u;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_storerregpage);

    title = findViewById(R.id.textView4);
    subheader = findViewById(R.id.textView7);

    inputStoreName = findViewById(R.id.store_name);
    inputStoreDesc = findViewById(R.id.store_desc);

    submit1 = findViewById(R.id.submit_btn);

    logout = findViewById(R.id.Sign_Out_Owner);

    gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    gsc = GoogleSignIn.getClient(this, gso);

    dbRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference();

    auth = FirebaseAuth.getInstance();

    submit1.setOnClickListener(
        new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            user = auth.getCurrentUser();
            u = user.getUid().toString();
            String storename, storedesc;
            storename = inputStoreName.getText().toString();
            storedesc = inputStoreDesc.getText().toString();
            Store store1 = new Store();
            store1.setName(storename);
            store1.setDesc(storedesc);
            dbRef.child("Stores").child(u).setValue(store1);
            Intent intent = new Intent(StoreRegistrationPage.this, Storeownerpage.class);
            startActivity(intent);
          }
        });

    logout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            signOut();
          }
        });
  }

  void signOut() {
    gsc.signOut()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(Task<Void> task) {
                startActivity(new Intent(StoreRegistrationPage.this, MainActivity.class));
              }
            });
  }
}
