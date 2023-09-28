package com.example.b07_store_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.ViewHolder> {

  Context context;
  ArrayList<Item> items;

  Recycleradapter(Context context, ArrayList<Item> items) {
    this.context = context;
    this.items = items;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.item_viewholder, parent, false);
    ViewHolder viewHolder = new ViewHolder(v);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.txtname.setText(items.get(position).getName());
    holder.txtprice.setText(items.get(position).getPrice());
    holder.txtdescription.setText(items.get(position).getDesc());
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView txtname, txtprice, txtdescription;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      txtname = itemView.findViewById(R.id.txtname);
      txtprice = itemView.findViewById(R.id.txtprice);
      txtdescription = itemView.findViewById(R.id.txtdescription);
    }
  }
}
