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

public class RegisterFragment extends Fragment {

    private UserRepository userRepository;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        userRepository = new UserRepository(requireContext());
        inputName = view.findViewById(R.id.etName);
        inputEmail = view.findViewById(R.id.etEmail);
        inputPhone = view.findViewById(R.id.etPhone);
        inputPassword = view.findViewById(R.id.etPassword);

        View btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> doRegister());

        return view;
    }

    private void doRegister() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder error = new StringBuilder();
        boolean ok = userRepository.register(name, email, phone, password, error);
        if (ok) {
            Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish();
        } else {
            String message = error.length() > 0 ? error.toString() : "Register failed";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
