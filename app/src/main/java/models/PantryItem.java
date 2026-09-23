package com.example.smartpantrymanager.models;

public class PantryItem {

    private long id;
    private String name;          // normalized, lowercase, singular
    private String displayName;   // original name as typed by user
    private double quantity;
    private String unit;          // e.g. "g", "ml", "pcs"
    private String expiryDate;    // stored as "yyyy-MM-dd", optional

    public PantryItem() {
    }

    public PantryItem(String displayName, double quantity, String unit, String expiryDate) {
        this.displayName = displayName;
        this.name = normalize(displayName);
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Normalizes an ingredient name for matching: lowercase, trimmed,
    // and naive singularization (removes a trailing "es" or "s").
    public static String normalize(String rawName) {
        if (rawName == null) return "";
        String cleaned = rawName.trim().toLowerCase();
        if (cleaned.endsWith("es")) {
            cleaned = cleaned.substring(0, cleaned.length() - 2);
        } else if (cleaned.endsWith("s") && !cleaned.endsWith("ss")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }
        return cleaned;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.name = normalize(displayName);
    }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}