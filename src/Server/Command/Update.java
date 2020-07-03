package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.Exceptions.HumanValueException;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.Checker;
import Server.Tools.Converter;
import Server.enums.Mood;
import Server.enums.WeaponType;

import java.sql.SQLException;
import java.util.*;

public class Update extends Command {
    public Update(LinkedHashMap<Integer, HumanBeing> human, String comand, HumanList humanList) {
        super(human, comand, humanList);
    }

    @Override
    public LinkedHashMap<Integer, HumanBeing> execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, Credentials credentials, DataBase dataBase, boolean b){
        StringTokenizer tokenizer = new StringTokenizer(command);
        tokenizer.nextToken();
        tokenizer.nextToken();
        int id = Integer.parseInt(tokenizer.nextToken());
        if (human.containsKey(id)) {
            try{
                    HumanBeing humanBeing = new HumanBeing();
                    humanBeing.setId(id);
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
                    switch (str.toUpperCase()){
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

                    humanBeing.setCarCool(Boolean.parseBoolean(tokenizer.nextToken()));
                    human.put(id, humanBeing);
                    humanList.setHumanBeings(Converter.convertToList(human));

                    try{
                        Checker.checkHuman(human.get(id));
                        dataBase.updateID(humanBeing, id, credentials.getUsername());
                    }catch (HumanValueException e){
                        e.printStackTrace();
                    }
                } catch(NoSuchElementException | SQLException e) { System.out.println("Вы не ввели id персонажа. Попытайтесь снова, введя команду заново"); }
        }
        return human;
    }
}
