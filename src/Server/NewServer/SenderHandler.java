package Server.NewServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SenderHandler implements Runnable {
    private Socket incoming;
    private String string;

    public SenderHandler(Socket incoming, String string) {
        this.incoming = incoming;
        this.string = string;
    }

    @Override
    public void run() {
        try (ObjectOutputStream sendToClient = new ObjectOutputStream(incoming.getOutputStream())) {
            sendToClient.writeObject("���������� �����������.\n�� ������ ������� �������.");
            while (true) {
                sendToClient.writeObject(string);
                System.out.println("����� ������� ���������.");
            }
        } catch (IOException e) {
            System.err.println("�� ���������� ��������� �����");
            e.printStackTrace();
        }
    }
}
