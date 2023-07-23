package com.parshuram.orderplacingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parshuram.orderplacingapp.R;
import com.parshuram.orderplacingapp.adaptors.RestaurantsAdaptor;
import com.parshuram.orderplacingapp.adaptors.TopCategoryAdapter;
import com.parshuram.orderplacingapp.models.RestaurantData;
import com.parshuram.orderplacingapp.models.TopCategoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HomeFragment extends Fragment {

    private final ArrayList<TopCategoryData> categoryItems = new ArrayList<>();
    private final ArrayList<RestaurantData> restaurantDataList = new ArrayList<>();

    private RecyclerView recyclerViewRestaurant;
    private RestaurantsAdaptor restaurantsAdaptor;

    String BASE_URL = "https://theoptimiz.com/restro/public/api/";
    String END_POINT = "get_resturants";

    private double latitude;
    private double longitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView addressTextView = view.findViewById(R.id.txtViewLocation);


        if (getArguments() != null) {
            String address = getArguments().getString("address");
            latitude = getArguments().getDouble("latitude");
            longitude = getArguments().getDouble("longitude");

            String exactAddress = extractLocationName(address) + ", " + extractCityName(address) + ", " + extractPinCode(address);

            addressTextView.setText(exactAddress);
        }

//        Here I just added a sample Category list with some images as demo

        categoryItems.add(new TopCategoryData("All", 1));
        categoryItems.add(new TopCategoryData("Pizza", R.drawable.pizza));
        categoryItems.add(new TopCategoryData("Burgar", R.drawable.burger));
        categoryItems.add(new TopCategoryData("Coco", R.drawable.softdrinks));
        categoryItems.add(new TopCategoryData("Chicken", R.drawable.chicken));
        categoryItems.add(new TopCategoryData("Rools", R.drawable.rolls));
        categoryItems.add(new TopCategoryData("Bread", R.drawable.bread));


        RecyclerView recyclerViewTopCategory = view.findViewById(R.id.topCategoryRecyclerView);
        recyclerViewTopCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        TopCategoryAdapter categoryAdaptor = new TopCategoryAdapter(categoryItems);
        recyclerViewTopCategory.setAdapter(categoryAdaptor);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String requestUrl = BASE_URL + END_POINT;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("lat", latitude);
            requestBody.put("lng", longitude);

        } catch (JSONException e) {

            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, requestBody, response -> {

            try {

                JSONArray dataArray = response.getJSONArray("data");

                if (dataArray.length() > 0) {

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject restaurantObject = dataArray.getJSONObject(i);

                        RestaurantData restaurantDetails = new RestaurantData(
                                restaurantObject.getInt("id"),
                                restaurantObject.getString("name"),
                                restaurantObject.getString("tags"),
                                restaurantObject.getDouble("rating"),
                                restaurantObject.getInt("discount"),
                                restaurantObject.getString("primary_image"),
                                restaurantObject.getDouble("distance")
                        );


                        restaurantDataList.add(restaurantDetails);

                        recyclerViewRestaurant = view.findViewById(R.id.restaurantListRecyclerView);
                        recyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                        restaurantsAdaptor = new RestaurantsAdaptor(restaurantDataList);
                        recyclerViewRestaurant.setAdapter(restaurantsAdaptor);


                    }


                } else {
                    Toast.makeText(getContext(), "Data length ", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();


            }
        }, error -> {
            Toast.makeText(getContext(), "Volley Error", Toast.LENGTH_SHORT).show();

        });

        requestQueue.add(jsonObjectRequest);

    }

    private static String extractPinCode(String inputString) {
        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static String extractCityName(String inputString) {
        String[] parts = inputString.split(",");
        if (parts.length >= 2) {
            String cityPart = parts[parts.length - 3];
            return cityPart.trim();
        }
        return "";
    }

    private static String extractLocationName(String inputString) {
        String[] parts = inputString.split(",");
        if (parts.length >= 2) {
            String locationPart = parts[1];
            return locationPart.trim();
        }
        return "";
    }

}