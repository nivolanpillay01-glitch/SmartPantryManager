# Smart Pantry Manager

An Android application, built in Java, that helps reduce food waste by tracking the ingredients a user has at home and suggesting recipes they can cook **strictly** from what's already in their pantry — no recipe is ever suggested unless every single ingredient it needs is present in sufficient quantity.

## Features

- **Pantry management** — add, edit, and delete pantry items (name, quantity, unit, optional expiry date)
- **Pantry List screen** — all current ingredients displayed in a RecyclerView bound to the database, with edit and delete actions
- **Recipe collection** — 19 recipes pre-seeded into the database on first run, each with a name, required ingredients, and preparation steps
- **Suggested Recipes screen** — runs the strict-matching rule against the current pantry and lists only recipes the user can make right now
- **Recipe Detail screen** — full ingredient list and method for a selected recipe
- **Settings screen** — toggle for expiring-soon alerts and a metric units preference, persisted with SharedPreferences
- **Input validation** on the Add/Edit form, and a clear empty-state message when no recipes match the pantry

## The Strict-Matching Rule

A recipe is only shown as "suggested" if every ingredient it requires is present in the pantry in at least the required quantity — a single missing or insufficient ingredient excludes the whole recipe from the list. To keep this robust against small real-world differences (e.g. "tomato" vs "tomatoes"), every ingredient name is normalized (lowercased and naively singularized) before it is stored or compared, in both the pantry and the recipe data.

## Database

This project uses **SQLite**, accessed through a custom `SQLiteOpenHelper` subclass (`PantryDatabaseHelper`), implemented entirely on-device.

**Why SQLite:** the app's data — a user's own pantry and a fixed set of seeded recipes — is inherently local and single-user, with no need for real-time sync across devices or a backend server. SQLite avoids the extra setup and moving parts of a cloud database (Firebase) or a self-hosted REST API (PostgreSQL), while still giving full CRUD support and guaranteed persistence between app sessions, which is all this app requires.

Three tables are used:
- `pantry_items` — the user's own ingredients
- `recipes` — the seeded recipe collection (name, preparation steps)
- `recipe_ingredients` — each recipe's required ingredients (many-to-one with `recipes`)

## Tech Stack

- **Language:** Java
- **IDE:** Android Studio
- **Min SDK:** API 24 (Android 7.0)
- **UI:** XML layouts with ConstraintLayout, RecyclerView + custom Adapters, CardView
- **Persistence:** SQLite (SQLiteOpenHelper) for app data, SharedPreferences for settings

## Project Structure

```
app/src/main/java/com/example/smartpantrymanager/
├── activities (PantryListActivity, AddEditItemActivity,
│               SuggestedRecipesActivity, RecipeDetailActivity,
│               SettingsActivity)
├── adapters   (PantryAdapter, RecipeAdapter)
├── database   (PantryDatabaseHelper)
└── models     (PantryItem, Recipe)
```

## Setup / Run Instructions

1. Clone this repository:
   ```
 
   ```
2. Open the project folder in **Android Studio** (Quail 4 or later recommended).
3. Let Gradle sync complete (this may take a few minutes on first open).
4. Create or select an Android Virtual Device with **API 24 or higher** (this project was built and tested on a Pixel 6, API 34) via **Tools → Device Manager**.
5. Click **Run ▶** with the emulator selected. The app launches directly into the Pantry List screen.
6. No further configuration, API keys, or backend setup is required — the database is created and seeded automatically on first launch.


