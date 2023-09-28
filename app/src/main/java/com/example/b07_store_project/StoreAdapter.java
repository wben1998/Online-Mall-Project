package com.example.b07_store_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
  ArrayList<String> storeName = new ArrayList<String>();
  ArrayList<String> keyList = new ArrayList<String>();
  Context context;

  public StoreAdapter(Context context, ArrayList<String> storeName, ArrayList<String> keyList) {
    this.storeName = storeName;
    this.keyList = keyList;
    this.context = context;
  }

  @Override
  public StoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.availablestorelist, parent, false);

    // Passing view to ViewHolder
    StoreAdapter.ViewHolder viewHolder = new StoreAdapter.ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
    holder.text.setText((String) storeName.get(position));
  }

  @Override
  public int getItemCount() {
    return storeName.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView text;

    public ViewHolder(View view) {
      super(view);
      text = (TextView) view.findViewById(R.id.storeName);
      view.findViewById(R.id.itemButton)
          .setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  int position = getAdapterPosition();
                  String keyPosition = keyList.get(position);
                  final Intent intent = new Intent(context, ItemPreviewList.class);
                  intent.putExtra("key", keyPosition);
                  context.startActivity(intent);
                }
              });
    }
  }
}
