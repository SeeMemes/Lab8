package Client;

import Server.Database.Credentials;
import Server.Database.CurrentUser;

import java.io.Serializable;

public class ServerRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    String command;
    String arguments;
    CurrentUser currentUser;

    public ServerRequest(String command, String arguments, CurrentUser currentUser){
        this.command = command;
        this.arguments = arguments;
        this.currentUser = currentUser;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
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

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", arguments='" + arguments + '\'' +
                '}';
    }
}
