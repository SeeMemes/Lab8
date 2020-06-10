package Server.Database;

import Server.MyOwnClasses.*;
import Server.enums.Mood;
import Server.enums.WeaponType;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class CollectionDBManager {

    private final Connection connection;

    public CollectionDBManager(Connection connection) {
        this.connection = connection;
    }

    public LinkedHashMap<Integer, HumanBeing> getCollection() throws SQLException {
        LinkedHashMap<Integer, HumanBeing> collection = new LinkedHashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.HUMANBEING);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();
            Car car = new Car();
            car.setCool(rs.getBoolean("car"));
            HumanBeing humanBeing = new HumanBeing(
                    rs.getString("name"),
                    new Coordinates(rs.getFloat("x"), rs.getInt("y")),
                    rs.getBoolean("realhero"),
                    rs.getBoolean("hastootpick"),
                    rs.getInt("impactspeed"),
                    WeaponType.valueOf(rs.getString("weapontype")),
                    Mood.valueOf(rs.getString("mood")),
                    car
            );

            collection.put(rs.getInt("id"), humanBeing);
        }

        return collection;
    }

    public boolean hasPermissions(Credentials credentials, int humanBeingID) throws SQLException {
        if (credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return true;

        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.USER_HAS_PERMISSIONS);
        int pointer = 0;
        preparedStatement.setInt(1, credentials.id);
        preparedStatement.setInt(2, humanBeingID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("exists");
        }
        return false;
    }

    public String add(HumanBeing humanBeing, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Add.HUMANBEING);
            preparedStatement.setString(1, humanBeing.getName());
            preparedStatement.setBoolean(3, humanBeing.getRealHero());
            preparedStatement.setBoolean(4, humanBeing.isHasToothpick());
            preparedStatement.setLong(5, humanBeing.getImpactSpeed());
            preparedStatement.setString(6, humanBeing.getWeaponType().name());
            preparedStatement.setString(7, humanBeing.getMood().name());
            preparedStatement.setBoolean(8, humanBeing.getCar().isCool());
            ResultSet rs = preparedStatement.executeQuery();
            int humanBeingID = 0;
            if (rs.next())
                humanBeingID = rs.getInt(1);

            preparedStatement = connection.prepareStatement(SqlQuery.Add.COORDINATE);
            preparedStatement.setFloat(1, humanBeing.getCoordinates().getX());
            preparedStatement.setDouble(2, humanBeing.getCoordinates().getY());
            preparedStatement.setInt(3, humanBeingID);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Add.HUMANBEING_USER_RELATIONSHIP);
            preparedStatement.setInt(1, credentials.id);
            preparedStatement.setInt(2, humanBeingID);
            preparedStatement.executeUpdate();

            connection.commit();

            return String.valueOf(humanBeingID);
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String update(int id, HumanBeing humanBeing, Credentials credentials) throws SQLException {
        if (!hasPermissions(credentials, id))
            return "You don't have permissions";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Update.HUMANBEING);
            preparedStatement.setString(1, humanBeing.getName());
            preparedStatement.setBoolean(3, humanBeing.getRealHero());
            preparedStatement.setBoolean(4, humanBeing.isHasToothpick());
            preparedStatement.setLong(5, humanBeing.getImpactSpeed());
            preparedStatement.setString(6, humanBeing.getWeaponType().name());
            preparedStatement.setString(7, humanBeing.getMood().name());
            preparedStatement.setBoolean(8, humanBeing.getCar().isCool());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Update.COORDINATE);
            preparedStatement.setFloat(1, humanBeing.getCoordinates().getX());
            preparedStatement.setDouble(2, humanBeing.getCoordinates().getY());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

    public int getCityByID(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.HUMAN_BY_ID);
        int pointer = 0;
        preparedStatement.setInt(++pointer, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public String deleteAll(Credentials credentials) throws SQLException {
        if (!credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return "You don't have permissions to delete all database, only root";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.ALL_HUMANBEING);
            preparedStatement.executeUpdate();
            connection.commit();
            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String delete(int id, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            int dragonID = getCityByID(id);
            if (!hasPermissions(credentials, dragonID))
                return "You don't have permissions";

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.HUMANBEING_BY_ID);
            int pointer = 0;
            preparedStatement.setInt(++pointer, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

}
