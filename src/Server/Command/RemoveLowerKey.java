package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.*;

public class RemoveLowerKey extends Command {
    public RemoveLowerKey(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        StringTokenizer stringTokenizer = new StringTokenizer(command);
        stringTokenizer.nextToken();
        int number = Integer.parseInt(stringTokenizer.nextToken());
        human.keySet().removeIf(key -> key < number);
        /*Set<Integer> keys = human.keySet();
        List<Integer> listKeys = new ArrayList<Integer>(keys);
        for (int i = 0; i < listKeys.size(); i++)
            if (listKeys.get(i) < number)
                human.remove(listKeys.get(i));*/
        return human;
    }
}
