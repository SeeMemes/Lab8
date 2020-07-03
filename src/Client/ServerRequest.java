package Client;

import Server.Database.Credentials;

import java.io.Serializable;

public class ServerRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    String command;
    String arguments;

    Credentials credentials;

    public ServerRequest(String command, String arguments, Credentials credentials){
        this.command = command;
        this.arguments = arguments;
        this.credentials = credentials;
    }

    public String getCommand() {
        return command;
    }

    public String getArguments() {
        return arguments;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public Credentials getCredentials() { return credentials; }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", arguments='" + arguments + '\'' +
                '}';
    }
}
