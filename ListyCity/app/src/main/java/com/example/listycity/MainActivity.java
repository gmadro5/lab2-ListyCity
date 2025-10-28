package com.example.listycity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;
    Button addButton;
    Button deleteButton;
    Button confirmButton;
    LinearLayout addCityContainer;
    TextInputEditText editCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ListView and Button
        cityList = findViewById(R.id.city_list);
        addButton = findViewById(R.id.add_button);
        deleteButton = findViewById(R.id.delete_button);
        confirmButton = findViewById(R.id.confirm_button);
        addCityContainer = findViewById(R.id.add_city_container);
        editCity = findViewById(R.id.edit_city_name);

        String []cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"};
        final int[] selectedIndex = {-1}; // track the selected index

        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));

        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

        // Add City button click
        // v -> { ... } is short for writing a whole anonymous class that implements the OnClickListener interface
        addButton.setOnClickListener(v -> {
            // Show the bottom input bar when "Add City" button is clicked
            if (addCityContainer.getVisibility() == View.GONE) {
                addCityContainer.setVisibility(View.VISIBLE);
                addCityContainer.setTranslationY(addCityContainer.getHeight());
                addCityContainer.animate().translationY(0).setDuration(250).start();
            }
        });

        // Delete City button click
        deleteButton.setOnClickListener( v -> {
            if (selectedIndex[0] != -1) {
                cityList.setItemChecked(selectedIndex[0], false); // uncheck first
                dataList.remove(selectedIndex[0]); // remove by index
                cityAdapter.notifyDataSetChanged();

                /* Supposed to clear the highlight after deletion, but doesn't work because I couldn't
                add the TextView inside a LinearLayout in content.xml without creating a content class
                */
                cityList.clearChoices();  // fully clears the highlight
                cityList.requestLayout(); // refresh the layout
                selectedIndex[0] = -1; // reset selected index
            } else {
                Toast.makeText(this, "Select a city to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Confirm button click
        confirmButton.setOnClickListener(v -> {
            String cityName = editCity.getText().toString().trim();

            // if cityName is not empty... else...
            if (!cityName.isEmpty()) {
                dataList.add(cityName);
                cityAdapter.notifyDataSetChanged();
                editCity.setText(""); // clear input

                // Hide with animation
                addCityContainer.animate()
                        .translationY(addCityContainer.getHeight())
                        .setDuration(250)
                        .withEndAction(() -> addCityContainer.setVisibility(View.GONE))
                        .start();
            } else {
                editCity.setError("Please enter a city");
            }
        });

        // When a city is clicked in the list
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedIndex[0] = position;
            cityList.setItemChecked(position, true); // highlights the clicked item
        });
    }
}