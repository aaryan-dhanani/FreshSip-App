package com.freshshipdrink.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MocktailDetailActivity extends AppCompatActivity {

    private Mocktail mocktail;
    private int quantity = 1;
    private TextView tvQuantity;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocktail_detail);

        mocktail = (Mocktail) getIntent().getSerializableExtra("mocktail");
        if (mocktail == null) {
            finish();
            return;
        }

        TextView tvName = findViewById(R.id.tvName);
        TextView tvDescription = findViewById(R.id.tvDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvTotal = findViewById(R.id.tvTotalPrice);
        Button btnMinus = findViewById(R.id.btnMinus);
        Button btnPlus = findViewById(R.id.btnPlus);
        View btnAddToCart = findViewById(R.id.btnAddToCart);

        tvName.setText(mocktail.name);
        tvDescription.setText(mocktail.description);

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantity();
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantity();
        });

        btnAddToCart.setOnClickListener(v -> {
            CartRepository repo = new CartRepository(this);
            repo.addOrIncrement(mocktail, quantity);
            finish();
        });

        updateQuantity();
    }

    private void updateQuantity() {
        tvQuantity.setText(String.valueOf(quantity));
        double total = mocktail.price * quantity;
        tvTotal.setText(String.format("$%.2f", total));
    }
}
