package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;

public class Exit extends Command {
    public Exit(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        new Save(human, "save", humanList).execute(human, command, humanList, b);
        System.out.println("Конец программы");
        System.exit(0);
        return human;
    }
}
