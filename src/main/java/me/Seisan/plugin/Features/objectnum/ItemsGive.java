package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;
import org.bukkit.Material;

public enum ItemsGive {

    CUBE_TERRE("§6Cube de terre" , "§cHRP : §7Superbe pour représenter de la terre.", 64, Material.DIRT, "seisan", "seisan"),
    CUBE_PIERRE("§6Cube de pierre","§cHRP : §7Magnifique pour représenter de la pierre.", 64, Material.STONE, "seisan", "seisan"),
    PIOCHE_FER("§6Pioche en fer","§cHRP : §7Retire les jutsus de pierre.", 1, Material.IRON_PICKAXE, "seisan", "seisan"),
    CISAILLES("§6Cisailles","§cHRP : §7Époustouflant pour retirer les laines.", 1, Material.SHEARS, "seisan", "seisan"),
    LAINE_BLANCHE("§6Laine blanche","§cHRP : §7Génial pour représenter certains jutsus.", 64, Material.WHITE_WOOL, "seisan", "seisan"),
    LAINE_BRUNE("§6Laine marron", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.BROWN_WOOL, "seisan", "seisan"),
    LAINE_ROUGE("§6Laine rouge", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.RED_WOOL, "seisan", "seisan"),
    LAINE_VERTE("§6Laine verte", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.GREEN_WOOL, "seisan", "seisan"),
    LAINE_JAUNE("§6Laine jaune", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.YELLOW_WOOL, "seisan", "seisan"),
    LAINE_NOIR("§6Laine noire", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.BLACK_WOOL, "seisan", "seisan"),
    LAINE_GRISE("§6Laine grise", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.GRAY_WOOL, "seisan", "seisan"),
    LAINE_ROSE("§6Laine rose", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.PINK_WOOL, "seisan", "seisan"),
    LAINE_BLEU("§6Laine bleu", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.BLUE_WOOL, "seisan", "seisan"),
    LAINE_CYAN("§6Laine cyan", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.CYAN_WOOL, "seisan", "seisan"),
    TAPIS_GRIS("§6Laine grise", "§cHRP : §7Intéressant pour représenter certains jutsus.", 64, Material.GRAY_CARPET, "seisan", "seisan"),
    ITEM_FRAME("§6Cadre","§cHRP : §7Utile pour placer des items n'importe où.", 16, Material.ITEM_FRAME, "seisan", "seisan"),
    ITEM_FRAME_INVISIBLE("§6Cadre","§cHRP : §7Discret pour placer des items n'importe où.", 16, Material.ITEM_FRAME, "EntityTag", "{Invisible:1}"),
    PANNEAU("§6Panneau","§cHRP : §7Sympatique pour écrire avec ces panneaux.", 16, Material.OAK_SIGN, "seisan", "seisan"),
    PANNEAU_HRP("§6Panneau HRP","§cHRP : §7Sympatique pour décrire un lieu.", 16, Material.WARPED_SIGN, "seisan", "seisan"),
    PAPIER("%name%","§cHRP : §7Formidable pour lock des conteneurs à votre nom.", 16, Material.TRIPWIRE_HOOK, "seisan", "seisan"),
    BONE_MEAL("§6Poudre d'os","§cHRP : §7Agréable pour le clan Senju.", 64, Material.BONE_MEAL, "seisan", "seisan"),
    SABLE("§6Sable","§cHRP : §7Nécessaire pour le clan §6Sabaku§7.", 64, Material.SAND, "seisan", "seisan"),
    SABLE_OR("§6Sable d'or","§cHRP : §7Nécessaire pour le clan §6Sabaku§7.", 64, Material.YELLOW_CONCRETE_POWDER, "seisan", "seisan"),
    SABLE_ARGENT("§6Limaille d'argent","§cHRP : §7Nécessaire pour le clan §6Sabaku§7.", 64, Material.GRAY_CONCRETE_POWDER, "seisan", "seisan"),
    SABLE_FER("§6Limaille de fer","§cHRP : §7Nécessaire pour le clan §6Sabaku§7.", 64, Material.BLACK_CONCRETE_POWDER, "seisan", "seisan"),
    MANNEQUIN("§6Mannequin","§cHRP : §7Pratique pour le clan §6Bunraku§7.", 1, Material.ARMOR_STAND, "seisan", "seisan"),
    KATANA_OS("§6Katana en os","§cHRP : §7Exclusivement pour le clan §fKaguya§7.", 1, Material.IRON_SWORD, "seisan", "katana_os");


    @Getter
    private String name;
    @Getter
    private String lore;
    @Getter
    private int amount;
    @Getter
    private Material material;
    @Getter
    private String keytag;
    @Getter
    private String valuetag;

    ItemsGive(String name, String lore, int amount, Material material, String keytag, String valuetag){
        this.name = name;
        this.lore = lore;
        this.amount = amount;
        this.material = material;
        this.keytag = keytag;
        this.valuetag = valuetag;
    }

}
