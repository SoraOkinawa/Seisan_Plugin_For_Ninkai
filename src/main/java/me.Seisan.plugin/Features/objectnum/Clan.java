package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;


public enum Clan {

    NULL("Null", 0, "null", "NULL",""),
    UCHIWA("Uchiwa", 1, "uchiwa_icon", "Uchiwa","5B3E78"),
    SENJU("Senju", 2, "senju_icon", "Senju","218326"),
    HYUGA("Hyûga", 3, "hyuga_icon", "Hyuga","B5BEF3"),
    ABURAME("Aburame", 4, "aburame_icon", "Aburame","243F27"),
    INUZUKA("Inuzuka", 5, "inuzuka_icon", "Inuzuka","6E1D0D"),
    KAMIZURU("Kamizuru", 6, "kamizuru_icon", "Kamizuru","FFB200"),
    HIKAI("Hikai", 7, "hikai_icon", "Hikai","B75423"),
    HOZUKI("Hozuki", 8, "hozuki_icon", "Hozuki","7EB7D6"),
    HOSHIGAKI("Hoshigaki", 9, "hoshigaki_icon", "Hoshigaki","1A245C"),
    KAGUYA("Kaguya", 10, "kaguya_icon", "Kaguya","E1D6C0"),
    DENKI("Denki", 11, "denki_icon", "Denki","676767"),
    CHINOIKE("Chinoike", 12, "chinoike_icon", "Chinoike","660000"),
    BUNRAKU("Bunraku", 13, "bunraku_icon", "Bunraku","81593E"),
    SABAKU("Sabaku", 14, "sabaku_icon", "Sabaku","F5B075"),
    KAMI("Kami", 15, "kami_icon", "Kami","A898C6"),
    INSHUSUMIE("Ninshu Sumie", 16, "ninshu_icon", "Ninshu_Sumie","F4F4F4"),
    NINSHUYOSUGA("Ninshu Yosuga", 17, "ninshu_icon", "Ninsu_Yosuga","919191"),
    ERMITECRAPAUD("Ermite Crapaud", 18, "ermite_icon", "Ermite_Crapaud","63D163"),
    ERMITESERPENT("Ermite Serpent", 19, "ermite_icon", "Ermite_Serpent","63D163"),
    ERMITELIMACE("Ermite Limace", 20, "ermite_icon", "Ermite_Limace","63D163"),
    ERMITELOUP("Ermite Loup", 21, "ermite_icon", "Ermite_Loup","63D163"),
    ERMITEPOULPE("Ermite Poulpe", 22, "ermite_icon", "Ermite_Poulpe","63D163"),
    ERMITERAT("Ermite Rat", 23, "ermite_icon", "Ermite_Rat","63D163"),
    ERMITEFAUCON("Ermite Faucon", 24, "ermite_icon", "Ermite_Faucon","63D163"),
    ERMITEARAIGNEE("Ermite Araignée", 25, "ermite_icon", "Ermite_Araignee","63D163"),
    ERMITETORTUE("Ermite Tortue", 26, "ermite_icon", "Ermite_Tortue","63D163"),
    ERMITESINGE("Ermite Singe", 27, "ermite_icon", "Ermite_Singe","63D163"),
    ERMITETAUREAU("Ermite Taureau", 28, "ermite_icon", "Ermite_Taureau","63D163"),
    ERMITELION("Ermite Lion", 29, "ermite_icon", "Ermite_Lion","63D163"),
    SPECIAL("Spécial", 30, "special_icon", "Special_comme_samere","FD2F92"),
    ONI("Oni", 31, "oni_icon", "Oni","693631"),
    YAMANAKA("Yamanaka", 32, "yamanaka_icon", "Yamanaka",""),
    NARA("Nara", 33, "nara_icon", "Nara",""),
    AKIMICHI("Akimichi", 34, "akimichi_icon", "Akimichi","931A07"),
    UZUMAKI("Uzumaki", 35, "uzumaki_icon", "Uzumaki","AF4658"),
    SARUTOBI("Sarutobi", 36, "sarutobi_icon", "Sarutobi",""),
    DEI("Dei", 37, "dei_icon", "Dei",""),
    DOKU("Doku", 38, "doku_icon", "Doku",""),
    TAIYAN("Taiyan", 39, "taiyan_icon", "Taiyan",""),
    SAMOURAI("Samourai", 40, "samourai_icon", "Samourai",""),
    GUGEN("Gugen", 41, "gugen_icon", "Gugen","CBD2D8"),
    HATAKE("Hatake", 42, "hatake_icon", "Hatake","919191"),
    KATABAMI("Katabami", 42, "katabami_icon", "Katabami","2F4934"),
    INDEFINI("Non défini", -1, "null", "null_null_null","");

    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String tag;
    @Getter
    private String identifiant;
    @Getter
    private String colorHexa;

    Clan(String name, int id, String tag, String identifiant, String colorHexa){
        this.name = name;
        this.id = id;
        this.tag = tag;
        this.identifiant = identifiant;
        this.colorHexa = colorHexa;
    }

    public static Clan getFromID(int id){
        for(Clan clan : values()){
            if(clan.getId() == id){
                return clan;
            }
        }
        return INDEFINI;
    }

    public static int getIDFromName(String name) {
        for(Clan clan : values()){
            if(clan.getName().equals(name)){
                return clan.getId();
            }
        }
        return -2;
    }

    public static Clan getFromIdentifiant(String identifiant) {
        for(Clan clan : values()){
            if(clan.getIdentifiant().equals(identifiant)){
                return clan;
            }
        }
        return INDEFINI;
    }

    public static Clan getFromName(String name) {
        for(Clan clan : values()){
            if(clan.getName().equals(name)){
                return clan;
            }
        }
        return INDEFINI;
    }
}
