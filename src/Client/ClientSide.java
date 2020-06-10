package Client;

import Client.NewClient.ClientConnection;
import Client.SocketClient.BlockingClient;

public class ClientSide {

    public static void main(String[] args) {
        System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.work();
        }
}