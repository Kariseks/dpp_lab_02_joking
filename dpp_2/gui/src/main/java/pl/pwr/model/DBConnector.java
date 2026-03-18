package pl.pwr.model;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
            System.out.println("Baza nie istnieje. Tworzę nową bazę z danymi testowymi...");
            try (Connection conn = getConnection()) {
                // Ścieżki od katalogu 'resources'
                executeSqlScript(conn, "pl/pwr/model/DBCreator.sql");
                executeSqlScript(conn, "pl/pwr/model/seeder.sql");
            } catch (SQLException e) {
                System.err.println("Błąd inicjalizacji bazy: " + e.getMessage());
            }
        }
    }

    private static void executeSqlScript(Connection conn, String resourcePath) {
        // getResourceAsStream szuka plików wewnątrz skompilowanych zasobów (target/classes)
        try (InputStream is = DBConnector.class.getResourceAsStream("/" + resourcePath)) {

            if (is == null) {
                System.err.println("Nie znaleziono pliku zasobów: " + resourcePath);
                return;
            }

            // Czytamy cały strumień do Stringa
            String script = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String[] queries = script.split(";");

            try (Statement stmt = conn.createStatement()) {
                for (String query : queries) {
                    String trimmedQuery = query.trim();
                    if (!trimmedQuery.isEmpty()) {
                        stmt.execute(trimmedQuery);
                    }
                }
            }
            System.out.println("Skrypt [" + resourcePath + "] wykonany pomyślnie.");

        } catch (Exception e) {
            System.err.println("Błąd podczas wykonywania skryptu " + resourcePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}