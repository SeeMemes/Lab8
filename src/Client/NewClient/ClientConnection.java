package Client.NewClient;

import Client.Creator;
import Client.ServerRequest;
import Server.Database.Credentials;
import Server.Database.CurrentUser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

import static Server.Tools.AppConstant.*;

public class ClientConnection {

    private static Scanner fromKeyboard;
    private static ObjectOutputStream toServer;
    private static ObjectInputStream fromServer;
    private ArrayList<String> commandList= new ArrayList<>();
    private ArrayList<String> userCommand= new ArrayList<>();

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void work() {
        try (Scanner scanner = new Scanner(System.in)) {
            commandList.add("insert");
            commandList.add("update");
            userCommand.add("login");
            userCommand.add("register");
            fromKeyboard = scanner;
            while (true) {
                try (Socket outcoming = new Socket(InetAddress.getLocalHost(), DEFAULT_PORT)) {
                    try (ObjectOutputStream outputStream = new ObjectOutputStream(outcoming.getOutputStream());
                         ObjectInputStream inputStream = new ObjectInputStream(outcoming.getInputStream())) {
                        toServer = outputStream;
                        fromServer = inputStream;
                        interactiveMode();
                        System.out.println("Завершение программы.");
                    }
                } catch (IOException e) {
                    System.err.println("Нет связи с сервером. Подключться ещё раз (введите {да} или {нет})?");
                    String answer;
                    while (!(answer = fromKeyboard.nextLine()).equals("да")) {
                        switch (answer) {
                            case "":
                                break;
                            case "нет":
                                exit();
                                break;
                                default: System.out.println("Введите корректный ответ.");
                        }
                    }
                    System.out.print("Подключение ...");
                    continue;
                }
            }
        }
    }

    /**
     * Парсит пользовательские команды и осуществляет обмен данными с сервером.
     * @throws IOException при отправке и получении данных с сервера.
     */
    private void interactiveMode() throws IOException {
        try {
            System.out.println((String) fromServer.readObject());
            String command;
            CurrentUser currentUser = new CurrentUser(new Credentials(-1, DEFAULT_LOGIN, DEFAULT_PASSWORD));
            System.out.print("> ");
            while (!(command = fromKeyboard.nextLine()).equals("exit")) {
                String[] parsedCommand = command.trim().split(" ", 2);
                switch (parsedCommand[0]) {
                    case "":
                        break;
                        default:
                            StringTokenizer stringTokenizer = new StringTokenizer(command);
                            String toExecute = stringTokenizer.nextToken();
                            String argument = "";
                            if (commandList.contains(toExecute)) {
                                try{
                                    argument = stringTokenizer.nextToken() + Creator.StringDataCreator();
                                } catch (Exception e) {
                                    System.out.println("Возможно вы не ввели id, попробуйте еще раз");
                                    continue;
                                }
                            }
                            else if (userCommand.contains(toExecute)){
                                try{
                                    System.out.print("Введите логин: ");
                                    String name = fromKeyboard.nextLine();
                                    System.out.print("Введите пароль: ");
                                    String password = fromKeyboard.nextLine();
                                    System.out.print("Повторите пароль: ");
                                    if (!fromKeyboard.nextLine().equals(password)) throw new DataFormatException();
                                    argument = name + " " + password;
                                    ServerRequest serverRequest = new ServerRequest(command, argument, currentUser);
                                    toServer.writeObject(serverRequest);
                                    Boolean serverAnswer = (Boolean) fromServer.readObject();
                                    if (serverAnswer) currentUser = new CurrentUser(new Credentials(-1, name, password));
                                    else throw new DataFormatException();
                                    System.out.print("> ");
                                    continue;
                                } catch (DataFormatException e){
                                    System.out.println("Вы ввели неправильные данные для входа/регистрации");
                                    continue;
                                }
                            }
                            else try{
                                if (stringTokenizer.hasMoreTokens()) argument = stringTokenizer.nextToken();
                            } catch (Exception e) {}
                            ServerRequest serverRequest = new ServerRequest(command, argument, currentUser);
                            toServer.writeObject(serverRequest);
                            System.out.println("Данные отправлены");
                            String serverAnswer = (String) fromServer.readObject();
                            System.out.println(serverAnswer);
                            System.out.println("Данные получены");
                            System.out.print("> ");
                }
            }
            exit();
        } catch (ClassNotFoundException e) {
            System.err.println("Класс не найден: " + e.getMessage());
        }
    }

    /**
     * Импортирует локальный файл и отправляет на сервер.
     * @param path путь к файлу.
     * @return команду для сервера и содержимое файла.
     * @throws SecurityException если отсутствуют права rw.
     * @throws IOException если файл не существует.
     */
    private String importingFile(String path) throws SecurityException, IOException {
        File localCollection = new File(path);
        String extension = localCollection.getAbsolutePath().substring(localCollection.getAbsolutePath().lastIndexOf(".") + 1);
        if (!localCollection.exists() | localCollection.length() == 0  | !extension.equals("xml"))
            throw new FileNotFoundException();
        if (!localCollection.canRead()) throw new SecurityException();
        try (BufferedReader inputStreamReader = new BufferedReader(new FileReader(localCollection))) {
            String nextLine;
            StringBuilder result = new StringBuilder();
            while ((nextLine = inputStreamReader.readLine()) != null) result.append(nextLine);
            return "import " + result.toString();
        }
    }

    /*
    Отвечает за завершение работу клиентского приложения.
     */
    private void exit() {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}