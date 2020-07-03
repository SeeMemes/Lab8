package Server.NewServer;

import Client.ServerRequest;
import Server.Command.*;
import Server.Database.*;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.ThreadServer.RequestHandler;
import Server.ThreadServer.ServerBootstrap;
import Server.Tools.Converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import static Server.Database.UserDBManager.DEFAULT_USERNAME;
import static Server.Tools.AppConstant.*;

public class ServerSide {
    private static HashMap<String, Command> commandList = new LinkedHashMap<>();

    public static void main(String[] args) throws Exception {

        final DBConfigure dbConfigure = new DBConfigure();
        String dblogin = DB_USER;
        String dbpassword = DB_PASS;
        if (!dbConfigure.connect(dblogin, dbpassword)) {
            System.out.println("Неправильные данные для входа в базу данных");
        }

        final CollectionDBManager collectionDBManager = new CollectionDBManager(dbConfigure.getDbConnection());
        final UserDBManager userDBManager = new UserDBManager(dbConfigure.getDbConnection());
        final DatabaseController controller = new DatabaseController(collectionDBManager, userDBManager);

        DataBase dataBase = new DataBase();

        CollectionHandler serverCollection = new CollectionHandler(dataBase);
        serverCollection.setCreationDate();
        fill(serverCollection);

        System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + DEFAULT_PORT +
                " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиентов ");
        new Thread(new ServerCommandShell(dataBase)).start();

        ServerBootstrap serverBootstrap = new ServerBootstrap(DEFAULT_PORT, new RequestHandler() {
            @Override
            public byte[] handleRequest(ServerRequest requestFromClient) {
                Credentials currentUser;
                Command errorCommand = new Command(null, "error", null) {
                    @Override
                    public String execute() {
                        return "Неизвестная команда. Введите 'help' для получения списка команд.";
                    }
                };

                currentUser = requestFromClient.getCredentials();
                String commandRequest = requestFromClient.getCommand() + " " + requestFromClient.getArguments();
                System.out.println("Получено: " + requestFromClient + ". ");
                String[] parsedCommand = commandRequest.trim().split(" ",2);

                String response = "Команда " + commandList.getOrDefault(parsedCommand[0], errorCommand).getCommand() + " была обработана";
                String command_answer = "";
                if ((currentUser.getUsername().equals(DEFAULT_USERNAME)||currentUser.getPassword().equals(DEFAULT_PASSWORD))
                        && (!parsedCommand[0].toLowerCase().equals("login")  && !parsedCommand[0].toLowerCase().equals("register"))) {
                    return "Войдите в систему".getBytes();
                }
                else {
                    StringTokenizer stringTokenizer;
                    Credentials credentials = requestFromClient.getCredentials();
                    boolean b;
                    String auth_answer;
                    switch (requestFromClient.getCommand()) {
                        case ("login"):
                            stringTokenizer = new StringTokenizer(requestFromClient.getArguments());
                            credentials = new Credentials(-1, stringTokenizer.nextToken(), stringTokenizer.nextToken());
                            b = new Login().execute(dataBase, controller, credentials);
                            auth_answer = Boolean.toString(b);
                            return auth_answer.getBytes();
                        case ("register"):
                            stringTokenizer = new StringTokenizer(requestFromClient.getArguments());
                            credentials = new Credentials(-1, stringTokenizer.nextToken(), stringTokenizer.nextToken());
                            b = new Register().execute(dataBase, controller, credentials);
                            auth_answer = Boolean.toString(b);
                            return auth_answer.getBytes();
                        case ("get_human_being_list"):
                            new GetHumanBeingList(serverCollection.getHuman(), "info", serverCollection.getHumans());
                            ArrayList<String []> result = new GetHumanBeingList(serverCollection.getHuman(), "info", serverCollection.getHumans()).execute(
                                    serverCollection.getHuman(), commandRequest,
                                    serverCollection.getHumans(), credentials, dataBase, true);
                            try{
                                return serializeArrayList(result);
                            } catch (IOException e) {
                                return null;
                            }
                        default:
                            try {
                                LinkedHashMap<Integer, HumanBeing> humanMap = (LinkedHashMap<Integer, HumanBeing>) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                                        serverCollection.getHuman(), commandRequest,
                                        serverCollection.getHumans(), credentials, dataBase, true);
                                serverCollection.setHuman(humanMap);
                                serverCollection.setHumans(Converter.convertToList(humanMap));
                                String answer = response + " " + command_answer;
                                return answer.getBytes();
                            } catch (ClassCastException e) {
                                command_answer = (String) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                                        serverCollection.getHuman(), commandRequest,
                                        serverCollection.getHumans(), credentials, dataBase, true);
                                return (response + " \n" + command_answer).getBytes();
                            }
                    }
                }
            }
        });
        serverBootstrap.start();
    }


    private static byte[] serializeArrayList (ArrayList<String []> arrayList) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(arrayList);
        return out.toByteArray();
    }

    private static void fill(CollectionHandler serverCollection){
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
    }
}