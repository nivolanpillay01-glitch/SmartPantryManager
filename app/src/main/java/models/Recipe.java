package com.example.smartpantrymanager.models;

import java.util.List;

public class Recipe {

    private long id;
    private String name;
    private List<RecipeIngredient> requiredIngredients;
    private String steps; // simple multi-line preparation instructions

    public Recipe() {
    }

    public Recipe(String name, List<RecipeIngredient> requiredIngredients, String steps) {
        this.name = name;
        this.requiredIngredients = requiredIngredients;
        this.steps = steps;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<RecipeIngredient> getRequiredIngredients() { return requiredIngredients; }
    public void setRequiredIngredients(List<RecipeIngredient> requiredIngredients) {
        this.requiredIngredients = requiredIngredients;
    }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    // Inner class representing one line of a recipe's ingredient list
    public static class RecipeIngredient {
        private String name;       // normalized, lowercase, singular
        private double quantity;
        private String unit;

        public RecipeIngredient() {
        }

        public RecipeIngredient(String rawName, double quantity, String unit) {
            this.name = PantryItem.normalize(rawName);
            this.quantity = quantity;
            this.unit = unit;
        }

        public String getName() { return name; }
        public double getQuantity() { return quantity; }
        public String getUnit() { return unit; }
    }
}