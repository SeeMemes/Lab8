package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class RemoveKey extends Command {
    public RemoveKey(LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList) {
        super(human, command, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        StringTokenizer tokenizer = new StringTokenizer(command);
        tokenizer.nextToken();
        human.remove(Integer.parseInt(tokenizer.nextToken()));
        return human;
    }


}
