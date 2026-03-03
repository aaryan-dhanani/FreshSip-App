package com.freshshipdrink.app;

import android.os.Bundle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rv = view.findViewById(R.id.rvMocktails);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new MocktailAdapter(requireContext(), createSampleMocktails()));

        return view;
    }

    private List<Mocktail> createSampleMocktails() {
        List<Mocktail> list = new ArrayList<>();
        list.add(new Mocktail("blue_moon", "Blue Moon Chill", "Cool blue mocktail.", 12.50, 4.8));
        list.add(new Mocktail("virgin_mojito", "Virgin Mojito", "Minty and fresh.", 9.00, 4.9));
        list.add(new Mocktail("sunset_ginger", "Sunset Ginger", "Ginger and citrus.", 10.00, 4.5));
        list.add(new Mocktail("berry_sparkle", "Berry Sparkle", "Mixed berries fizz.", 11.20, 4.7));
        return list;
    }
}
