package Server.enums;

public enum Mood {
    SADNESS("sadness"),
    SORROW("sorrow"),
    GLOOM("gloom"),
    RAGE("rage"),
    FRENZY("frenzy");

    private String name;

    public String toString(){
        return name;
    }

    private Mood(String stringVal) {
        name=stringVal;
    }

    public static String getEnumByString(String code){
        for(Mood e : Mood.values()){
            if(e.name.equals(code)) return e.name();
        }
        return null;
    }
}
