package Server.Command;

import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;

public class Help extends Command {
    String command;

    public Help(LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList) {
        super(human, command, humanList);
        this.command = command;
    }

    @Override
    public String execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        String answer = "help : вывести справку по доступным командам\n" +
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
                        "print_field_descending_mood mood : вывести значения поля mood в порядке убывания";

        return answer;
    }

    public String getCommand () { return command; }
}
