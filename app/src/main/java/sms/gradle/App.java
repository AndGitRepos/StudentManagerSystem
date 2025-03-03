/*
 * This source file was generated by the Gradle 'init' task
 */
package sms.gradle;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        String url = "jdbc:h2:./data/test";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connection established successfully!");

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS TEST_TABLE");
                stmt.execute("CREATE TABLE TEST_TABLE (id INT PRIMARY KEY, name VARCHAR(255))");
                System.out.println("Table created successfully!");

                stmt.execute("INSERT INTO TEST_TABLE VALUES (1, 'Alice')");
                stmt.execute("INSERT INTO TEST_TABLE VALUES (2, 'Bob')");
                System.out.println("Data inserted successfully!");

                try (ResultSet rs = stmt.executeQuery("SELECT * FROM TEST_TABLE")) {
                    System.out.println("\nQuery Results:");
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        System.out.printf("ID: %d, Name: %s%n", id, name);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error occurred!");
            e.printStackTrace();
        }
    }
}
