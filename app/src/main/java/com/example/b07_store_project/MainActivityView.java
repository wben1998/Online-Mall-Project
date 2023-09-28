package com.example.b07_store_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.b07_store_project.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivityView extends AppCompatActivity {

  MainActivityPresenter presenter;

  MainActivityModel model;
  ActivityMainBinding binding;

  GoogleSignInOptions gso;
  GoogleSignInClient gsc;
  Button signInBtn; // change to sign in
  Button registerBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.appBarMain.toolbar);
    presenter = new MainActivityPresenter(this, new MainActivityModel(), "");
    // Register with Google
    registerBtn = findViewById(R.id.register_btn);
    registerBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(MainActivityView.this, RegisterPage.class);
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

    signInBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            signIn();
          }
        });
  }

  public void signIn() {
    Intent signInIntent = gsc.getSignInIntent();
    super.onActivityResult(200, 200, signInIntent);
  }
}
