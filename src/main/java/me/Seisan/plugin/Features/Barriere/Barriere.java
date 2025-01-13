package me.Seisan.plugin.Features.Barriere;

public class Barriere {

    // Barriere's properties :
    // - Name
    // - Description
    // - Price
    // - IsPriceMultiplier
    // - Category
    // - Default
    // - PrepareTime
    // - Rank

    private String name;
    private String description;
    private int price;
    private boolean isPriceMultiplier;
    private String category;
    private boolean isDefault;
    private int prepareTime;

    private String rank;

    public Barriere(String name, String description, int price, boolean isPriceMultiplier, String category, boolean isDefault, int prepareTime, String rank) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isPriceMultiplier = isPriceMultiplier;
        this.category = category;
        this.isDefault = isDefault;
        this.prepareTime = prepareTime;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPriceMultiplier() {
        return isPriceMultiplier;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public String getRank() {
        return rank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIsPriceMultiplier(boolean isPriceMultiplier) {
        this.isPriceMultiplier = isPriceMultiplier;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
