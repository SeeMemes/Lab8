package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;

public class AverageOfImpactSpeed extends Command {

    public AverageOfImpactSpeed(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public String execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        /*int k = 0;
        for (int i = 0; i < humanList.getHumanBeings().size(); i++)
            k += humanList.getHumanBeing(i).getImpactSpeed();*/
        int sum = humanList.getHumanBeings().stream().mapToInt(i->i.getImpactSpeed()).sum();
        return Integer.toString(sum / humanList.getHumanBeings().size());
    }
}
