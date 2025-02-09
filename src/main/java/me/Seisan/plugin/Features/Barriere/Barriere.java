package me.Seisan.plugin.Features.Barriere;

import lombok.Getter;
import lombok.Setter;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.skill.SkillLevel;

import java.util.ArrayList;
import java.util.List;

public class Barriere {

    // Barriere's properties :
    // - plugin-name
    // - Name
    // - Description
    // - Price
    // - IsPriceMultiplier
    // - Category
    // - isUniqueInCategory
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
    private boolean isUniqueInCategory;

    private boolean isDefault;

    private boolean isInvisibleWhenDefault;

    private int prepareTime;

    private String rank;

    private boolean isSecret;

    private int level;

    public static ArrayList<Barriere> instanceList = new ArrayList<>();

    public Barriere(String nameInPlugin, String name, String description, float price, boolean isPriceMultiplier, String category, boolean isUniqueInCategory, boolean isDefault, boolean isInvisibleWhenDefault, int prepareTime, String rank, boolean isSecret, int level) {
        this.nameInPlugin = nameInPlugin;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isPriceMultiplier = isPriceMultiplier;
        this.category = category;
        this.isUniqueInCategory = isUniqueInCategory;
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

    public boolean isUniqueInCategory() {
        return isUniqueInCategory;
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
    public static Barriere getDefaultBarriereByCategory(String category, List<Barriere> barrieres) {
        for (Barriere barriere : barrieres) {
            if (barriere.getCategory().equalsIgnoreCase(category) && barriere.isDefault()) {
                return barriere;
            }
        }
        return null;
    }

    //get a barrier by its name from a list
    public static Barriere getBarriereByNameInPlugin(String name, List<Barriere> barriere) {
        for (Barriere b : barriere) {
            if (b.getNameInPlugin().equals(name)) {
                return b;
            }
        }
        return null;
    }

    // calculate max preparation time
    public static int getMaxPrepareTime(List<Barriere> barriere) {
        int maxPrepareTime = 0;
        for (Barriere b : barriere) {
            if (b.getPrepareTime() > maxPrepareTime) {
                maxPrepareTime = b.getPrepareTime();
            }
        }
        return maxPrepareTime;
    }

    // calculate cost of the barriere
    public static int getBarriereCost(List<Barriere> barriere) {
        float cost = 0;
        float multiplier = 1;
        for (Barriere b : barriere) {
            if (b.isPriceMultiplier()) {
                multiplier = b.getPrice();
            } else {
                cost += b.getPrice();
            }
        }
        cost *= multiplier;

        return Math.round(cost);
    }

    // get all catergorie of barrieres in the player's barriere list
    public static List<String> getBarriereCategories(List<Barriere> barriere) {
        List<String> categories = new ArrayList<>();
        for (Barriere b : barriere) {
            if (!categories.contains(b.getCategory())) {
                categories.add(b.getCategory());
            }
        }
        return categories;
    }

    // get all barriere of a category in the player's barriere list
    public static List<Barriere> getBarriereByCategory(List<Barriere> barriere, String category) {
        List<Barriere> barriereList = new ArrayList<>();
        for (Barriere b : barriere) {
            if (b.getCategory().equals(category)) {
                barriereList.add(b);
            }
        }
        return barriereList;
    }

    // get the default barriere in one category in the player's barriere list
    public static Barriere getDefaultBarriere(List<Barriere> barriere, String category) {
        for (Barriere b : barriere) {
            if (b.getCategory().equals(category) && b.isDefault()) {
                return b;
            }
        }
        return null;
    }

    public static ArrayList<Barriere> getDefaultBarrieres(List<Barriere> barrieres) {
        ArrayList<Barriere> defaultBarrieres = new ArrayList<>();
        for (Barriere barriere : barrieres) {
            if (barriere.isDefault()) {
                defaultBarrieres.add(barriere);
            }
        }
        return defaultBarrieres;
    }

    //get max rank of a barriere
    public static SkillLevel getMaxRank(List<Barriere> barriere) {
        SkillLevel maxRank = SkillLevel.NULL;
        for (Barriere b : barriere) {
            if (SkillLevel.getByCharName(b.getRank()).getLevelOrder() > maxRank.getLevelOrder()) {
                maxRank = SkillLevel.getByCharName(b.getRank());
            }
        }
        return maxRank;
    }

}
