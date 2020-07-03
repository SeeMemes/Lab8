package Server.ThreadServer;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerBootstrap {

    private final Thread serverThread;

    public ServerBootstrap(int port, RequestHandler requestHandler) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        this.serverThread = new ServerThread(serverSocket, requestHandler);
    }

    public void start() {
        this.serverThread.start();
    }

    public void stop() {
        this.serverThread.interrupt();
    }
}
