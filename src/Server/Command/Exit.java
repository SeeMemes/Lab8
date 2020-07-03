package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;

public class Exit extends Command {
    public Exit(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b){
        new Save(human, "save", humanList).execute(human, command, humanList, credentials, dataBase, b);
        System.out.println("Конец программы");
        System.exit(0);
        return human;
    }
}
