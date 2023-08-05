package com.example.fooddeliveryapp.Adapters.Order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Order.OrderModel;
import com.example.fooddeliveryapp.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderModel> orderList;

    public OrderAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    // Define an interface for the click listener
    public interface OnItemClickListener {
        void onItemClick(OrderModel order);
    }

    // Click listener variable
    private OnItemClickListener onItemClickListener;

    // Setter method to set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.orderStatus.setText(order.getStatus());
        holder.orderDate.setText(order.getOrderDate());
        holder.orderPrice.setText(order.getTotalPrice());
        holder.orderStatusImage.setImageResource(order.getOrderStatusImage());

        // Set click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView orderStatusImage;
        TextView orderStatus, orderDate, orderPrice;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            orderStatus = itemView.findViewById(R.id.order_status);
            orderDate = itemView.findViewById(R.id.order_date);
            orderPrice = itemView.findViewById(R.id.order_total_price);
            orderStatusImage = itemView.findViewById(R.id.order_status_img);
        }
    }
}
