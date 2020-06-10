package Server.Command;

import Server.Exceptions.HumanValueException;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.Checker;
import Server.Tools.Converter;
import Server.enums.Mood;
import Server.enums.WeaponType;

import java.util.*;

public class Update extends Command {
    public Update(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        StringTokenizer tokenizer = new StringTokenizer(command);
        tokenizer.nextToken();
        int id = Integer.parseInt(tokenizer.nextToken());
        for (int i = 0; i < humanList.getHumanBeings().size(); i++) {
            if (humanList.getHumanBeings().get(i).getId() == id) {
                try{
                    HumanBeing humanBeing = new HumanBeing();
                    humanBeing.setId(human.size() + 1);
                    humanBeing.setCreationDate();

                    humanBeing.setName(tokenizer.nextToken());

                    humanBeing.setCoordinatesX(Float.parseFloat(tokenizer.nextToken()));

                    humanBeing.setCoordinatesY(Integer.parseInt(tokenizer.nextToken()));

                    humanBeing.setRealHero(Boolean.parseBoolean(tokenizer.nextToken()));

                    humanBeing.setHasToothpick(Boolean.parseBoolean(tokenizer.nextToken()));

                    humanBeing.setImpactSpeed(Integer.parseInt(tokenizer.nextToken()));

                    String str = tokenizer.nextToken();
                    switch (str){
                        case "HAMMER":
                            humanBeing.setWeaponType(WeaponType.HAMMER);
                            break;
                        case "PISTOL":
                            humanBeing.setWeaponType(WeaponType.PISTOL);
                            break;
                        case "SHOTGUN":
                            humanBeing.setWeaponType(WeaponType.SHOTGUN);
                            break;
                        case "RIFLE":
                            humanBeing.setWeaponType(WeaponType.RIFLE);
                            break;
                        case "KNIFE":
                            humanBeing.setWeaponType(WeaponType.KNIFE);
                            break;
                    }

                    str = tokenizer.nextToken();
                    switch (str){
                        case "SADNESS":
                            humanBeing.setMood(Mood.SADNESS);
                            break;
                        case "SORROW":
                            humanBeing.setMood(Mood.SORROW);
                            break;
                        case "GLOOM":
                            humanBeing.setMood(Mood.GLOOM);
                            break;
                        case "RAGE":
                            humanBeing.setMood(Mood.RAGE);
                            break;
                        case "FRENZY":
                            humanBeing.setMood(Mood.FRENZY);
                            break;
                    }

                    System.out.println("По настоящему ли крута машина персонажа (true/false): ");
                    humanBeing.setCarCool(Boolean.parseBoolean(tokenizer.nextToken()));

                    try{
                        Checker.checkHuman(humanList.getHumanBeing(i));
                    }catch (HumanValueException e){
                        e.printStackTrace();
                    }
                } catch(NoSuchElementException e) { System.out.println("Вы не ввели id персонажа. Попытайтесь снова, введя команду заново"); }
                break;
            }
        }
        return Converter.сonvertToLinkedHashMap(humanList);
    }
}
