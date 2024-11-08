package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;


public enum ChakraType {

    RAITON("§eRaiton", "F0B01D"),
    KATON("§cKaton", "CE2029"),
    DOTON("§6Doton", "84655F"),
    SUITON("§9Suiton", "2F94C1"),
    FUUTON("§fFûton", "E6F0F4"),
    REZUTON( "§1Rezuton", "2957EE"),
    SHINTON( "§8Shinton", "3F3F63"),
    SHOTON("§dShoton", "DF90EB"),
    HETON("§7Heton", "8BD43E"),
    HARITON("§bHariton", "FFF4AB"),
    VUUTON("§7Vuuton", "AEB8C3"),
    YOTON("§4Yôton", "7F3034"),
    HAITON("§cHaiton", "6D4064"),
    BAKUTON("§6Bakuton", "F97431"),
    NENDOTON("§6Nendoton", "B15B5A"),
    HYUTON("§3Hyôton", "8EDBFF"),
    RANTON( "§8Ranton", "2D4C5F"),
    FUUKATON("§7Fuukaton", "3C7158"),
    JITON("§fJiton", "7F7CA1"),
    NETON("§7Neton", "47C3BA"),
    MOKUTON("§2Mokuton", ""),
    MITSUTON("§6Mitsuton", ""),
    DENTON("§0Denton", ""),
    NULL("ERREUR", "");

    @Getter
    public String name;
    @Getter
    public String colorHexa;
    ChakraType(String name, String colorHexa) {
        this.name = name;
        this.colorHexa = colorHexa;
    }


    public static ChakraType fromName(String str){
        for(ChakraType chakra : values()){
            if(chakra.getName().substring(2).equals(str)){
                return chakra;
            }
        }
        return NULL;
    }

    public static ChakraType fromId(int id) {
        for(ChakraType type : values()) {
            if(type.ordinal() == id) {
                return type;
            }
        }
        return null;
    }
}