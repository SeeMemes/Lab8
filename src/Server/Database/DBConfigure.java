package Server.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static Server.Tools.AppConstant.*;


public class DBConfigure {

    private Connection dbConnection = null;

    public boolean connect(String login, String password) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            dbConnection = DriverManager.getConnection(DB_URL, login, password);
            System.out.println("Successfully connected to: " + DB_URL);
            return true;
        } catch (SQLException e) {
            System.err.println("Unable to connect to database" + e.toString());
            return false;
        }
    }

    public void disconnect() {
        System.out.println("Disconnecting the database...");
        try {
            dbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getDbConnection(){
        return this.dbConnection;
    }
}
