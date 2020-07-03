package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.sql.SQLException;
import java.util.*;

public class RemoveLowerKey extends Command {
    public RemoveLowerKey(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b){
        StringTokenizer stringTokenizer = new StringTokenizer(command);
        stringTokenizer.nextToken();
        int number = Integer.parseInt(stringTokenizer.nextToken());
        Set<Integer> keys = human.keySet();
        List<Integer> listKeys = new ArrayList<Integer>(keys);
        for (int i = 0; i < listKeys.size(); i++)
            if (listKeys.get(i) < number)
                try{
                    int key = listKeys.get(i);
                    if (human.containsKey(key)) {
                        if (dataBase.remove_by_ID(key, credentials.getUsername())) {
                            human.remove(key);
                        } else {
                            System.err.println("Невозможно удалить объект под номером " + i);
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Невозможно удалить объект под номером " + i);
                }

        //human.keySet().removeIf(key -> key < number);
        return human;
    }
}
