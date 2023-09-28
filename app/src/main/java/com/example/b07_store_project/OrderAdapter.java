package com.example.b07_store_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
  List<String> orders;

  public OrderAdapter(Context context, List<String> list_of_orders) {
    this.orders = list_of_orders;
  }

  @Override
  public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View contactView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.orders, parent, false);
    OrderAdapter.ViewHolder viewHolder = new OrderAdapter.ViewHolder(contactView);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
    String order = this.orders.get(position);
    TextView textView = holder.orderTextView;
    textView.setText(order);
  }

  @Override
  public int getItemCount() {
    return orders.size();
  }

  public void updateOrderCompletionStatus(int position, boolean isCompleted) {
    // Get the order ID or any identifier you use to uniquely identify the order
    String orderString = orders.get(position);
    String OrderID = extractOrderId(orderString);
    // Assuming you have a reference to your Firebase database
    DatabaseReference dbRef =
        FirebaseDatabase.getInstance("https://b07-grocery-default-rtdb.firebaseio.com/")
            .getReference();

    // Update the isCompleted value for the specific order
    dbRef.child("Orders").child(OrderID).child("isCompleted").setValue(true);
  }

  public String extractOrderId(String orderData) {
    String orderIdPrefix = "Order ID:\n";
    int startIndex = orderData.indexOf(orderIdPrefix);

    if (startIndex != -1) {
      // Found the start of the order ID
      startIndex += orderIdPrefix.length();
      // Find the end of the order ID, which could be the end of the string or the next line
      int endIndex = orderData.indexOf("\n", startIndex);
      if (endIndex == -1) {
        // If not found, use the length of the string
        endIndex = orderData.length();
      }
      // Extract the order ID
      String orderId = orderData.substring(startIndex, endIndex).trim();
      return orderId;
    } else {
      // Order ID not found in the order data
      return null;
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView orderTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      orderTextView = (TextView) itemView.findViewById(R.id.order_details);
      itemView
          .findViewById(R.id.change_order_status)
          .setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  int position = getAdapterPosition();
                  // Call the updateOrderCompletionStatus method to toggle the isCompleted value
                  updateOrderCompletionStatus(position, true);
                }
              });
    }
  }
}
