package Server.Database;


import Server.MyOwnClasses.HumanBeing;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DatabaseController {

    private final CollectionDBManager collectionDBManager;
    private final UserDBManager userDBManager;

    public DatabaseController(CollectionDBManager collectionDBManager, UserDBManager userDBManager) {
        this.collectionDBManager = collectionDBManager;
        this.userDBManager = userDBManager;
    }

    public LinkedHashMap<Integer, HumanBeing> getCollectionFromDB() throws SQLException {
        LinkedHashMap<Integer, HumanBeing> collection = collectionDBManager.getCollection();
        if (collection == null)
            throw new SQLException("It was not possible to fetch the collection from database");
        return collection;
    }

    public boolean login(Credentials credentials) {
        try {
            int id = userDBManager.checkUserAndGetID(credentials);
            return id > 0;
        } catch (SQLException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean register(Credentials credentials) {
        try {
            int id = userDBManager.registerUser(credentials);
            return id > 0;
        } catch (Throwable ex) {
            ex.getMessage();
            return false;
        }
    }

    public String addHumanBeing(HumanBeing humanBeing, Credentials credentials) {
        try {
            return collectionDBManager.add(humanBeing, credentials);
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    public String updateHumanBeing(int id, HumanBeing humanBeing, Credentials credentials) {
        try {
            return collectionDBManager.update(id, humanBeing, credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String clearHumanBeing(Credentials credentials) {
        try {
            return collectionDBManager.deleteAll(credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String removeHumanBeing(int id, Credentials credentials) {
        try {
            return collectionDBManager.delete(id, credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
