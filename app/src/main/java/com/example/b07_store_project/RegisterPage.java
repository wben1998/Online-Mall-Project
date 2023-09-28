package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

  private DatabaseReference dbRef;
  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button createAccountBtn;

  Switch switch1;

  FirebaseAuth auth;
  boolean isChecked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register_page);

    // Switch button for selecting if user want to sign up as store owner
    switch1 = findViewById(R.id.store_owner_switch);
    isChecked = switch1.isChecked();
    switch1.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          if (isChecked) {
            // Switch is checked
          } else {
            // Switch is not checked
          }
        });

    // Create Account Button
    createAccountBtn = findViewById(R.id.create_account);

    gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    gsc = GoogleSignIn.getClient(this, gso);

    auth = FirebaseAuth.getInstance();

    createAccountBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            registerAccount();
          }
        });

    dbRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference();
  }

  void registerAccount() {
    Intent signInIntent = gsc.getSignInIntent();
    startActivityForResult(signInIntent, 100);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 100) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuth(account.getIdToken());
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }

      // navigateToSecondAct();
    }
  }

  private void firebaseAuth(String idToken) {
    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    auth.signInWithCredential(credential)
        .addOnCompleteListener(
            new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(Task<AuthResult> task) {
                Exception exceptionFirebase = task.getException();
                if (task.isSuccessful()) {
                  FirebaseUser user = auth.getCurrentUser();
                  Users users;
                  Intent intent;
                  isChecked = switch1.isChecked();
                  if (!isChecked) { // Switch is unchecked
                    users = new Users(user.getUid(), user.getDisplayName(), user.getEmail());
                    dbRef.child("Shoppers").child(user.getUid()).setValue(users);
                    intent = new Intent(RegisterPage.this, StoreList.class);
                    startActivity(intent);
                  } else {
                    users = new Users(user.getUid(), user.getDisplayName(), user.getEmail());
                    dbRef.child("Owners").child(user.getUid()).setValue(users);
                    intent = new Intent(RegisterPage.this, StoreRegistrationPage.class);
                    startActivity(intent);
                  }

                } else {
                  Toast.makeText(RegisterPage.this, "Error", Toast.LENGTH_SHORT).show();
                }
              }
            });
  }
}
