package Server.Command;

import Server.Database.Credentials;
import Server.Database.DatabaseController;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

import java.util.LinkedHashMap;

public class Register extends Command{
    public Register() {}

    @Override
    public Boolean execute(DatabaseController databaseController, Credentials credentials) {
        return databaseController.register(credentials);
    }
}
