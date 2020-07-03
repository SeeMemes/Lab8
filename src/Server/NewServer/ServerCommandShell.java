package Server.NewServer;

import Server.Command.*;
import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.Database.DatabaseController;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.io.*;
import java.util.*;

public class ServerCommandShell implements Runnable{
    private CollectionHandler serverCollection;
    private DataBase dataBase;
    private HashMap<String, Command> commandList = new LinkedHashMap<>();

    public ServerCommandShell(DataBase dataBase) {
        this.serverCollection = new CollectionHandler(dataBase);
        this.dataBase = dataBase;
        LinkedHashMap<String, Command> commandList = new LinkedHashMap<>();
        HumanList humanList = serverCollection.getHumans();
        LinkedHashMap<Integer, HumanBeing> human = serverCollection.getHuman();
        commandList.put("you_cannot_save_from_client", new Save(human, "save", humanList));
        commandList.put("info", new Info(human, "info", humanList));
        commandList.put("exit", new Exit(human, "exit", humanList));
        commandList.put("help", new Help(human, "help", humanList));
        commandList.put("show", new Show(human, "show", humanList));
        commandList.put("clear", new Clear(human, "clear", humanList));
        commandList.put("update", new Update(human, "update", humanList));
        commandList.put("insert", new Insert(human, "insert", humanList));
        commandList.put("remove_key", new RemoveKey(human, "remove_key", humanList));
        commandList.put("execute_script", new ExecuteScript(human, "execute_script", humanList));
        commandList.put("replace_if_lowe", new ReplaceIfLowe(human, "replace_if_lowe", humanList));
        commandList.put("remove_lower_key", new RemoveLowerKey(human, "remove_lower_key", humanList));
        commandList.put("remove_greater_key", new RemoveGreaterKey(human, "remove_greater_key", humanList));
        commandList.put("sum_of_impact_speed", new SumOfImpactSpeed(human, "sum_of_impact_speed", humanList));
        commandList.put("average_of_impact_speed", new AverageOfImpactSpeed(human, "average_of_impact_speed", humanList));
        commandList.put("print_field_descending_mood", new PrintFieldDescendingMood(human, "print_field_descending_mood", humanList));
        this.commandList = commandList;
    }

    @Override
    public void run() {

        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(reader);
        System.out.println();

        while (true) {
                Command errorCommand = new Command(null, "error", null) {
                    @Override
                    public String execute() {
                        return "Неизвестная команда. Введите 'help' для получения списка команд.";
                    }
                };
                Scanner scanner = new Scanner(System.in);
                System.out.print("> ");
                String commandRequest = scanner.nextLine();
                String[] parsedCommand = commandRequest.trim().split(" ",2);

                String command = parsedCommand[0];
                String command_answer = "";
                    switch (command.toLowerCase()) {
                        case ("login"):
                            System.out.println("not a server command");
                            break;
                        case ("register"):
                            System.out.println("not a server command");
                            break;
                        case ("update"):
                            System.out.println("not a server command");
                            break;
                        case ("insert"):
                            System.out.println("not a server command");
                            break;
                        default:
                            try {
                                LinkedHashMap<Integer, HumanBeing> humanMap = (LinkedHashMap<Integer, HumanBeing>) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                                        serverCollection.getHuman(), commandRequest,
                                        serverCollection.getHumans(), new Credentials(0, "-1", "-1"), dataBase, true);
                                serverCollection.setHuman(humanMap);
                                System.out.println(command_answer);
                            } catch (ClassCastException e) {
                                command_answer = (String) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                                        serverCollection.getHuman(), commandRequest,
                                        serverCollection.getHumans(), new Credentials(0, "-1", "-1"), dataBase, true);
                                System.out.println(command_answer);
                            }
                            break;
                    }
        }
    }
}
