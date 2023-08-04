// OrderItemAdapter.java
package com.example.fooddeliveryapp.Adapters.Order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Order.OrderItemModel;
import com.example.fooddeliveryapp.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private ArrayList<OrderItemModel> orderItemsList;

    public OrderItemAdapter(ArrayList<OrderItemModel> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.imageView.setImageResource(orderItemsList.get(position).getImage());
        holder.name.setText(orderItemsList.get(position).getName());
        holder.price.setText(orderItemsList.get(position).getPrice());
        holder.totalPrice.setText(orderItemsList.get(position).getTotalPrice());
        holder.rating.setText(orderItemsList.get(position).getRating());
        holder.timing.setText(orderItemsList.get(position).getTiming());
        holder.quantity.setText(orderItemsList.get(position).getQuantityText() + "x");
    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, timing, rating, price, totalPrice, quantity;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.order_img);
            name = itemView.findViewById(R.id.order_name);
            timing = itemView.findViewById(R.id.order_timing);
            rating = itemView.findViewById(R.id.order_ratting);
            price = itemView.findViewById(R.id.foodPrice_order);
            totalPrice = itemView.findViewById(R.id.order_total_price);
            quantity = itemView.findViewById(R.id.order_quantity);
        }
    }
}
