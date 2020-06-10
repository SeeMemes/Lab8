/**
 * This simple tool class allows us to convert from ArrayList to LinkedHashMap and backwards
 */

package Server.Tools;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Converter {
    public Converter() {}
    public static LinkedHashMap<Integer, HumanBeing> сonvertToLinkedHashMap(HumanList humanList) {
        LinkedHashMap<Integer, HumanBeing> linkedHashMap = new LinkedHashMap<Integer, HumanBeing>();
        for (int i = 0; i < humanList.getHumanBeings().size(); i++) linkedHashMap.put (i, humanList.getHumanBeing(i));
        return linkedHashMap;
    }
    public static ArrayList<HumanBeing> convertToList(LinkedHashMap<Integer, HumanBeing> linkedHashMap){
        ArrayList<HumanBeing> humanList = new ArrayList<HumanBeing>(linkedHashMap.values());
        return humanList;
    }
}
