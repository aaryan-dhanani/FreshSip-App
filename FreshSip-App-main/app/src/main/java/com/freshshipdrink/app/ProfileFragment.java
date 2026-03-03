package com.freshshipdrink.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvUserInfo = view.findViewById(R.id.tvUserInfo);
        Switch switchDarkMode = view.findViewById(R.id.switchDarkMode);
        View btnLogout = view.findViewById(R.id.btnLogout);

        UserRepository userRepository = new UserRepository(requireContext());
        User user = userRepository.getLoggedInUser();
        if (user != null) {
            tvUserInfo.setText(user.email);
        } else {
            tvUserInfo.setText("Not logged in");
        }

        SharedPreferences prefs = requireContext().getSharedPreferences("settings", 0);
        boolean dark = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(dark);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnLogout.setOnClickListener(v -> {
            userRepository.logout();
            startActivity(new android.content.Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}
