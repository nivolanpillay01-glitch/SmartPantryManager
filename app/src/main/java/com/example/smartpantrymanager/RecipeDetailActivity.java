package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView textRecipeName = findViewById(R.id.textRecipeName);
        TextView textIngredients = findViewById(R.id.textIngredients);
        TextView textSteps = findViewById(R.id.textSteps);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);

        PantryDatabaseHelper dbHelper = new PantryDatabaseHelper(this);
        List<Recipe> allRecipes = dbHelper.getAllRecipes();

        Recipe recipe = null;
        for (Recipe r : allRecipes) {
            if (r.getId() == recipeId) {
                recipe = r;
                break;
            }
        }

        if (recipe != null) {
            textRecipeName.setText(recipe.getName());

            StringBuilder ingredientsText = new StringBuilder();
            for (Recipe.RecipeIngredient ing : recipe.getRequiredIngredients()) {
                ingredientsText.append("- ")
                        .append(formatQuantity(ing.getQuantity()))
                        .append(" ")
                        .append(ing.getUnit() != null ? ing.getUnit() : "")
                        .append(" ")
                        .append(ing.getName())
                        .append("\n");
            }
            textIngredients.setText(ingredientsText.toString().trim());
            textSteps.setText(recipe.getSteps());
        }
    }

    private String formatQuantity(double quantity) {
        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}