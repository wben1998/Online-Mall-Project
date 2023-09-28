package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.b07_store_project.databinding.ActivityMainBinding;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button signInBtn; // change to sign in
  Button registerBtn;

  FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.appBarMain.toolbar);

    // Register with Google
    registerBtn = findViewById(R.id.register_btn);
    registerBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RegisterPage.class);
            startActivity(intent);
          }
        });

    // Sign in with Google
    signInBtn = findViewById(R.id.signIn_btn);

    gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    gsc = GoogleSignIn.getClient(this, gso);

    auth = FirebaseAuth.getInstance();

    signInBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            signIn();
          }
        });
  }

  void signIn() {
    Intent signInIntent = gsc.getSignInIntent();
    startActivityForResult(signInIntent, 200);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 200) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuth(account.getIdToken());
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void firebaseAuth(String idToken) {
    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    auth.fetchSignInMethodsForEmail(auth.getCurrentUser().getEmail())
        .addOnCompleteListener(
            new OnCompleteListener<SignInMethodQueryResult>() {
              @Override
              public void onComplete(Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                  boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                  if (isNewUser) {
                    // User is a new user, call constructors and save to database
                    Intent i = new Intent(MainActivity.this, RegisterPage.class);
                    Toast.makeText(MainActivity.this, "Please register", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                  } else {
                    // User already exists, proceed to SecondActivity directly
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                  FirebaseUser user = auth.getCurrentUser();
                                  checkAccType(user.getUid().toString());
                                }
                              }
                            });
                  }
                } else {
                  // Error occurred while checking for existing user
                  Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  void checkAccType(String uID) {
    DatabaseReference rootRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference()
            .child("Shoppers");

    rootRef.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Intent intent;
            // if shoppers has uId
            if (snapshot.hasChild(uID)) {
              intent = new Intent(MainActivity.this, StoreList.class);
              startActivity(intent);
            } else {
              intent = new Intent(MainActivity.this, Storeownerpage.class);
              startActivity(intent);
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }
}
