package Server.NewServer;

import Server.Database.Credentials;
import Server.Database.CurrentUser;
import Server.Database.DatabaseController;
import Server.Command.*;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Client.ServerRequest;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.io.IOException;

import static Server.Database.UserDBManager.DEFAULT_USERNAME;
import static Server.Tools.AppConstant.DEFAULT_PASSWORD;


public class ServerConnection implements Runnable {

    private CollectionHandler serverCollection;
    private Socket incoming;
    private DatabaseController databaseController;
    private HashMap<String, Command> commandList = new LinkedHashMap<>();

        ServerConnection(DatabaseController databaseController, Socket incoming) {
        this.serverCollection = new CollectionHandler(databaseController);
        this.databaseController = databaseController;
        this.incoming = incoming;
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

    /**
     * Запускает активное соединение с клиентом в новом {@link Thread}.
     */
    @Override
    public void run() {

        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(reader);

        try (ObjectInputStream getFromClient = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream sendToClient = new ObjectOutputStream(incoming.getOutputStream())) {
            sendToClient.writeObject("Соединение установлено.\nВы можете вводить команды.");
            Command errorCommand = new Command(null, "error", null) {
                @Override
                public String execute() {
                    return "Неизвестная команда. Введите 'help' для получения списка команд.";
                }
            };
            CurrentUser currentUser;
            while (true) {
                try {
                    ServerRequest requestFromClient = (ServerRequest) getFromClient.readObject();

                    currentUser = requestFromClient.getCurrentUser();
                    String commandRequest = requestFromClient.getCommand() + " " + requestFromClient.getArguments();
                    System.out.print("Получено [" + requestFromClient + "] от " + incoming + ". ");
                    String[] parsedCommand = commandRequest.trim().split(" ",2);

                    String response = "Команда " + commandList.getOrDefault(parsedCommand[0], errorCommand).getCommand() + " была выполнена";
                    String command_answer = "";
                    if ((currentUser.getCredentials().getUsername().equals(DEFAULT_USERNAME)||currentUser.getCredentials().getPassword().equals(DEFAULT_PASSWORD))
                            && (!parsedCommand[0].toLowerCase().equals("login")  && !parsedCommand[0].toLowerCase().equals("register"))) {
                        sendToClient.writeObject("Войдите в систему");
                    }
                    else {
                        StringTokenizer stringTokenizer;
                        Credentials credentials;
                        switch (requestFromClient.getCommand()) {
                            case ("login"):
                                stringTokenizer = new StringTokenizer(requestFromClient.getArguments());
                                credentials = new Credentials(-1, stringTokenizer.nextToken(), stringTokenizer.nextToken());
                                sendToClient.writeObject(new Login().execute(databaseController, credentials));
                                break;
                            case ("register"):
                                stringTokenizer = new StringTokenizer(requestFromClient.getArguments());
                                credentials = new Credentials(-1, stringTokenizer.nextToken(), stringTokenizer.nextToken());
                                sendToClient.writeObject(new Register().execute(databaseController, credentials));
                                break;
                            default:
                                try {
                                    LinkedHashMap<Integer, HumanBeing> humanMap = (LinkedHashMap<Integer, HumanBeing>) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                                            serverCollection.getHuman(), commandRequest,
                                            serverCollection.getHumans(), true);
                                    serverCollection.setHuman(humanMap);
                                    //serverCollection.setHumans(Converter.convertToList(humanMap));
                                    String answer = response + " " + command_answer;
                                    sendToClient.writeObject(answer);
                                } catch (ClassCastException e) {
                                    command_answer = (String) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                                            serverCollection.getHuman(), commandRequest,
                                            serverCollection.getHumans(), true);
                                    sendToClient.writeObject(response + " \n" + command_answer);
                                }
                                break;
                        }
                    }
                    System.out.println("Ответ успешно отправлен.");
                } catch (SocketException e) {
                    System.out.println(incoming + " отключился от сервера."); //Windows
                    break;
                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            System.err.println(incoming + " отключился от сервера."); //Unix
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerConnection that = (ServerConnection) o;
        return Objects.equals(serverCollection, that.serverCollection) &&
                Objects.equals(incoming, that.incoming) &&
                Objects.equals(commandList, that.commandList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverCollection, incoming, commandList);
    }

    @Override
    public String toString() {
        return "ServerConnection{" +
                "serverCollection=" + serverCollection +
                ", incoming=" + incoming +
                ", commandList=" + commandList +
                '}';
    }
}