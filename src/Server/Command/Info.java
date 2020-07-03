package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class Info extends Command {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Info(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public String execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b) {
        return "Type: LinkedHashMap\n" + "Initialization date: " + humanList.getCreationDate() + "\n"
                + "Number of elements: " + humanList.getHumanBeings().size();
    }
}
