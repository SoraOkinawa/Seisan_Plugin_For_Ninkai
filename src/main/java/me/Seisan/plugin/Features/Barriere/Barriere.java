package me.Seisan.plugin.Features.Barriere;

import lombok.Getter;
import lombok.Setter;
import me.Seisan.plugin.Features.ability.Ability;

import java.util.ArrayList;

public class Barriere {

    // Barriere's properties :
    // - plugin-name
    // - Name
    // - Description
    // - Price
    // - IsPriceMultiplier
    // - Category
    // - Default
    // - PrepareTime
    // - Rank
    // - Secret
    // - Level
    // - Invisible when default



    private String nameInPlugin;

    private String name;

    private String description;

    private float price;

    private boolean isPriceMultiplier;

    private String category;

    private boolean isDefault;

    private boolean isInvisibleWhenDefault;

    private int prepareTime;

    private String rank;

    private boolean isSecret;

    private int level;

    public static ArrayList<Barriere> instanceList = new ArrayList<>();

    public Barriere(String nameInPlugin, String name, String description, float price, boolean isPriceMultiplier, String category, boolean isDefault, boolean isInvisibleWhenDefault, int prepareTime, String rank, boolean isSecret, int level) {
        this.nameInPlugin = name;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isPriceMultiplier = isPriceMultiplier;
        this.category = category;
        this.isDefault = isDefault;
        this.isInvisibleWhenDefault = isInvisibleWhenDefault;
        this.prepareTime = prepareTime;
        this.rank = rank;
        this.isSecret = isSecret;
        this.level = level;
        instanceList.add(this);
    }

    // Getters and Setters

    public String getNameInPlugin() {
        return nameInPlugin;
    }

    public void setNameInPlugin(String nameInPlugin) {
        this.nameInPlugin = nameInPlugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isPriceMultiplier() {
        return isPriceMultiplier;
    }

    public void setPriceMultiplier(boolean priceMultiplier) {
        isPriceMultiplier = priceMultiplier;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isInvisibleWhenDefault() {
        return isInvisibleWhenDefault;
    }

    public void setInvisibleWhenDefault(boolean isInvisibleWhenDefault) {
        this.isInvisibleWhenDefault = isInvisibleWhenDefault;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }


    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static ArrayList<Barriere> getInstanceList() {
        return instanceList;
    }

    public static void setInstanceList(ArrayList<Barriere> instanceList) {
        Barriere.instanceList = instanceList;
    }


    // get all default barrieres
    public static ArrayList<Barriere> getDefaultBarrieres() {
        ArrayList<Barriere> defaultBarrieres = new ArrayList<>();
        for (Barriere barriere : instanceList) {
            if (barriere.isDefault()) {
                defaultBarrieres.add(barriere);
            }
        }
        return defaultBarrieres;
    }

    //get default barriere of a category
    public static Barriere getDefaultBarriereByCategory(String category) {
        for (Barriere barriere : instanceList) {
            if (barriere.getCategory().equalsIgnoreCase(category) && barriere.isDefault()) {
                return barriere;
            }
        }
        return null;
    }
    public static Barriere getBarriereByName(String name) {
        for (Barriere barriere : instanceList) {
            if (barriere.getName().equalsIgnoreCase(name)) {
                return barriere;
            }
        }
        return null;
    }

    public static Barriere getBarriereByNameInPlugin(String name) {
        for (Barriere barriere : instanceList) {
            if (barriere.getNameInPlugin().equalsIgnoreCase(name)) {
                return barriere;
            }
        }
        return null;
    }

    public static ArrayList<Barriere> getBarriereByCategory(String category) {
        ArrayList<Barriere> barriereList = new ArrayList<>();
        for (Barriere barriere : instanceList) {
            if (barriere.getCategory().equalsIgnoreCase(category)) {
                barriereList.add(barriere);
            }
        }
        return barriereList;
    }

    public static ArrayList<Barriere> getBarriereByRank(String rank) {
        ArrayList<Barriere> barriereList = new ArrayList<>();
        for (Barriere barriere : instanceList) {
            if (barriere.getRank().equalsIgnoreCase(rank)) {
                barriereList.add(barriere);
            }
        }
        return barriereList;
    }

    public static ArrayList<Barriere> getBarriereByLevel(int level) {
        ArrayList<Barriere> barriereList = new ArrayList<>();
        for (Barriere barriere : instanceList) {
            if (barriere.getLevel() == level) {
                barriereList.add(barriere);
            }
        }
        return barriereList;
    }


}
