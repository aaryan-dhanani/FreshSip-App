package com.freshshipdrink.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        View btn = findViewById(R.id.btnGetStarted);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(GetStartedActivity.this, AuthActivity.class));
            finish();
        });
    }
}
