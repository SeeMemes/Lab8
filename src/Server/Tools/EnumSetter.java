package Server.Tools;

import Server.enums.Mood;
import Server.enums.WeaponType;

public class EnumSetter {
    public static Mood getMoodfromString (String str){
        switch (str.toUpperCase()){
            case "SADNESS":
                return Mood.SADNESS;
            case "SORROW":
                return Mood.SORROW;
            case "GLOOM":
                return Mood.GLOOM;
            case "RAGE":
                return Mood.RAGE;
            case "FRENZY":
                return Mood.FRENZY;
            default:
                return null;
        }
    }

    public static WeaponType getWeaponTypefromString (String str){
        switch (str.toUpperCase()){
            case "HAMMER":
                return WeaponType.HAMMER;
            case "PISTOL":
                return WeaponType.PISTOL;
            case "SHOTGUN":
                return WeaponType.SHOTGUN;
            case "RIFLE":
                return WeaponType.RIFLE;
            case "KNIFE":
                return WeaponType.KNIFE;
            default:
                return null;
        }
    }
}
