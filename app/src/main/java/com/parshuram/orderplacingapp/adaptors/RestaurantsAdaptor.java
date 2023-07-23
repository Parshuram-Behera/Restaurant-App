package com.parshuram.orderplacingapp.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parshuram.orderplacingapp.R;
import com.parshuram.orderplacingapp.models.RestaurantData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantsAdaptor extends RecyclerView.Adapter<RestaurantsAdaptor.RestaurantViewHolder> {

    private final ArrayList<RestaurantData> restaurantItems;

    public RestaurantsAdaptor(ArrayList<RestaurantData> restaurantItems){
        this.restaurantItems = restaurantItems;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item , parent , false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        RestaurantData data = restaurantItems.get(position);

        Picasso.get().load(data.getPrimary_image()).into(holder.restaurantImage);
        holder.restaurantName.setText(data.getName());
        holder.restaurantRating.setText(String.valueOf(data.getRating()) + "★");
        holder.restaurantDiscount.setText(String.valueOf(data.getDiscount()));
        holder.restaurantDistance.setText(String.valueOf(data.getDistance())+"km");

    }

    @Override
    public int getItemCount() {
        return restaurantItems.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{

        private final ImageView restaurantImage;
        private final TextView restaurantName;
        private final TextView restaurantRating;
        private final TextView restaurantDiscount;
        private final TextView restaurantDistance;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantRating = itemView.findViewById(R.id.txtViewRating);
            restaurantDistance = itemView.findViewById(R.id.txtdistance);
            restaurantDiscount = itemView.findViewById(R.id.discountText);
        }
    }
}
