package Server.ThreadServer;

import Client.ServerRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServerThread extends Thread {

    public static final int READ_THREADS = 5;

    private final ServerSocket serverSocket;
    private final RequestHandler requestHandler;

    private final ExecutorService readThreadPool = Executors.newFixedThreadPool(READ_THREADS);
    private final ForkJoinPool writeThreadPool = new ForkJoinPool();
    private final Executor handlerExecutor = new ThreadPerTaskExecutor();

    public ServerThread(ServerSocket serverSocket, RequestHandler requestHandler) {
        this.serverSocket = serverSocket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        while(!thread.isInterrupted()) {
            try {
                Socket socketClient = serverSocket.accept();
                CompletableFuture
                        .supplyAsync(readRequestMessage(socketClient), readThreadPool) // read bytes
                        .thenApplyAsync((requestHandler::handleRequest),handlerExecutor) // process request
                        .thenAcceptAsync(writeResponseMessage(socketClient), writeThreadPool).thenRun( () -> closeSocket(socketClient));
            } catch (IOException e) {
                System.out.println("Не получилось принять клиента");
                e.printStackTrace();
            }

        }
        releaseResources();
    }

    private void closeSocket(Socket client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseResources() {
        try {
            readThreadPool.shutdown();
            writeThreadPool.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Consumer<byte[]> writeResponseMessage(Socket socketClient) {
        return bytes -> {
            try {
                OutputStream outputStream = socketClient.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                socketClient.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Supplier<ServerRequest> readRequestMessage(Socket socketClient) {
        return () -> {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socketClient.getInputStream());
                ServerRequest serverRequest = (ServerRequest) objectInputStream.readObject();
                socketClient.shutdownInput();
                return serverRequest;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    static final class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) { new Thread(r).start(); }
    }
}
