package pl.pwr.model;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class DBConnector {
    private static final String DB_FILE = "jokes.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void setupDatabase() {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            System.out.println("Baza nie istnieje. Tworze nową bazę z danymi testowymi");
            try (Connection conn = getConnection()) {
                createTables(conn);
                seedData(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        executeSqlScript(conn, "src/main/resources/pl/pwr/model/DBCreator.sql");
    }

    private static void seedData(Connection conn) throws SQLException {
        executeSqlScript(conn,  "src/main/resources/pl/pwr/model/DBSeedData.sql");
    }


    private static void executeSqlScript(Connection conn, String fileName) {
        try {

            String script = Files.readString(Path.of("src/main/resources/" + fileName));

            String[] queries = script.split(";");   //to exectue each query separate

            try (Statement stmt = conn.createStatement()) {
                for (String query : queries) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query);
                    }
                }
            }
            System.out.println("Skrypt SQL wykonany pomyślnie.");
        } catch (Exception e) {
            System.err.println("Błąd wczytywania pliku SQL: " + e.getMessage());
        }
    }
}
