package com.example.ecom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecom.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    boolean validateMail = false;
    boolean validatePW = false;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                finish();
            }
        });

        // Observer design pattern
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateMail = binding.email.getText().toString().trim().equalsIgnoreCase("");
                validatePW = binding.password.getText().toString().trim().equalsIgnoreCase("");

                if (validateMail) {
                    binding.email.setError("Email cannot be blank");
                }
                else {
                    binding.email.setError(null);
                }
                if (validatePW) {
                    binding.password.setError("Password cannot be blank");
                }

                if (!validateMail && !validatePW){
                    String email=binding.email.getText().toString();
                    String password=binding.password.getText().toString();
                    login(email, password);
                }
            }
        });
    }

    private void login(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Signing in");
        //progressDialog.setMessage("Account");1
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email.trim(), password.trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (firebaseAuth.getCurrentUser().isEmailVerified()){
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                } else {
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null
                //&& FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()
        )
        {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        }
        else {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        }
    }
}