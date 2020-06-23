package Server.Database;

public class SqlQuery {
    public static class Get {
        //humanBeing
        public static final String HUMANBEING = "SELECT id, name, x, y, realHero, hasToothPick, impactSpeed, weaponType, mood, car\n" +
                "FROM humanBeing\n" +
                "    INNER JOIN coordinates ON id = coordinates.humanBeing_id\n";

        public static final String HUMAN_BY_ID = "SELECT id FROM HUMANBEING where id = ?";

        //Users
        public static final String USERS = "SELECT * FROM users";
        public static final String PASS_USING_USERNAME = "SELECT password, id FROM users WHERE username = ?";
        public static final String ID_USING_USERNAME = "SELECT id FROM users WHERE username = ?";

        public static final String USER_HAS_PERMISSIONS = "" +
                "SELECT exists(SELECT 1 from users_humanBeing where user_id = ? AND humanBeing_id = ?)";
    }
    public static class Add {
        //humanBeing
        public static final String HUMANBEING = "" +
                "INSERT INTO humanBeing(name, realHero, hasToothPick, impactSpeed, weaponType, mood, car)\n"+
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        public static final String COORDINATE = "" +
                "INSERT INTO coordinates(x, y, humanBeing_id) " +
                "VALUES(?, ?, ?)";

        //Users
        public static final String USER = "" +
                "INSERT INTO users(username, password) VALUES(?, ?)";

        public static final String HUMANBEING_USER_RELATIONSHIP = "" +
                "INSERT INTO users_humanBeing VALUES (?, ?)";
    }
    public static class Update {
        public static final String HUMANBEING = "" +
        "UPDATE humanBeing SET name=?, realHero=?, hasToothPick=?, impactSpeed=?, weaponType=?, mood=?, car=?\n"+
                "WHERE humanBeing.id = ?;";
        public static final String COORDINATE = "" +
                "UPDATE coordinates SET x = ?, y = ? WHERE humanBeing_id = ?";
    }
    public static class Delete {
        public static final String ALL_HUMANBEING = "DELETE FROM HUMANBEING";
        public static final String HUMANBEING_BY_ID = "DELETE FROM HUMANBEING where id = ?";

        public static final String USER = "DELETE FROM users where username = ?";
    }
}
