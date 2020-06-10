package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SumOfImpactSpeed extends Command {
    public SumOfImpactSpeed(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public String execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        /*int k = 0;
        for (int i = 0; i < humanList.getHumanBeings().size(); i++)
            k += humanList.getHumanBeing(i).getImpactSpeed();*/
        int sum = humanList.getHumanBeings().stream().mapToInt(i->i.getImpactSpeed()).sum();
        return Integer.toString(sum);
    }
}
