package com.example.ecom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecom.databinding.ActivityDashboardBinding;
import com.example.ecom.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    private ProductsAdapter productsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         setSupportActionBar(binding.myToolbar);

         productsAdapter = new ProductsAdapter(this);
         binding.productRecycler.setAdapter(productsAdapter);
         binding.productRecycler.setLayoutManager(new LinearLayoutManager(this));

        getProducts();

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (FirebaseAuth.getInstance().getCurrentUser() == null
                    //&& FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()
                //)
                //{
                    //startActivity(new Intent(DashboardActivity.this, SignupActivity.class));
                //}
                //else {
                    startActivity(new Intent(DashboardActivity.this, CartActivity.class));
                //}
            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (FirebaseAuth.getInstance().getCurrentUser() == null
                    //&& FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()
                //)
                //{
                 //   startActivity(new Intent(DashboardActivity.this, SignupActivity.class));
                //}
                //else {
                    startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                //}
                //FirebaseAuth.getInstance().signOut();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = binding.searchButton.getText().toString();
                startActivity(new Intent(DashboardActivity.this, SearchActivity.class));
            }
        });
    }

    private void getProducts() {
        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("show",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList) {
                            ProductModel productModel=ds.toObject(ProductModel.class);
                            productsAdapter.addProduct(productModel);
                        }
                    }
                });
    }
}