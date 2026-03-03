package com.freshshipdrink.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    private UserRepository userRepository;
    private EditText inputEmail;
    private EditText inputPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        userRepository = new UserRepository(requireContext());
        inputEmail = view.findViewById(R.id.etEmail);
        inputPassword = view.findViewById(R.id.etPassword);

        View btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> doLogin());

        View linkCreateAccount = view.findViewById(R.id.tvCreateAccount);
        linkCreateAccount.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).runOnUiThread(() ->
                        ((AuthActivity) getActivity()).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.authContainer, new RegisterFragment())
                                .commit()
                );
            }
        });

        return view;
    }

    private void doLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder error = new StringBuilder();
        boolean ok = userRepository.login(email, password, error);
        if (ok) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish();
        } else {
            String message = error.length() > 0 ? error.toString() : "Login failed";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
