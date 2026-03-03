package com.freshshipdrink.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MocktailAdapter extends RecyclerView.Adapter<MocktailAdapter.ViewHolder> {

    private final Context context;
    private final List<Mocktail> items;

    public MocktailAdapter(Context context, List<Mocktail> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mocktail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mocktail m = items.get(position);
        holder.tvName.setText(m.name);
        holder.tvPrice.setText(String.format("$%.2f", m.price));
        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MocktailDetailActivity.class);
            intent.putExtra("mocktail", m);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPrice;
        Button btnView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}

