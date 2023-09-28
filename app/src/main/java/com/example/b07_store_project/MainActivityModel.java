package com.example.b07_store_project;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

public class MainActivityModel {
  DatabaseReference rootRef;
  FirebaseAuth auth;

  MainActivityView view;

  String type;

  public MainActivityModel() {
    rootRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference()
            .child("Shoppers");
  }

  public void firebaseAuth(String idToken) {
    auth = FirebaseAuth.getInstance();
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
                    Intent i = new Intent(view, RegisterPage.class);
                    view.startActivity(i);
                    view.finish();
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
                }
              }
            });
  }

  public String getAccountType(String type) {
    return type;
  }

  public void checkAccType(String uID) {
    DatabaseReference rootRef = this.rootRef;
    rootRef.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Intent intent;
            // if shoppers has uId
            if (snapshot.hasChild(uID)) {
              getAccountType("Shopper");
              intent = new Intent(view, StoreList.class);
              view.startActivity(intent);
            } else {
              getAccountType("Owner");
              intent = new Intent(view, StoreRegistrationPage.class);
              view.startActivity(intent);
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    onActivityResult(requestCode, resultCode, data);
    if (requestCode == 200) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        this.firebaseAuth(account.getIdToken());
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public String checkAcctType(String uid) {
    if (uid.equals("tFDZj7pUpKeTmaMmsYaLkLFJ4gk1")) return "Shopper";
    return "Owner";
  }
}
