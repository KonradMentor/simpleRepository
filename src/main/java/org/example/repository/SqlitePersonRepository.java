package org.example.repository;

import org.example.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SqlitePersonRepository implements PersonRepository {
    private static final String JDBC_SQLITE_PREFIX = "jdbc:sqlite:";
    private static final String INSERT_QUERY = "INSERT INTO Persons (Name, Surname) VALUES (?, ?)";
    private static final String SUCCESSFUL_INSERT_MESSAGE = "Saved %s %s to sqlite database. %d row(s) inserted successfully.";
    private static final String DELETE_ALL_QUERY = "DELETE FROM Persons WHERE Name = ? AND Surname = ?";
    private static final String SUCCESSFUL_REMOVE_QUERY = "Removed all entries for %s %s from sqlite database. %d row(s) deleted successfully.";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Persons WHERE Id = ?";
    private static final String SUCCESSFUL_DELETION_BY_ID_MESSAGE = "Removed entry with id %d from sqlite database. %d row(s) deleted successfully.";
    private static final String GET_ALL_PERSONS_QUERY = "SELECT Name,Surname FROM Persons";
    private Connection conn;

    public SqlitePersonRepository(String dbFile) {
        try {
            this.conn = DriverManager.getConnection(JDBC_SQLITE_PREFIX + dbFile);
        } catch (SQLException e) {
            System.err.println("Unable to create database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void save(String name, String surname) {
        int rowsAffected = performUpdate(INSERT_QUERY, name, surname);
//        int rowsAffected = performNotSafeUpdate(String.format("INSERT INTO Persons (Name, Surname) VALUES ('%s', '%s')", name, surname));
        System.out.printf((SUCCESSFUL_INSERT_MESSAGE) + "%n", name, surname, rowsAffected);
    }

    @Override
    public void removeAll(String name, String surname) {
        int rowsAffected = performUpdate(DELETE_ALL_QUERY, name, surname);
        System.out.printf(SUCCESSFUL_REMOVE_QUERY, name, surname, rowsAffected);
    }

    @Override
    public void removeById(int id) {
        int rowsAffected = performUpdate(DELETE_BY_ID_QUERY, Integer.toString(id));
        System.out.printf(SUCCESSFUL_DELETION_BY_ID_MESSAGE, id, rowsAffected);
    }

    @Override
    public List<Person> findAll() {
        List<Person> personList;
        try (ResultSet resultSet = conn.createStatement().executeQuery(GET_ALL_PERSONS_QUERY)) {
            personList = extractPersons(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return personList;
    }
    
    @Override
    public void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Unable to close database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private int performNotSafeUpdate(String query) {
        //execute query without prepared statement
        //not safe, because it is vulnerable to SQL injection
        //for example, if name is Robert'); DROP TABLE Customers; --", then the whole table will be deleted

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Unable to perform update for query: " + query);
            throw new RuntimeException(e);
        }
        return 1;
    }

    private int performUpdate(String query, String... params) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            IntStream.range(0, params.length)
                    .forEach(i -> setPreparedStatementParameters(i, params[i], preparedStatement));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Unable to perform update for query: " + query);
            e.printStackTrace();
            return 0;
        }
    }

    private void setPreparedStatementParameters(int index, String param, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setString(index + 1, param);
        } catch (SQLException e) {
            System.err.println("Failed to set parameter at index " + (index + 1) + " with value " + param);
            e.printStackTrace();
        }
    }

    private List<Person> extractPersons(ResultSet resultSet) throws SQLException {
        List<Person> personList = new ArrayList<>();
        while (resultSet.next()) {
            personList.add(new Person(resultSet.getString("Name"), resultSet.getString("Surname")));
        }
        return personList;
    }
}
