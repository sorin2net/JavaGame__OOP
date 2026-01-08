package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDatabase {
    private static final String DB_URL = "jdbc:sqlite:scores.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC"); //incarca driverul JDBC
        } catch (ClassNotFoundException e) {
            System.err.println("nu s-a putut incarca driverul: " + e.getMessage());
            System.exit(1);
        }
    }

    public ScoreDatabase() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createFinalScoresTable = "CREATE TABLE IF NOT EXISTS final_scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "player_name TEXT NOT NULL, " +
                "final_health INTEGER, " +
                "final_score INTEGER, " +
                "potions INTEGER, " +
                "current_level INTEGER, " +
                "player_x REAL DEFAULT 0, " +
                "player_y REAL DEFAULT 0, " +
                "completed_game INTEGER DEFAULT 0, " +
                "skelly1 INTEGER DEFAULT 0, " +
                "skelly2 INTEGER DEFAULT 0, " +
                "golem INTEGER DEFAULT 0, " +
                "potiuni_luate INTEGER DEFAULT 0, " +
                "boss INTEGER DEFAULT 0, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

        String createCumulativeScoresTable = "CREATE TABLE IF NOT EXISTS cumulative_scores (" +
                "player_name TEXT PRIMARY KEY, " +
                "total_score INTEGER DEFAULT 0, " +
                "completed_game INTEGER DEFAULT 0, " +
                "highest_level_x REAL DEFAULT 0, " +
                "highest_level_y REAL DEFAULT 0, " +
                "highest_level INTEGER DEFAULT 0, " +
                "potion_h INTEGER DEFAULT 0, " +
                "highest_health INTEGER DEFAULT 0, " +
                "skelly1 INTEGER DEFAULT 0, " +
                "skelly2 INTEGER DEFAULT 0, " +
                "golem INTEGER DEFAULT 0, " +
                "potiuni_luate INTEGER DEFAULT 0, " +
                "boss INTEGER DEFAULT 0)";
        //se inchid automat resursele
        try (Connection connection = getConnection(); //obtine conexiunea cu driverul definit
             Statement statement = connection.createStatement()) {  // pentru a putea executa comenzile in baza de date
            statement.execute(createFinalScoresTable);
            statement.execute(createCumulativeScoresTable);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveFinalScore(String playerName, int finalHealth, int finalScore, int potions, int currentLevel,
                               boolean completedGame, float playerX, float playerY,
                               int skelly1, int skelly2, int golem, int potiuniLuate, int boss) {

        String insertFinalScore = "INSERT INTO final_scores (player_name, final_health, final_score, potions, " +
                "current_level, completed_game, player_x, player_y, skelly1, skelly2, golem, potiuni_luate, boss) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String updateCumulativeScore = "INSERT OR REPLACE INTO cumulative_scores (player_name, total_score, completed_game, " +
                "highest_level_x, highest_level_y, highest_level, potion_h, highest_health, skelly1, skelly2, golem, potiuni_luate, boss) " +
                "VALUES (?, " +
                "COALESCE((SELECT total_score FROM cumulative_scores WHERE player_name = ?), 0) + ?, " +
                "COALESCE((SELECT COUNT(*) FROM final_scores WHERE player_name = ? AND completed_game = 1), 0), " +// sa nu fie null ci 0
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //ca sa salvez in cumalativ pozitia jucatorului, nivelul, potiunile, viata.. de la ultimul nivel la care a ramas
        String getHighestLevelQuery = "SELECT current_level, player_x, player_y, final_health, potions, skelly1, skelly2, golem, potiuni_luate, boss " +
                "FROM final_scores WHERE player_name = ? ORDER BY current_level DESC, potions DESC LIMIT 1";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // sa apelez manula connection.commit ca sa salvez mai multe simultan

            try (PreparedStatement finalScoreStmt = connection.prepareStatement(insertFinalScore);
                 PreparedStatement cumulativeStmt = connection.prepareStatement(updateCumulativeScore);
                 PreparedStatement highestLevelStmt = connection.prepareStatement(getHighestLevelQuery)) {

                finalScoreStmt.setString(1, playerName);
                finalScoreStmt.setInt(2, finalHealth);
                finalScoreStmt.setInt(3, finalScore);
                finalScoreStmt.setInt(4, potions);
                finalScoreStmt.setInt(5, currentLevel);
                finalScoreStmt.setInt(6, completedGame ? 1 : 0);
                finalScoreStmt.setFloat(7, playerX);
                finalScoreStmt.setFloat(8, playerY);
                finalScoreStmt.setInt(9, skelly1);
                finalScoreStmt.setInt(10, skelly2);
                finalScoreStmt.setInt(11, golem);
                finalScoreStmt.setInt(12, potiuniLuate);
                finalScoreStmt.setInt(13, boss);
                finalScoreStmt.executeUpdate();

                float highestLevelX = 0, highestLevelY = 0;
                int highestLevel = 0, highestHealth = 0, highestPotions = 0;
                int highSkelly1 = 0, highSkelly2 = 0, highGolem = 0, highPotiuniLuate = 0, highBoss = 0;

                highestLevelStmt.setString(1, playerName);
                try (ResultSet resultSet = highestLevelStmt.executeQuery()) {
                    if (resultSet.next()) {
                        highestLevel = resultSet.getInt("current_level");
                        highestLevelX = resultSet.getFloat("player_x");
                        highestLevelY = resultSet.getFloat("player_y");
                        highestHealth = resultSet.getInt("final_health");
                        highestPotions = resultSet.getInt("potions");
                        highSkelly1 = resultSet.getInt("skelly1");
                        highSkelly2 = resultSet.getInt("skelly2");
                        highGolem = resultSet.getInt("golem");
                        highPotiuniLuate = resultSet.getInt("potiuni_luate");
                        highBoss = resultSet.getInt("boss");
                    }
                }

                cumulativeStmt.setString(1, playerName);
                cumulativeStmt.setString(2, playerName);
                cumulativeStmt.setInt(3, finalScore);
                cumulativeStmt.setString(4, playerName);
                cumulativeStmt.setFloat(5, highestLevelX);
                cumulativeStmt.setFloat(6, highestLevelY);
                cumulativeStmt.setInt(7, highestLevel);
                cumulativeStmt.setInt(8, highestPotions);
                cumulativeStmt.setInt(9, highestHealth);
                cumulativeStmt.setInt(10, highSkelly1);
                cumulativeStmt.setInt(11, highSkelly2);
                cumulativeStmt.setInt(12, highGolem);
                cumulativeStmt.setInt(13, highPotiuniLuate);
                cumulativeStmt.setInt(14, highBoss);
                cumulativeStmt.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback(); // anuleaza modificarile din tranzactia curenta
                System.err.println("tranzactia nu a reusit si a fost  anulat: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("eroare la salvare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }


    public List<CumulativeScoreEntry> getCumulativeScores(int limit) {
        List<CumulativeScoreEntry> cumulativeScores = new ArrayList<>();
        String query = "SELECT player_name, total_score FROM cumulative_scores ORDER BY total_score DESC LIMIT ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, limit); // spun cati vreau sa imi afiseze cu scorul cel mai mare in starea scor
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) { // itereaza pe fiecare rand
                String playerName = resultSet.getString("player_name");
                int totalScore = resultSet.getInt("total_score");
                cumulativeScores.add(new CumulativeScoreEntry(playerName, totalScore));
            }
        } catch (SQLException e) {
            System.err.println("eroare: " + e.getMessage());
            e.printStackTrace();
        }

        return cumulativeScores;
    }

    public static class CumulativeScoreEntry {
        private final String playerName;
        private final int totalScore;

        public CumulativeScoreEntry(String playerName, int totalScore) {
            this.playerName = playerName;
            this.totalScore = totalScore;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getTotalScore() {
            return totalScore;
        }

    }


    // astea se folosesc la load
    public PlayerData getPlayerData(String playerName) {
        String query = "SELECT highest_level, highest_level_x, highest_level_y, completed_game, potion_h, highest_health, " +
                "skelly1, skelly2, golem, potiuni_luate, boss FROM cumulative_scores WHERE player_name = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, playerName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int highestLevel = resultSet.getInt("highest_level");
                    float highestLevelX = resultSet.getFloat("highest_level_x");
                    float highestLevelY = resultSet.getFloat("highest_level_y");
                    boolean completedGame = resultSet.getInt("completed_game") == 1;
                    int highestPotions = resultSet.getInt("potion_h");
                    int highestHealth = resultSet.getInt("highest_health");
                    int skelly1 = resultSet.getInt("skelly1");
                    int skelly2 = resultSet.getInt("skelly2");
                    int golem = resultSet.getInt("golem");
                    int potiuniLuate = resultSet.getInt("potiuni_luate");
                    int boss = resultSet.getInt("boss");

                    return new PlayerData(highestLevel, highestLevelX, highestLevelY, completedGame,
                            highestPotions, highestHealth, skelly1, skelly2, golem, potiuniLuate, boss);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching player data: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static class PlayerData {
        private final int highestLevel;
        private final float highestLevelX;
        private final float highestLevelY;
        private final boolean completedGame;
        private final int highestPotions;
        private final int highestHealth;
        private final int skelly1, skelly2, golem, potiuniLuate, boss;

        public PlayerData(int highestLevel, float highestLevelX, float highestLevelY, boolean completedGame,
                          int highestPotions, int highestHealth, int skelly1, int skelly2, int golem, int potiuniLuate, int boss) {
            this.highestLevel = highestLevel;
            this.highestLevelX = highestLevelX;
            this.highestLevelY = highestLevelY;
            this.completedGame = completedGame;
            this.highestPotions = highestPotions;
            this.highestHealth = highestHealth;
            this.skelly1 = skelly1;
            this.skelly2 = skelly2;
            this.golem = golem;
            this.potiuniLuate = potiuniLuate;
            this.boss = boss;
        }

        public int getHighestLevel() {
            return highestLevel;
        }

        public float getHighestLevelX() {
            return highestLevelX;
        }

        public float getHighestLevelY() {
            return highestLevelY;
        }

        public boolean isCompletedGame() {
            return completedGame;
        }

        public int getHighestPotions() {
            return highestPotions;
        }

        public int getHighestHealth() {
            return highestHealth;
        }

        public int getSkelly1() {
            return skelly1;
        }

        public int getSkelly2() {
            return skelly2;
        }

        public int getGolem() {
            return golem;
        }

        public int getPotiuniLuate() {
            return potiuniLuate;
        }

        public int getBoss() {
            return boss;
        }
    }


    public int getHighestLevelScore(String playerName) {
        String query = "SELECT final_score FROM final_scores WHERE player_name = ? " +
                "ORDER BY current_level DESC LIMIT 1";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("final_score"); // am necoie de el ca sa il scad din cumulative score
            }
        } catch (SQLException e) {
            System.err.println("Erorare la lua datele de la cel mai mare nivel pentru un jucator: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteHighestLevelRecord(String playerName) {
        int scoreToSubtract = getHighestLevelScore(playerName);
        String updateQuery = "UPDATE cumulative_scores SET total_score = total_score - ? " +
                "WHERE player_name = ?";

        String deleteQuery = "DELETE FROM final_scores WHERE id = " +
                "(SELECT id FROM final_scores WHERE player_name = ? " +
                "ORDER BY current_level DESC LIMIT 1)";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                 PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {

                //scade din scorul de la cumulative
                updateStmt.setInt(1, scoreToSubtract);
                updateStmt.setString(2, playerName);
                updateStmt.executeUpdate();

                //sterge din final
                deleteStmt.setString(1, playerName);
                deleteStmt.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                System.err.println("eroare: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("eroare la a sterge un rand: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
