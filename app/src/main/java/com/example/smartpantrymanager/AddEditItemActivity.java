package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.PantryDatabaseHelper;
import com.example.smartpantrymanager.models.PantryItem;

import java.util.List;

public class AddEditItemActivity extends AppCompatActivity {

    private PantryDatabaseHelper dbHelper;
    private EditText editName, editQuantity, editUnit, editExpiry;
    private TextView textError;

    private long editingItemId = -1; // -1 means we're adding a new item
    private PantryItem existingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        dbHelper = new PantryDatabaseHelper(this);

        TextView textFormTitle = findViewById(R.id.textFormTitle);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);
        textError = findViewById(R.id.textError);
        Button buttonSave = findViewById(R.id.buttonSave);

        editingItemId = getIntent().getLongExtra("item_id", -1);

        if (editingItemId != -1) {
            textFormTitle.setText("Edit Ingredient");
            loadExistingItem(editingItemId);
        }

        buttonSave.setOnClickListener(v -> saveItem());
    }

    private void loadExistingItem(long id) {
        List<PantryItem> allItems = dbHelper.getAllPantryItems();
        for (PantryItem item : allItems) {
            if (item.getId() == id) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            editName.setText(existingItem.getDisplayName());
            editQuantity.setText(String.valueOf(existingItem.getQuantity()));
            editUnit.setText(existingItem.getUnit());
            editExpiry.setText(existingItem.getExpiryDate());
        }
    }

    private void saveItem() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        // ---- Input validation (Section 3.1 requirement) ----
        if (name.isEmpty()) {
            showError("Please enter an ingredient name.");
            return;
        }
        if (quantityStr.isEmpty()) {
            showError("Please enter a quantity.");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid number.");
            return;
        }

        if (quantity <= 0) {
            showError("Quantity must be greater than zero.");
            return;
        }

        textError.setVisibility(android.view.View.GONE);

        PantryItem item = (existingItem != null) ? existingItem : new PantryItem();
        item.setDisplayName(name);
        item.setQuantity(quantity);
        item.setUnit(unit.isEmpty() ? null : unit);
        item.setExpiryDate(expiry.isEmpty() ? null : expiry);

        if (existingItem != null) {
            dbHelper.updatePantryItem(item);
        } else {
            dbHelper.addPantryItem(item);
        }

        finish(); // return to Pantry List, which refreshes in onResume()
    }

    private void showError(String message) {
        textError.setText(message);
        textError.setVisibility(android.view.View.VISIBLE);
    }
}