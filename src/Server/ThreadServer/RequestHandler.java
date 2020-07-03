package Server.ThreadServer;

import Client.ServerRequest;

public interface RequestHandler {

    /**
     * @param serverRequest Data that came from the client
     * @return Response data
     */
    byte[] handleRequest(ServerRequest serverRequest);
}
