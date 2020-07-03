package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.enums.WeaponType;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GetHumanBeingList extends Command {
    public GetHumanBeingList(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public ArrayList<String[]> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b){
        ArrayList<String[]> humanBeings = new ArrayList<>();

        List<HumanBeing> humanBeingList = humanList.getHumanBeings();
        List<Integer> keys = new ArrayList<>(human.keySet());
        System.out.println(keys.toString());
        for (int i = 0; i < humanBeingList.size(); i++) {
            HumanBeing humanBeing = humanBeingList.get(i);
            String id = Long.toString(keys.get(i));
            String name = humanBeing.getName();
            String x = Float.toString(humanBeing.getCoordinates().getX());
            String y = Integer.toString(humanBeing.getCoordinates().getY());
            String realhero = Boolean.toString(humanBeing.getRealHero());
            String hastoothpick = Boolean.toString(humanBeing.isHasToothpick());
            String impactspeed = Integer.toString(humanBeing.getImpactSpeed());
            String weapontype = humanBeing.getWeaponType().toString();
            String mood = humanBeing.getMood().toString();
            String car = Boolean.toString(humanBeing.getCar().isCool());
            try {
                String username = dataBase.getUsername(keys.get(i));

                String c[] = new String[]{id, name, x, y, realhero, hastoothpick, impactspeed, weapontype, mood, car, username};
                humanBeings.add(c);
            } catch (SQLException e) {e.printStackTrace();}
        }

        return humanBeings;
    }
}
