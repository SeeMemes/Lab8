package Server.SocketServer;

import Client.ServerRequest;
import Server.Command.*;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.Converter;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class SelectorServer {

    public static final String EXIT_COMMAND = "EXIT";
    public static final String HOST = "localhost";
    public static final int PORT = 8810;
    String answer = null;
    private CollectionManager serverCollection;
    private HashMap<String, Command> commandList;

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        CollectionManager serverCollection;
        try {
            serverCollection = new CollectionManager(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Изначальный путь не задан, будет создан файл New_File");
            String name = "New_File.xml";
            File file = new File(name);
            try {
                OutputStreamWriter filewriter = new OutputStreamWriter(new FileOutputStream(name));
                filewriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> \n<humanList>\n</humanList>");
                filewriter.close();
            } catch (IOException e1) {
            }
            serverCollection = new CollectionManager(name);
            serverCollection.setCreationDate();
        }
        System.out.println("Сервер начал слушать клиентов. " + "\nПорт " + PORT +
                " / Адрес " + HOST + ".\nОжидаем подключения клиентов ");
        new SelectorServer(serverCollection).startServer();
    }

    public SelectorServer(CollectionManager serverCollection) {
        this.serverCollection = serverCollection;
        LinkedHashMap<String, Command> commandList = new LinkedHashMap<>();
        HumanList humanList = serverCollection.getHumans();
        LinkedHashMap<Integer, HumanBeing> human = serverCollection.getHuman();
        commandList.put("save", new Save(human, "save", humanList));
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

    public void startServer() throws IOException, ClassNotFoundException, SQLException {
        Reader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = bootstrapServer(serverSocketChannel, HOST, PORT);

        boolean running = true;

        while (running) {
            if (reader.ready()) {
                String stdinCommand = readCommand(reader).trim();
                String result = handleCommand(stdinCommand);
                switch (result) {
                    case EXIT_COMMAND:
                        running = false;
                        System.out.println(result);
                        continue;

                }
                System.out.println(this.answer);
            }

            selector.select(25);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                try {
                    if (selectionKey.isAcceptable()) {
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println(client.getLocalAddress() + " подключился.");
                        client.write(ByteBuffer.wrap(("Вы подключены по адресу " + HOST + "\n").getBytes()));
                    } else if (selectionKey.isReadable()) {
                        read(selectionKey);
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        write(selectionKey, answer);
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                } catch (IOException e){
                    System.out.println("Удаленный хост разорвал существующее подключение");
                    selectionKey.cancel();
                }
                iterator.remove();
            }
        }

        selector.close();
        serverSocketChannel.close();
        reader.close();
        startServer();
    }

    private void read(SelectionKey selectionKey) throws IOException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
        channel.read(byteBuffer);
        byte[] bytes = byteBuffer.array();
        ServerRequest requestFromClient = deSerializer(bytes);

        System.out.println("Получено [" + requestFromClient + "] от " + channel.getLocalAddress() + ". ");
        String commandRequest = requestFromClient.getCommand() + " " + requestFromClient.getArguments();

        String result = handleCommand(commandRequest);
        if (result.equals(EXIT_COMMAND))
            channel.close();
    }

    private String handleCommand(String commandRequest) {
        Command errorCommand = new Command(null, "error", null) {
            @Override
            public String execute() {
                return "Неизвестная команда. Введите 'help' для получения списка команд.";
            }
        };
        String[] parsedCommand = commandRequest.trim().split(" ", 2);
        String response = "Команда " + commandList.getOrDefault(parsedCommand[0], errorCommand).getCommand() + " была выполнена" + "\n";
        String command_answer = "";
        String answer = null;
        try {
            LinkedHashMap<Integer, HumanBeing> humanMap = (LinkedHashMap<Integer, HumanBeing>) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                    serverCollection.getHuman(), commandRequest,
                    serverCollection.getHumans(), true);
            serverCollection.setHuman(humanMap);
            serverCollection.setHumans(Converter.convertToList(humanMap));
            answer = response + command_answer;
        } catch (ClassCastException e) {
            command_answer = (String) commandList.getOrDefault(parsedCommand[0], errorCommand).execute(
                    serverCollection.getHuman(), commandRequest,
                    serverCollection.getHumans(), true);
            answer = response + command_answer;
        }

        this.answer = answer;
        if (commandRequest.equals("exit"))
            return EXIT_COMMAND;
        return "SOME DATA " + commandRequest;
    }

    private void write(SelectionKey selectionKey, String answer) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        System.out.println("Отправлен ответ " + " на " + channel.getLocalAddress());
        channel.write(ByteBuffer.wrap(answer.getBytes()));
    }

    private static ServerRequest deSerializer(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (ServerRequest) objectInputStream.readObject();
    }

    private static Selector bootstrapServer(ServerSocketChannel channel, String host, int port) throws IOException {
        Selector selector = Selector.open();

        InetSocketAddress address = new InetSocketAddress(host, port);
        channel.bind(address);
        channel.configureBlocking(false);
        channel.register(selector, channel.validOps());
        return selector;
    }

    private static String readCommand(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (reader.ready()) {
            char readChar = (char) reader.read();
            builder.append(readChar);
        }
        return builder.toString();
    }
}