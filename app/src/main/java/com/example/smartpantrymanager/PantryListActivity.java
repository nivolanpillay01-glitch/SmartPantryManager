package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapters.PantryAdapter;
import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.models.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PantryListActivity extends AppCompatActivity
        implements PantryAdapter.OnItemActionListener {

    private PantryDatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryItems;

    private RecyclerView recyclerPantry;
    private TextView textEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        dbHelper = new PantryDatabaseHelper(this);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        textEmptyState = findViewById(R.id.textEmptyState);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);

        Button buttonViewRecipes = findViewById(R.id.buttonViewRecipes);
        buttonViewRecipes.setOnClickListener(v ->
                startActivity(new Intent(PantryListActivity.this, SuggestedRecipesActivity.class)));

        ImageButton buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(v ->
                startActivity(new Intent(PantryListActivity.this, SettingsActivity.class)));

        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));

        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(PantryListActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // refresh every time we return to this screen
    }

    private void loadPantryItems() {
        pantryItems = dbHelper.getAllPantryItems();

        if (pantryItems.isEmpty()) {
            recyclerPantry.setVisibility(View.GONE);
            textEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerPantry.setVisibility(View.VISIBLE);
            textEmptyState.setVisibility(View.GONE);
        }

        adapter = new PantryAdapter(pantryItems, this);
        recyclerPantry.setAdapter(adapter);
    }

    @Override
    public void onEditClicked(PantryItem item) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra("item_id", item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        dbHelper.deletePantryItem(item.getId());
        Toast.makeText(this, item.getDisplayName() + " removed", Toast.LENGTH_SHORT).show();
        loadPantryItems();
    }
}