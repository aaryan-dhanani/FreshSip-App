package com.freshshipdrink.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {

    private CartRepository cartRepository;
    private final List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter adapter;
    private TextView tvTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRepository = new CartRepository(requireContext());

        RecyclerView rv = view.findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(cartItems, item -> {
            cartRepository.remove(item.id);
            loadCart();
        });
        rv.setAdapter(adapter);

        tvTotal = view.findViewById(R.id.tvTotal);
        View btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(v -> placeOrder());

        loadCart();

        return view;
    }

    private void loadCart() {
        cartItems.clear();
        cartItems.addAll(cartRepository.getAll());
        adapter.notifyDataSetChanged();
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.price * item.quantity;
        }
        tvTotal.setText(String.format("Total: $%.2f", total));
    }

    private void placeOrder() {
        if (cartItems.isEmpty()) {
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> order = new HashMap<>();
        order.put("userId", auth.getCurrentUser().getUid());
        order.put("status", "Preparing");
        order.put("createdAt", System.currentTimeMillis());

        List<Map<String, Object>> items = new ArrayList<>();
        double total = 0;
        for (CartItem c : cartItems) {
            Map<String, Object> m = new HashMap<>();
            m.put("mocktailId", c.mocktailId);
            m.put("name", c.name);
            m.put("price", c.price);
            m.put("quantity", c.quantity);
            items.add(m);
            total += c.price * c.quantity;
        }
        order.put("items", items);
        order.put("total", total);

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(docRef -> {
                    cartRepository.clear();
                    loadCart();
                    OrderSuccessActivity.start(requireContext(), docRef.getId(), "20 - 30 mins");
                });
    }
}
