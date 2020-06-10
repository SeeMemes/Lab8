package Server.Command;

import Server.Database.Credentials;
import Server.Database.DatabaseController;

public class Login extends Command {
    public Login() {}

    @Override
    public Boolean execute(DatabaseController databaseController, Credentials credentials) {
        return databaseController.login(credentials);
    }
}
