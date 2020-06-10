/**
 * Here we contain methods which help us to handle commands
 *
 * @param help shows us the command reference
 * @param save saves our HumanList to Answer.xml
 * @param show shows us the containing of LinkedHashMap
 * @param update seeks for the object with the same id as were declared with method invoking
 * @param ReadScript allows us to execute script from indicated designated place
 * @param SetHumanBeing allows us to change values of the class
 * @exception Exceptions.HumanValueException can be thrown if class creation rules are violated
 * @throws humanCheck
 */
package Server.MyOwnClasses;

import Server.Exceptions.HumanValueException;
import Server.NewServer.CommandShell;
import Server.Tools.Checker;
import Server.Tools.Converter;
import Server.Tools.FWriter;
import Server.enums.Mood;
import Server.enums.WeaponType;

import java.io.*;
import java.util.*;

public class Commands {
    public static void help(){
        System.out.println(
                "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "insert key {element} : добавить новый элемент с заданным ключом\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_key key : удалить элемент из коллекции по его ключу\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "replace_if_lowe key {element} : заменить значение по ключу, если новое значение меньше старого\n" +
                "remove_greater_key key : удалить из коллекции все элементы, ключ которых превышает заданный\n" +
                "remove_lower_key key : удалить из коллекции все элементы, ключ которых меньше, чем заданный\n" +
                "sum_of_impact_speed : вывести сумму значений поля impactSpeed для всех элементов коллекции\n" +
                "average_of_impact_speed : вывести среднее значение поля impactSpeed для всех элементов коллекции\n" +
                "print_field_descending_mood mood : вывести значения поля mood в порядке убывания"
        );
    }

    public static LinkedHashMap<Integer, HumanBeing> clean () {
        LinkedHashMap<Integer, HumanBeing> linkedHashMap = new LinkedHashMap<Integer, HumanBeing>();
        System.out.println("Очистка произведена");
        return linkedHashMap;
    }

    public static void save (HumanList humanList, String path){
        FWriter.unParse(humanList, path);
        System.out.println("Коллекция сохранена в файл " + path);
    }

    public static void show (LinkedHashMap<Integer, HumanBeing> humans) {
        System.out.print("Коллекция: ");
        System.out.println(humans);
    }

    public static void update(HumanList humanList, long id, String command){
        for (int i = 0;i < humanList.getHumanBeings().size(); i++){
            if (humanList.getHumanBeings().get(i).getId() == id) {
                setHumanBeing(humanList.getHumanBeing(i),humanList.getHumanBeings().size()+1,command);
                break;
            }
        }
    }

    public static LinkedHashMap<Integer, HumanBeing> ReadScript(String str, HumanList humanList, LinkedHashMap<Integer, HumanBeing> human, String path){
        try{
            InputStream inputStream = new FileInputStream(str);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                String comand;
                if (bufferedReader.ready()) comand = bufferedReader.readLine();
                else break;
                human = CommandShell.Analyze(humanList, human, comand, true);
                humanList.setHumanBeings(Converter.convertToList(human));
            }
        } catch (FileNotFoundException e){
            System.out.println("Данного файла не существует");
        } catch (IOException e) {
            System.out.println("Не удалось считать из файла");
        }
        return human;
    }

    public static void setHumanBeing(HumanBeing humanBeing, int id, String command) {
        StringTokenizer tokenizer = new StringTokenizer(command);
        humanBeing.setId(id);
        tokenizer.nextToken();
        tokenizer.nextToken();
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
            Checker.checkHuman(humanBeing);
        }catch (HumanValueException e){
            e.printStackTrace();
        }
    }
}
