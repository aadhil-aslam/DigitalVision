package com.example.ecom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecom.databinding.ActivityMainBinding;
import com.example.ecom.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    boolean validateName = false;
    boolean validateMail = false;
    boolean validatePW = false;

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                finish();
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateMail = binding.email.getText().toString().trim().equalsIgnoreCase("");
                validatePW = binding.password.getText().toString().trim().equalsIgnoreCase("");
                validateName = binding.name.getText().toString().trim().equalsIgnoreCase("");

                if (validateMail) {
                    binding.email.setError("Email cannot be blank");
                }
                else {
                    binding.email.setError(null);
                }
                if (validatePW) {
                    binding.password.setError("Password cannot be blank");
                }
                else {
                    binding.password.setError(null);
                }
                if (validateName) {
                    binding.name.setError("Name cannot be blank");
                }else {
                    binding.name.setError(null);
                }

                //if (!validateMail && !validatePW && !validateName){
                    String name=binding.name.getText().toString();
                    String email=binding.email.getText().toString();
                    String password=binding.password.getText().toString();
                    createAccount(name, email, password);
                //}
            }
        });
    }

    private void createAccount(String name, String email, String password) {
        FirebaseAuth fAuth=FirebaseAuth.getInstance();

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating");
        progressDialog.setMessage("Account");
        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileChangeRequest);

                fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){
                                    progressDialog.cancel();
                                    Toast.makeText(SignupActivity.this,"Account Created. Please check you email for verification", Toast.LENGTH_SHORT).show();
                                    binding.name.setText("");
                                    binding.email.setText("");
                                    binding.password.setText("");
                                } else {
                                    progressDialog.cancel();
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}