package edu.school21.raceserver.GameResultRepository;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameResultRepository {
    private final Connection connection;

    public GameResultRepository(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
        Statement statement = dataSource.getConnection().createStatement();
        statement.execute("CREATE SCHEMA IF NOT EXISTS race;\n" +
                "CREATE TABLE IF NOT EXISTS race.results(\n" +
                "    id SERIAL PRIMARY KEY,\n" +
                "    score INTEGER NOT NULL);");
    }
    public void save(long score) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO race.results(score) " +
                "VALUES(" + score + ")");
    }
    public long getHighScore() {
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT score FROM race.results ORDER BY id DESC LIMIT 1;");
            if (!set.next()) {
                return 0L;
            } else {
                return set.getLong(1);
            }
        } catch (SQLException e) {
            return 0L;
        }
    }

}
