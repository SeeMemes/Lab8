package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.FWriter;

import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

public class Save extends Command {
    public Save(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
            String newPath = "New_File";
            FWriter.unParse(humanList, newPath);

        return human;
    }
}
