package com.example.b07_store_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
  ArrayList<Item> itemList = new ArrayList<Item>();
  Context context;
  ArrayList<String> itemIDList = new ArrayList<String>();

  public ItemListAdapter(Context context, ArrayList<Item> itemList, ArrayList<String> itemIDList) {
    this.itemList = itemList;
    this.context = context;
    this.itemIDList = itemIDList;
  }

  @Override
  public ItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdisplay, parent, false);

    // Passing view to ViewHolder
    ItemListAdapter.ViewHolder viewHolder = new ItemListAdapter.ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
    holder.itemName.setText(itemList.get(position).getName());
    holder.itemPrice.setText(itemList.get(position).getPrice());
    holder.itemDesc.setText(itemList.get(position).getDesc());
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView itemPrice;
    TextView itemName;
    TextView itemDesc;
    EditText itemQuantity;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public ViewHolder(View view) {
      super(view);
      itemName = view.findViewById(R.id.itemName);
      itemPrice = view.findViewById(R.id.itemPrice);
      itemDesc = view.findViewById(R.id.itemDesc);
      itemQuantity = (EditText) view.findViewById(R.id.itemQuantity);

      view.findViewById(R.id.cartButton)
          .setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  DatabaseReference dbRef =
                      FirebaseDatabase.getInstance(
                              "https://b07-grocery-default-rtdb.firebaseio.com/")
                          .getReference()
                          .child("Orders");
                  FirebaseUser user = auth.getCurrentUser();
                  int position = getAdapterPosition();
                  String itemID = itemIDList.get(position);
                  String name = itemList.get(position).getName();
                  String price = itemList.get(position).getPrice();
                  String desc = itemList.get(position).getDesc();
                  String storeID = itemList.get(position).getStoreOwnerID();
                  int number = Integer.parseInt(itemQuantity.getText().toString());
                  Item item = new Item(storeID, name, desc, price);
                  System.out.println(item);
                  Order order = new Order(user.getUid(), item, number);
                  dbRef.child(user.getUid() + "_" + itemID).setValue(order);
                }
              });
    }
  }
}
