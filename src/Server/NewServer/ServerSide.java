package Server.NewServer;

import Server.Database.CollectionDBManager;
import Server.Database.DBConfigure;
import Server.Database.DatabaseController;
import Server.Database.UserDBManager;
import Server.SocketServer.CollectionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static Server.Tools.AppConstant.*;

public class ServerSide {

    public static void main(String[] args) throws Exception {
        CollectionManager serverCollection;
        Scanner scanner = new Scanner(System.in);

        final DBConfigure dbConfigure = new DBConfigure();
        String dblogin = DB_USER;
        String dbpassword = DB_PASS;
        while (!dbConfigure.connect(dblogin, dbpassword))
        {
            System.out.print("db login: ");
            dblogin = scanner.nextLine();
            System.out.print("db password: ");
            dbpassword = scanner.nextLine();
        }

        final CollectionDBManager collectionDBManager = new CollectionDBManager(dbConfigure.getDbConnection());
        final UserDBManager userDBManager = new UserDBManager(dbConfigure.getDbConnection());
        final DatabaseController controller = new DatabaseController(collectionDBManager, userDBManager);

        try (ServerSocket server = new ServerSocket(DEFAULT_PORT)) {
            System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + DEFAULT_PORT +
                    " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиентов ");
            Thread pointer = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.print(".");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.print("\n");
                        Thread.currentThread().interrupt();
                    }
                }
            });

            while (true) {
                Socket incoming = server.accept();
                pointer.interrupt();
                System.out.println(incoming + " подключился к серверу.");
                Runnable r = new ServerConnection(controller, incoming);
                Thread t = new Thread(r);
                t.start();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}