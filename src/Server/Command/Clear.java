package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;

public class Clear extends Command {
    public Clear(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b){
        for (int i = 0; i < human.size(); i++)
            human = new RemoveKey(human, command, humanList).execute(human, "remove " + i,humanList,credentials,dataBase,b);
        System.out.println("Очистка произведена");
        return human;
    }
}
