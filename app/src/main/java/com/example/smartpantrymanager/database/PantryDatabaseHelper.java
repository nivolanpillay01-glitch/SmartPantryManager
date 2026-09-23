package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";           // normalized
    public static final String COL_PANTRY_DISPLAY = "display_name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipes table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    // Recipe ingredients table (many-to-one with recipes)
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "name";                // normalized
    public static final String COL_RI_QTY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    public PantryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_DISPLAY + " TEXT NOT NULL, " +
                COL_PANTRY_QTY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QTY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " +
                TABLE_RECIPES + "(" + COL_RECIPE_ID + "))");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    // Inserts one recipe and its ingredient list in one call.
    private void insertRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_RECIPE_NAME, name);
        recipeValues.put(COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (Object[] ing : ingredients) {
            ContentValues riValues = new ContentValues();
            riValues.put(COL_RI_RECIPE_ID, recipeId);
            riValues.put(COL_RI_NAME, PantryItem.normalize((String) ing[0]));
            riValues.put(COL_RI_QTY, (double) (Double) ing[1]);
            riValues.put(COL_RI_UNIT, (String) ing[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, riValues);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Tomato Egg Scramble",
                "1. Whisk eggs. 2. Saute tomato until soft. 3. Add eggs, scramble until set. 4. Season with salt.",
                new Object[][]{
                        {"eggs", 3.0, "pcs"},
                        {"tomatoes", 2.0, "pcs"},
                        {"salt", 1.0, "tsp"}
                });

        insertRecipe(db, "Garlic Butter Rice",
                "1. Melt butter, fry garlic until fragrant. 2. Stir in cooked rice. 3. Season and serve.",
                new Object[][]{
                        {"rice", 2.0, "cup"},
                        {"garlic", 3.0, "pcs"},
                        {"butter", 30.0, "g"}
                });

        insertRecipe(db, "Simple Chicken Stir Fry",
                "1. Cook chicken until browned. 2. Add vegetables. 3. Add soy sauce. 4. Stir fry 5 min.",
                new Object[][]{
                        {"chicken", 300.0, "g"},
                        {"carrots", 2.0, "pcs"},
                        {"soy sauce", 2.0, "tbsp"}
                });

        insertRecipe(db, "Cheesy Pasta",
                "1. Boil pasta. 2. Melt cheese with a splash of milk. 3. Toss pasta in cheese sauce.",
                new Object[][]{
                        {"pasta", 200.0, "g"},
                        {"cheese", 100.0, "g"},
                        {"milk", 50.0, "ml"}
                });

        insertRecipe(db, "Onion Potato Hash",
                "1. Dice potato and onion. 2. Fry in oil until golden and soft. 3. Season with salt and pepper.",
                new Object[][]{
                        {"potatoes", 3.0, "pcs"},
                        {"onions", 1.0, "pcs"},
                        {"salt", 1.0, "tsp"}
                });

        insertRecipe(db, "Banana Oat Bowl",
                "1. Cook oats with milk. 2. Slice banana on top. 3. Drizzle with honey.",
                new Object[][]{
                        {"oats", 1.0, "cup"},
                        {"bananas", 1.0, "pcs"},
                        {"milk", 200.0, "ml"}
                });

        insertRecipe(db, "Tuna Sandwich",
                "1. Mix tuna with mayo. 2. Spread on bread. 3. Add lettuce, close sandwich.",
                new Object[][]{
                        {"tuna", 1.0, "can"},
                        {"bread", 2.0, "slices"},
                        {"mayonnaise", 1.0, "tbsp"}
                });

        insertRecipe(db, "Bell Pepper Omelette",
                "1. Whisk eggs. 2. Fry chopped bell pepper. 3. Add eggs, cook until set.",
                new Object[][]{
                        {"eggs", 2.0, "pcs"},
                        {"bell peppers", 1.0, "pcs"},
                        {"salt", 0.5, "tsp"}
                });

        insertRecipe(db, "Beef and Broccoli",
                "1. Sear beef strips. 2. Add broccoli, stir fry. 3. Add soy sauce, cook 5 min.",
                new Object[][]{
                        {"beef", 250.0, "g"},
                        {"broccoli", 1.0, "cup"},
                        {"soy sauce", 2.0, "tbsp"}
                });

        insertRecipe(db, "Mushroom Toast",
                "1. Fry mushrooms in butter. 2. Toast bread. 3. Pile mushrooms on toast.",
                new Object[][]{
                        {"mushrooms", 150.0, "g"},
                        {"bread", 2.0, "slices"},
                        {"butter", 15.0, "g"}
                });

        insertRecipe(db, "Simple Vegetable Soup",
                "1. Boil carrots, onion, and potato in stock. 2. Simmer until soft. 3. Season to taste.",
                new Object[][]{
                        {"carrots", 2.0, "pcs"},
                        {"onions", 1.0, "pcs"},
                        {"potatoes", 2.0, "pcs"}
                });

        insertRecipe(db, "Lemon Garlic Chicken",
                "1. Season chicken with salt. 2. Sear with garlic. 3. Squeeze lemon over before serving.",
                new Object[][]{
                        {"chicken", 300.0, "g"},
                        {"garlic", 2.0, "pcs"},
                        {"lemons", 1.0, "pcs"}
                });

        insertRecipe(db, "Yogurt Berry Parfait",
                "1. Layer yogurt in a glass. 2. Add berries. 3. Repeat layers, top with honey.",
                new Object[][]{
                        {"yogurt", 200.0, "g"},
                        {"berries", 100.0, "g"}
                });

        insertRecipe(db, "Fried Rice",
                "1. Fry egg, set aside. 2. Stir fry rice with carrot and onion. 3. Mix in egg and soy sauce.",
                new Object[][]{
                        {"rice", 2.0, "cup"},
                        {"eggs", 2.0, "pcs"},
                        {"carrots", 1.0, "pcs"},
                        {"soy sauce", 1.0, "tbsp"}
                });

        insertRecipe(db, "Avocado Toast",
                "1. Toast bread. 2. Mash avocado with salt. 3. Spread on toast.",
                new Object[][]{
                        {"bread", 2.0, "slices"},
                        {"avocados", 1.0, "pcs"},
                        {"salt", 0.5, "tsp"}
                });

        insertRecipe(db, "Spinach Cheese Wrap",
                "1. Warm tortilla. 2. Add spinach and cheese. 3. Fold and lightly toast.",
                new Object[][]{
                        {"tortillas", 1.0, "pcs"},
                        {"spinach", 50.0, "g"},
                        {"cheese", 50.0, "g"}
                });

        insertRecipe(db, "Corn Fritters",
                "1. Mix corn with flour and egg. 2. Season with salt. 3. Fry spoonfuls until golden.",
                new Object[][]{
                        {"corn", 1.0, "cup"},
                        {"eggs", 1.0, "pcs"},
                        {"flour", 0.5, "cup"}
                });

        insertRecipe(db, "Cucumber Yogurt Salad",
                "1. Slice cucumber thinly. 2. Mix with yogurt. 3. Season with salt and a squeeze of lemon.",
                new Object[][]{
                        {"cucumbers", 1.0, "pcs"},
                        {"yogurt", 100.0, "g"},
                        {"lemons", 1.0, "pcs"}
                });

        insertRecipe(db, "Zucchini Fritters",
                "1. Grate zucchini, squeeze out excess water. 2. Mix with egg and flour. 3. Fry until golden.",
                new Object[][]{
                        {"zucchinis", 1.0, "pcs"},
                        {"eggs", 1.0, "pcs"},
                        {"flour", 0.25, "cup"}
                });
    }

    // ================= PANTRY CRUD =================

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_DISPLAY, item.getDisplayName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());
        return db.insert(TABLE_PANTRY, null, values);
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_DISPLAY, item.getDisplayName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());
        return db.update(TABLE_PANTRY, values,
                COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    public int deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PANTRY,
                COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null,
                COL_PANTRY_DISPLAY + " ASC");

        while (cursor.moveToNext()) {
            items.add(cursorToPantryItem(cursor));
        }
        cursor.close();
        return items;
    }

    private PantryItem cursorToPantryItem(Cursor cursor) {
        PantryItem item = new PantryItem();
        item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_PANTRY_ID)));
        item.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_DISPLAY)));
        item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PANTRY_QTY)));
        item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT)));
        item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY)));
        return item;
    }

    // ================= RECIPE READ =================

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null,
                COL_RECIPE_NAME + " ASC");

        while (cursor.moveToNext()) {
            long recipeId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_RECIPE_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME));
            String steps = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS));

            Recipe recipe = new Recipe();
            recipe.setId(recipeId);
            recipe.setName(name);
            recipe.setSteps(steps);
            recipe.setRequiredIngredients(getIngredientsForRecipe(db, recipeId));
            recipes.add(recipe);
        }
        cursor.close();
        return recipes;
    }

    private List<Recipe.RecipeIngredient> getIngredientsForRecipe(SQLiteDatabase db, long recipeId) {
        List<Recipe.RecipeIngredient> ingredients = new ArrayList<>();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COL_RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RI_NAME));
            double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_RI_QTY));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow(COL_RI_UNIT));
            ingredients.add(new Recipe.RecipeIngredient(name, qty, unit));
        }
        cursor.close();
        return ingredients;
    }

    // ================= STRICT-MATCHING LOGIC (Section 2.3) =================

    // Returns only the recipes for which every required ingredient is present
    // in the pantry in at least the required quantity. This is the core
    // business rule of the app.
    public List<Recipe> getSuggestedRecipes() {
        List<PantryItem> pantryItems = getAllPantryItems();
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> suggested = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (recipeIsFullyMatched(recipe, pantryItems)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    // Optional "Almost There" list: recipes missing exactly one ingredient.
    public List<Recipe> getAlmostThereRecipes() {
        List<PantryItem> pantryItems = getAllPantryItems();
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> almostThere = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            int missingCount = countMissingIngredients(recipe, pantryItems);
            if (missingCount == 1) {
                almostThere.add(recipe);
            }
        }
        return almostThere;
    }

    private boolean recipeIsFullyMatched(Recipe recipe, List<PantryItem> pantryItems) {
        return countMissingIngredients(recipe, pantryItems) == 0;
    }

    private int countMissingIngredients(Recipe recipe, List<PantryItem> pantryItems) {
        int missing = 0;
        for (Recipe.RecipeIngredient required : recipe.getRequiredIngredients()) {
            if (!pantryHasEnough(required, pantryItems)) {
                missing++;
            }
        }
        return missing;
    }

    private boolean pantryHasEnough(Recipe.RecipeIngredient required, List<PantryItem> pantryItems) {
        for (PantryItem pantryItem : pantryItems) {
            if (pantryItem.getName().equals(required.getName())) {
                return pantryItem.getQuantity() >= required.getQuantity();
            }
        }
        return false; // ingredient not in pantry at all
    }
}