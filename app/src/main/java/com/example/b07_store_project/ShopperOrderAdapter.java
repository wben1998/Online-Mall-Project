package com.example.b07_store_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShopperOrderAdapter extends RecyclerView.Adapter<ShopperOrderAdapter.ViewHolder> {
  List<String> orders;

  public ShopperOrderAdapter(Context context, List<String> list_of_orders) {
    this.orders = list_of_orders;
  }

  @Override
  public ShopperOrderAdapter.ViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    View contactView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.shopper_orders, parent, false);
    ShopperOrderAdapter.ViewHolder viewHolder = new ShopperOrderAdapter.ViewHolder(contactView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ShopperOrderAdapter.ViewHolder holder, int position) {
    String order = this.orders.get(position);
    TextView textView = holder.orderTextView;
    textView.setText(order);
  }

  @Override
  public int getItemCount() {
    return orders.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView orderTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      orderTextView = (TextView) itemView.findViewById(R.id.shopper_order_details);
    }
  }
}
