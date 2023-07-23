package com.parshuram.orderplacingapp.adaptors;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parshuram.orderplacingapp.R;
import com.parshuram.orderplacingapp.models.TopCategoryData;

import java.util.ArrayList;

public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.CategoryViewHolder> {


    private final ArrayList<TopCategoryData> categoryNames;
    private int selectedItemPosition = RecyclerView.NO_POSITION;

//    public int itemPosition = 0;


    public TopCategoryAdapter(ArrayList<TopCategoryData> categoryNames) {
        this.categoryNames = categoryNames;
    }


    @NonNull
    @Override

    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TopCategoryData data = categoryNames.get(position);


        if (position == 0) {
            holder.categoryName.setText(data.getCategoryName());
            holder.categoryImage.setVisibility(View.GONE);

        } else {




            holder.categoryImage.setImageResource(data.getCategoryimage());

            holder.categoryName.setText(data.getCategoryName());
            holder.expandableLayout.setVisibility(View.GONE);



            boolean isItemSelected = (selectedItemPosition == position);
//
//            Toast.makeText(, "", Toast.LENGTH_SHORT).show();

            holder.expandableLayout.setVisibility(isItemSelected ? View.VISIBLE : View.GONE);
//            holder.categoryName.setText(data.getCategoryName());

            int backgroundColor = isItemSelected ? R.drawable.category_item_selected_background : R.drawable.category_item_background;
            holder.categoryItemLayout.setBackgroundResource(backgroundColor);
            int backgroundTextColor = isItemSelected ? Color.WHITE : 0;
            holder.categoryName.setTextColor(backgroundTextColor);


            }



        holder.categoryItemLayout.setOnClickListener(v -> {


            if (position == 0){

            }else {

                int previousSelectedItemPosition = selectedItemPosition;
                selectedItemPosition = position;
                notifyItemChanged(previousSelectedItemPosition);

                notifyItemChanged(selectedItemPosition);
            }

        });


    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout categoryItemLayout;
        private final RelativeLayout expandableLayout;
        private final TextView categoryName;
        private final ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImageView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            categoryItemLayout = itemView.findViewById(R.id.categoryItemLayout);
            categoryName = itemView.findViewById(R.id.categoryName);


        }
    }
}
