package com.freshshipdrink.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private final List<Order> orders = new ArrayList<>();
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        RecyclerView rv = view.findViewById(R.id.rvOrders);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(orders);
        rv.setAdapter(adapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .whereEqualTo("userId", uid)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    orders.clear();
                    snap.getDocuments().forEach(doc -> {
                        double total = doc.getDouble("total") != null ? doc.getDouble("total") : 0;
                        long createdAt = doc.getLong("createdAt") != null ? doc.getLong("createdAt") : 0;
                        String status = doc.getString("status");
                        orders.add(new Order(doc.getId(), createdAt, total, status != null ? status : ""));
                    });
                    adapter.notifyDataSetChanged();
                });
    }
}
