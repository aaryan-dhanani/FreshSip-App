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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        auth = FirebaseAuth.getInstance();
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

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && auth.getCurrentUser() != null) {
                            String uid = auth.getCurrentUser().getUid();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("phone", phone);
                            user.put("createdAt", System.currentTimeMillis());

                            db.collection("users")
                                    .document(uid)
                                    .set(user);

                            Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), MainActivity.class));
                            requireActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Register failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
