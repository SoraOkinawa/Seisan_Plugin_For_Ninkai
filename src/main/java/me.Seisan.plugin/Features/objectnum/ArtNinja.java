package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;
import org.bukkit.Art;

import java.util.ArrayList;


public class ArtNinja {
    public static ArrayList<ArtNinja> allNinjaArts = new ArrayList<>();
    
//    NULL("Null", 0, "ERROR_404", ""),
//    FUINJUTSU("Fuinjutsu", 1, "Fuinjutsu", "4E5587"),
//    GENJUTSU("Genjutsu", 2, "Genjutsu", "9843C6"),
//    NINJUTSU("Ninjutsu", 3, "Ninjutsu", "A52F1F"),
//    IRYOJUTSU("Iryojutsu", 4, "Iryojutsu", "72C17A"),
//    KARI("Kari Sasori", 5, "Kari_Sasori", "D3D3D3"),
//    KARATE("Karaté aquatique", 6, "Karate_aquatique", "D3D3D3"),
//    SHORINJI("Shorinji Kempo", 7, "Shorijin_Kempo", "D3D3D3"),
//    JUKEN("Juken", 8, "Juken", "D3D3D3"),
//    PORTES("Portes & respirations célestes", 9, "Portes_&_respirations_celestes", "D3D3D3"),
//    EPEISTES("Épéistes de la brume", 10, "Epeistes_de_la_brume", "D3D3D3"),
//    SHUGUREJUTSU("Shugurejutsu", 11, "Shugurejutsu", "D3D3D3"),
//    MULTICLONAGE("Multiclonage", 12, "Multiclonage", "D3D3D3"),
//    KATA("Kata Ichi Ryu", 13, "Kata_Ichi_Ryu", "D3D3D3"),
//    NODO("Nodo no Oyayubi", 14, "Nodo_no_Oyayubi", "D3D3D3"),
//    TESSEN("Tessen", 15, "Tessen", "D3D3D3"),
//    KYUJUTSU("Kyujutsu", 16, "Kyujutsu", "D3D3D3"),
//    BOJIEI("Bô Jiei", 17, "Bo_Jiei", "D3D3D3"),
//    SHURYO("Shuryo Sasori", 18, "Shuryo_Sasori", "D3D3D3"),
//    TECHDELOMBRE("50 techniques de l'ombre", 19, "50_techniques_de_l_ombre", "D3D3D3"),
//    GRIFFE("Griffe du loup blanc", 20, "Griffe_du_loup_blanc", "D3D3D3"),
//    HANKAGE("Han Kage", 21, "Han_Kage", "D3D3D3"),
//    HANGU("Hangu To Hugo", 22, "Hangu_To_Hugo", "D3D3D3"),
//    IJITSU("Ijitsu", 23, "Ijitsu", "D3D3D3"),
//    YASEIKEN("Yaseiken", 24, "Yaseiken", "D3D3D3"),
//    OMOIHIDAN("Omoi Hidan", 25, "Omoi_Hidan", "D3D3D3"),
//    INDEFINI("Non défini", -1, "NO_DEFINE", "D3D3D3");

    @Getter
    private final String name;
    @Getter
    private final int id;
    @Getter
    private final String tag;
    @Getter
    private final String identifiant;
    @Getter
    private String colorHexa;

    public ArtNinja(String name, int id, String tag, String identifiant, String colorHexa){
        this.name = name;
        this.id = id;
        this.tag = tag;
        this.identifiant = identifiant;
        this.colorHexa = colorHexa;
        
        allNinjaArts.add(this);
    }

    public static ArtNinja getFromID(int id){
        for(ArtNinja voieNinja : allNinjaArts){
            if(voieNinja.getId() == id){
                return voieNinja;
            }
        }
        return getFromID(1);
    }

    public static int getIDFromName(String name) {
        for(ArtNinja voieNinja : allNinjaArts){
            if(voieNinja.getName().equals(name)){
                return voieNinja.getId();
            }
        }
        return -2;
    }

    public static ArtNinja getFromIdentifiant(String identifiant) {
        for(ArtNinja voieNinja : allNinjaArts){
            if(voieNinja.getIdentifiant().equals(identifiant)){
                return voieNinja;
            }
        }
        return getFromID(1);
    }

    public static ArtNinja getFromName(String name) {
        for(ArtNinja voieNinja : allNinjaArts){
            if(voieNinja.getName().equals(name)){
                return voieNinja;
            }
        }
        return getFromID(1);
    }
    
    public boolean isVoieNinja() {
        return this.equals(getFromIdentifiant("ninjutsu")) ||
                this.equals(getFromIdentifiant("genjutsu")) ||
                this.equals(getFromIdentifiant("iryoujutsu")) ||
                this.equals(getFromIdentifiant("houjutsu"));
    }
}
