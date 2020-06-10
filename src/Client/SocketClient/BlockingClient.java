package Client.SocketClient;

import Client.Creator;
import Client.ServerRequest;
import Server.Command.Login;
import Server.Database.Credentials;
import Server.Database.CurrentUser;
import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author Crunchify.com
 *
 */

public class BlockingClient {

    private static Scanner fromKeyboard;
    private ArrayList<String> commandList= new ArrayList<>();
    public static final String HOST = "localhost";
    public static final int PORT = 8810;

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void work() {
        try (Scanner scanner = new Scanner(System.in)) {
            commandList.add("insert");
            commandList.add("update");
            fromKeyboard = scanner;
            while (true) {
                try (Socket outcoming = new Socket(HOST, PORT)) {
                    outcoming.setSoTimeout(5000);
                    interactiveMode(outcoming);
                    exit();
                } catch (IOException | ClassNotFoundException e) {
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
                    System.out.println("Подключение ...");
                    continue;
                }
            }
        }
    }

    public void interactiveMode(Socket socket) throws IOException, ClassNotFoundException {
        String command;
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        char readChar = (char) reader.read();
        builder.append(readChar);
        System.out.print("Введите логин: ");
        String login = fromKeyboard.nextLine();
        System.out.print("Введите пароль: ");
        String password = fromKeyboard.nextLine();
        CurrentUser currentUser = new CurrentUser(new Credentials(-1, login, password));

        while (reader.ready()) {
            readChar = (char) reader.read();
            builder.append(readChar);
        }
        System.out.println(builder.toString());
        System.out.print("> ");
        while (!(command = fromKeyboard.nextLine()).equals("exit")) {
            OutputStream writer = socket.getOutputStream();

            StringTokenizer stringTokenizer = new StringTokenizer(command);
            String toExecute = stringTokenizer.nextToken();
            String argument = "";
            if (commandList.contains(toExecute)) {
                try {
                    argument = stringTokenizer.nextToken() + Creator.StringDataCreator();
                } catch (Exception e) {
                    System.out.println("Возможно вы не ввели id, попробуйте еще раз");
                    continue;
                }
            } else try {
                if (stringTokenizer.hasMoreTokens()) argument = stringTokenizer.nextToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (command.toLowerCase().equals("save")) command = "error";

            ServerRequest serverRequest = new ServerRequest(command, argument, currentUser);
            writer.write(serialize(serverRequest));
            writer.flush();
            builder = new StringBuilder();
            readChar = (char) reader.read();
            builder.append(readChar);
            while (reader.ready()) {
                readChar = (char) reader.read();
                builder.append(readChar);
            }
            System.out.println(builder.toString());
            System.out.print("> ");
        }
    }

    private static byte[] serialize(ServerRequest serverRequest) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(serverRequest);
        return  out.toByteArray();
    }

    private void exit() {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}