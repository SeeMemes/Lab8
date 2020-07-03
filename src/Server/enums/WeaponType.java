package Server.enums;

public enum WeaponType {
    HAMMER("hammer"),
    PISTOL("pistol"),
    SHOTGUN("shotgun"),
    RIFLE("rifle"),
    KNIFE("knife");

    private String name;

    public String toString(){
        return name;
    }

    private WeaponType(String stringVal) {
        name=stringVal;
    }

    public static String getEnumByString(String code){
        for(WeaponType e : WeaponType.values()){
            if(e.name.equals(code)) return e.name();
        }
        return null;
    }
}
