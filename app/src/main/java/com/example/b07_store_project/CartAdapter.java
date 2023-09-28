package com.example.b07_store_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
  List<String> orders;

  public CartAdapter(Context context, List<String> list_of_orders) {
    this.orders = list_of_orders;
  }

  @Override
  public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View contactView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
    CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(contactView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
    String order = this.orders.get(position);
    TextView textView = holder.cartTextView;
    textView.setText(order);
  }

  @Override
  public int getItemCount() {
    return orders.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView cartTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      cartTextView = (TextView) itemView.findViewById(R.id.cart_order_details);
    }
  }
}
