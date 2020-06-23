package Server.NewServer;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

public class Sender extends Thread {
    private ForkJoinPool pool;
    private Socket incoming;

    public Sender(Socket incoming, int nThreads) {
        pool = new ForkJoinPool(nThreads);
        this.incoming = incoming;
    }

    @Override
    public void run() {
        while (!isInterrupted());
    }

    public void send(String string) {
        SenderHandler senderHandler = new SenderHandler(incoming, string);
        pool.execute(senderHandler);
    }
}
