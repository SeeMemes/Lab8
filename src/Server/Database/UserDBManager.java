package Server.Database;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDBManager {

    public static final String DEFAULT_USERNAME = "default";
    public static final String ROOT_USERNAME = "root";
    private final Connection connection;

    public UserDBManager(Connection connection) {
        this.connection = connection;
    }

    public int checkUserAndGetID(Credentials credentials) throws SQLException, NoSuchAlgorithmException {
        if (credentials == null || credentials.username.equals(DEFAULT_USERNAME))
            return -1;

        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.PASS_USING_USERNAME);
        preparedStatement.setString(1, credentials.username);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            if (hashPassword(credentials.password).equals(rs.getString(1)))
                return rs.getInt(2);
            else
                return -1;
        }
        rs.close();
        preparedStatement.close();

        return -1;
    }


    public int registerUser(Credentials credentials) throws SQLException, NoSuchAlgorithmException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement stmt = connection.prepareStatement("select * from users");
            ArrayList<String> usersNamesa = new ArrayList<>();
            stmt.execute();
            ResultSet rss = stmt.getResultSet();
            while (rss.next()) {
                usersNamesa.add(rss.getString("name"));
            }
            if (!usersNamesa.contains(credentials.getUsername())) {
                PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Add.USER);
                int pointer = 0;
                preparedStatement.setString(++pointer, credentials.username);
                preparedStatement.setString(++pointer, hashPassword(credentials.password));
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(SqlQuery.Get.ID_USING_USERNAME);
                pointer = 0;
                preparedStatement.setString(++pointer, credentials.username);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next())
                    return rs.getInt(1);

                connection.commit();
            }
            else return -1;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
        return -1;
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return toHexString(md.digest(password.getBytes(StandardCharsets.UTF_8)));
    }
    public String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32)
            hexString.insert(0, '0');

        return hexString.toString();
    }
}
