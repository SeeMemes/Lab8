package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class RemoveKey extends Command {
    public RemoveKey(LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList) {
        super(human, command, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b){
        StringTokenizer tokenizer = new StringTokenizer(command);
        tokenizer.nextToken();
        int id = Integer.parseInt(tokenizer.nextToken());
        try{
            if (dataBase.remove_by_ID(id, credentials.getUsername())) {
                human.remove(id);
                System.out.println("Объект успешно удален");
            }
            else System.out.println("Не удалось удалить объект");
        } catch (SQLException e) {
            System.err.println("Невозможно удалить объект под номером " + id);
        }
        return human;
    }


}
