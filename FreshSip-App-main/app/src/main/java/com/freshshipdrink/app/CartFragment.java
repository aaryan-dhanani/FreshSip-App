package com.freshshipdrink.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private final List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter adapter;
    private TextView tvTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRepository = new CartRepository(requireContext());
        orderRepository = new OrderRepository(requireContext());
        userRepository = new UserRepository(requireContext());

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

        User user = userRepository.getLoggedInUser();
        if (user == null) {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        long orderId = orderRepository.createOrder(user.id, cartItems);
        if (orderId == -1) {
            Toast.makeText(getContext(), "Could not place order", Toast.LENGTH_SHORT).show();
            return;
        }

        cartRepository.clear();
        loadCart();
        OrderSuccessActivity.start(requireContext(), String.valueOf(orderId), "20 - 30 mins");
    }
}
