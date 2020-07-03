package Server.Command;

import Server.Database.Credentials;
import Server.Database.DataBase;
import Server.Database.DatabaseController;

import java.sql.SQLException;

public class Register extends Command{
    public Register() {}

    @Override
    public Boolean execute(DataBase dataBase, DatabaseController databaseController, Credentials credentials) {
        boolean b = false;
        try{
            b = dataBase.regist(credentials.getUsername(), credentials.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
        //return databaseController.register(credentials);
    }
}
