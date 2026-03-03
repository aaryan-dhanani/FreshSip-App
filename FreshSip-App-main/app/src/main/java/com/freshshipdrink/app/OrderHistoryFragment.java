package com.freshshipdrink.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private final List<Order> orders = new ArrayList<>();
    private OrderAdapter adapter;
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderRepository = new OrderRepository(requireContext());
        userRepository = new UserRepository(requireContext());

        RecyclerView rv = view.findViewById(R.id.rvOrders);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(orders);
        rv.setAdapter(adapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        User user = userRepository.getLoggedInUser();
        if (user == null) {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        orders.clear();
        orders.addAll(orderRepository.getOrdersForUser(user.id));
        adapter.notifyDataSetChanged();
    }
}
