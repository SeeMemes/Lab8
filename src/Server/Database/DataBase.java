package Server.Database;

import Server.MyOwnClasses.Car;
import Server.MyOwnClasses.Coordinates;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.EnumSetter;
import Server.enums.Mood;
import Server.enums.WeaponType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import static Server.Tools.AppConstant.*;

public class DataBase {
    public static Connection conn = null;
    PreparedStatement stmt = null;
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_DEFAULT_URL = "jdbc:postgresql://localhost:5432/postgres";

    //  Database credentials

    public DataBase() throws SQLException {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.postgresql.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            boolean b = true;
            String url = DB_DEFAULT_URL;
            String user = "postgres";
            String pass = "12345";
            while (b) {
                try {
                    conn = DriverManager.getConnection(url, user, pass);
                    b = false;
                } catch (SQLException e) {
                    Scanner scanner = new Scanner(System.in);
                    System.err.println("Неправильно введены данные для входа в дб. Попробуйте снова введя новые: ");
                    System.out.print("URL: ");
                    url = scanner.nextLine();
                    System.out.println();

                    System.out.print("USER: ");
                    user = scanner.nextLine();
                    System.out.println();

                    System.out.print("PASS: ");
                    pass = scanner.nextLine();
                    System.out.println();
                    b = true;
                }
            }

            //STEP 4: Execute a query
            System.out.println("Database created successfully...");
        } catch (
                Exception e) {

            e.printStackTrace();
        }

    }

    public boolean author(String name, String password) throws SQLException {
        String basepassword = "";
        ArrayList<String> usersNamesa = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("select * from users");
        stmt.execute();
        ResultSet rss = stmt.getResultSet();
        while (rss.next()) {
            usersNamesa.add(rss.getString("username"));
        }
        if (usersNamesa.contains(name)) {
            PreparedStatement statement = conn.prepareStatement("select password from users where username=?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rsa = statement.getResultSet();
            while (rsa.next()) {
                basepassword = rsa.getString("password");
            }

            if (basepassword.equals(password)) {
                return true;
            } else {
                return false;
            }

        }
        return false;

    }

    public boolean regist(String name, String password) throws SQLException {
        ArrayList<String> usersNames = new ArrayList<>();
        stmt = conn.prepareStatement("select * from users");
        stmt.execute();
        int id = (int) (Math.random()*1000000);
        ResultSet rss = stmt.getResultSet();
        while (rss.next()) {
            usersNames.add(rss.getString("username"));
        }
        if (usersNames.contains(name)) {
            return false;
        } else {
            stmt = conn.prepareStatement("INSERT INTO users (username, password, id) VALUES (?,?,?) returning password");
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setInt(3, id);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                System.out.println("Пользователь зарегистрирован, его пароль: " + rs.getString("password"));
            }
        }
        usersNames.clear();
        return true;
    }

    public LinkedHashMap<Integer, HumanBeing> updateHumanBeings() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("select * from humanbeing");
        LinkedHashMap<Integer, HumanBeing> human = new LinkedHashMap<Integer, HumanBeing>();
        if (preparedStatement.execute()) {
            ResultSet rs = preparedStatement.getResultSet();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float x = rs.getFloat("cord_x");
                int y = rs.getInt("cord_y");
                boolean realHero = rs.getBoolean("realhero");
                boolean hasToothPick = rs.getBoolean("hastoothpick");
                int impactSpeed = rs.getInt("impactspeed");
                WeaponType weaponType = EnumSetter.getWeaponTypefromString(rs.getString("weapontype"));
                Mood mood = EnumSetter.getMoodfromString(rs.getString("mood"));
                boolean car_b = rs.getBoolean("car");

                try {
                    Coordinates coordinates = new Coordinates(x, y);
                    Car car = new Car(car_b);
                    HumanBeing humanBeing = new HumanBeing(name, coordinates, realHero, hasToothPick, impactSpeed, weaponType, mood, car);
                    human.put(id, humanBeing);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ошибочка");
                }

            }
        }
        return human;
    }

    public boolean insertHumanBeing(HumanBeing humanBeing, int id, String username) throws SQLException {
        String name = humanBeing.getName();
        float x = humanBeing.getCoordinates().getX();
        int y = humanBeing.getCoordinates().getY();
        boolean realHero = humanBeing.getRealHero();
        boolean hasToothPick = humanBeing.isHasToothpick();
        int impactSpeed = humanBeing.getImpactSpeed();
        String weaponType = humanBeing.getWeaponType().name().toLowerCase();
        String mood = humanBeing.getMood().name().toLowerCase();
        boolean car = humanBeing.getCar().isCool();
        System.out.println(username);

        PreparedStatement preparedStatement =
                conn.prepareStatement("insert into humanbeing (id, name, cord_x, cord_y, realhero, hastoothpick, impactspeed, weapontype, mood, car, username) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setFloat(3, x);
        preparedStatement.setInt(4, y);
        preparedStatement.setBoolean(5, realHero);
        preparedStatement.setBoolean(6, hasToothPick);
        preparedStatement.setInt(7, impactSpeed);
        preparedStatement.setString(8, weaponType);
        preparedStatement.setString(9, mood);
        preparedStatement.setBoolean(10, car);
        preparedStatement.setString(11, username);

        preparedStatement.execute();
        return true;
    }

    public String getUsername(int id) throws SQLException {
        String username = "";
        PreparedStatement statement = conn.prepareStatement("select username from humanbeing where id=?");
        statement.setInt(1, id);
        statement.execute();
        ResultSet rsa = statement.getResultSet();
        while (rsa.next()) {
            username = rsa.getString("username");
        }
        return username;
    }

    public boolean updateID(HumanBeing humanBeing, int objID, String username) throws SQLException {

        String name = humanBeing.getName();
        float x = humanBeing.getCoordinates().getX();
        int y = humanBeing.getCoordinates().getY();
        boolean realHero = humanBeing.getRealHero();
        boolean hasToothPick = humanBeing.isHasToothpick();
        int impactSpeed = humanBeing.getImpactSpeed();
        String weaponType = humanBeing.getWeaponType().name().toLowerCase();
        String mood = humanBeing.getMood().name().toLowerCase();
        boolean car = humanBeing.getCar().isCool();
        System.out.println(username);
        String ObjUser = "";

        PreparedStatement statement = conn.prepareStatement("select username from humanbeing where id=?");
        statement.setInt(1, objID);
        statement.execute();
        ResultSet rsa = statement.getResultSet();
        while (rsa.next()) {
            ObjUser = rsa.getString("username");
        }
        System.out.println(username +" "+ ObjUser);
        if (!ObjUser.contains(username)) {
            return false;
        }else {


            PreparedStatement preparedStatement =
                    conn.prepareStatement("update humanbeing set " +
                            "(name, cord_x, cord_y, realhero, hastoothpick, impactspeed, weapontype, mood, car, username) " +
                            "= (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) where id=?");
            preparedStatement.setString(1, name);
            preparedStatement.setFloat(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setBoolean(4, realHero);
            preparedStatement.setBoolean(5, hasToothPick);
            preparedStatement.setInt(6, impactSpeed);
            preparedStatement.setString(7, weaponType);
            preparedStatement.setString(8, mood);
            preparedStatement.setBoolean(9, car);
            preparedStatement.setString(10, username);
            preparedStatement.setInt(11, objID);
            preparedStatement.execute();
            return true;
        }
    }

    public boolean remove_by_ID(int id, String username) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("select * from humanbeing");
        stmt.execute();
        ResultSet rss = stmt.getResultSet();
        while (rss.next()) {
            ids.add(rss.getInt("id"));
        }
        if(!ids.contains(id)){
            return false;
        }else {
            String ObjUser = "";
            PreparedStatement statement = conn.prepareStatement("select username from humanbeing where id=?");
            statement.setInt(1, id);
            statement.execute();
            ResultSet rsa = statement.getResultSet();

            while (rsa.next()) {
                ObjUser = rsa.getString("username");
            }
            if (!username.equals(ObjUser)){
                return false;
            }else{
                PreparedStatement statementt = conn.prepareStatement("delete from humanbeing where id=?");
                statementt.setInt(1, id);
                statementt.execute();
                return true;
            }

        }
    }

}

