package Server.Command;

import Server.MyOwnClasses.Commands;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.CompareHumanBeings;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class ReplaceIfLowe extends Command {

    public ReplaceIfLowe(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        HumanBeing humanBeing = new HumanBeing();
        StringTokenizer stringTokenizer = new StringTokenizer(command);
        stringTokenizer.nextToken();
        int key = Integer.parseInt(stringTokenizer.nextToken());
        try {
            Commands.setHumanBeing(humanBeing, humanList.getHumanBeings().size() + 1, command);
        } catch (InputMismatchException e) {
            System.out.println("Попробуйте еще раз, введя правильные данные.");
        }
        if (CompareHumanBeings.compare(humanBeing, human.get(key))) {
                human.replace(key, human.get(key), humanBeing);
        }
        return human;
    }
}
