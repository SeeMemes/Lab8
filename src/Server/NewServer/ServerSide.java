package Server.NewServer;

import Server.Database.CollectionDBManager;
import Server.Database.DBConfigure;
import Server.Database.DatabaseController;
import Server.Database.UserDBManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static Server.Tools.AppConstant.*;

public class ServerSide {

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
        new Thread(new ServerCommandShell(controller)).start();

        try (ServerSocket server = new ServerSocket(DEFAULT_PORT)) {
            System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + DEFAULT_PORT +
                    " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиентов ");

            while (true) {
                Socket incoming = server.accept();
                System.out.println(incoming + " подключился к серверу.");
                new Thread(new ServerConnection(controller, incoming)).start();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}