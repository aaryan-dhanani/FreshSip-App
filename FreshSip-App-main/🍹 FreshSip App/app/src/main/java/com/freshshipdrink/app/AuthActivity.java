package com.freshshipdrink.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        View loginTab = findViewById(R.id.btnLoginTab);
        View registerTab = findViewById(R.id.btnRegisterTab);

        loginTab.setOnClickListener(v -> showFragment(new LoginFragment()));
        registerTab.setOnClickListener(v -> showFragment(new RegisterFragment()));

        showFragment(new LoginFragment());
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.authContainer, fragment);
        ft.commit();
    }
}
