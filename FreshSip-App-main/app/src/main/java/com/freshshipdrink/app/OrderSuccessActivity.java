package com.freshshipdrink.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccessActivity extends AppCompatActivity {

    private static final String EXTRA_ORDER_ID = "order_id";
    private static final String EXTRA_ETA = "eta";

    public static void start(Context context, String orderId, String eta) {
        Intent intent = new Intent(context, OrderSuccessActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        intent.putExtra(EXTRA_ETA, eta);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        String orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        String eta = getIntent().getStringExtra(EXTRA_ETA);

        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvEta = findViewById(R.id.tvEta);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        if (orderId != null) {
            tvOrderId.setText("Order ID: " + orderId);
        }
        if (eta != null) {
            tvEta.setText("Estimated Time: " + eta);
        }

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
