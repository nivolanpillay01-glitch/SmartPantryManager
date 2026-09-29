package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapters.RecipeAdapter;
import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity
        implements RecipeAdapter.OnRecipeClickListener {

    private PantryDatabaseHelper dbHelper;
    private RecyclerView recyclerRecipes;
    private TextView textEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new PantryDatabaseHelper(this);

        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        textEmptyState = findViewById(R.id.textEmptyState);
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes(); // re-run strict-matching every time pantry may have changed
    }

    private void loadSuggestedRecipes() {
        // This is the core business rule (Section 2.3): only recipes where
        // every required ingredient is present in the pantry, in sufficient
        // quantity, are shown here.
        List<Recipe> suggested = dbHelper.getSuggestedRecipes();

        if (suggested.isEmpty()) {
            recyclerRecipes.setVisibility(View.GONE);
            textEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerRecipes.setVisibility(View.VISIBLE);
            textEmptyState.setVisibility(View.GONE);
        }

        RecipeAdapter adapter = new RecipeAdapter(suggested, this);
        recyclerRecipes.setAdapter(adapter);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }
}