package com.example.budgetmanager.model;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class DatabaseManager {
    // 数据库连接字符串
    private static final String DB_URL = "jdbc:sqlite:budget_manager.db";
    // 数据库连接对象
    private Connection connection;

    // 连接数据库
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
    }

    // 关闭数据库连接
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // 添加预算数据到数据库
    public void addBudgetToDatabase(BudgetModel budgetModel) throws SQLException {
        connect();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Budgets (category, amount) VALUES (?, ?)");
        Map<String, Double> budgets = budgetModel.getAllBudgets();
        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            preparedStatement.setString(1, entry.getKey());
            preparedStatement.setDouble(2, entry.getValue());
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
        close();
    }
    //添加总预算到数据库当中
    public void addTotalBudgetToDatabase(String category, double amount) throws SQLException {
        connect();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Budgets (category, amount) VALUES (?, ?)");
        preparedStatement.setString(1, category);
        preparedStatement.setDouble(2, amount);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        close();
    }

    // 清空预算数据表
    public void clearBudgetsInDatabase() throws SQLException {
        connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM Budgets");
        statement.close();
        close();
    }

    // 添加支出数据到数据库
    public void addExpenseToDatabase(String category, double amount, String note, String date) throws SQLException {
        // 实现添加支出数据到数据库的逻辑
        connect();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Expenses (category, amount, note, date) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, category);
        preparedStatement.setDouble(2, amount);
        preparedStatement.setString(3, note);
        preparedStatement.setString(4, date);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        close();
    }



    // 从数据库加载预算数据
    public Map<String, Double> loadBudgetFromDatabase() throws SQLException {
        connect();
        Map<String, Double> budgets = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Budgets");
        while (resultSet.next()) {
            String category = resultSet.getString("category");
            double amount = resultSet.getDouble("amount");
            budgets.put(category, amount);
        }
        resultSet.close();
        statement.close();
        close();
        return budgets;
    }

    // 从数据库加载支出数据
    public List<ExpenseModel.Expense> loadExpensesFromDatabase() throws SQLException {
        connect();
        List<ExpenseModel.Expense> expenses = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Expenses");
        while (resultSet.next()) {
            String category = resultSet.getString("category");
            double amount = resultSet.getDouble("amount");
            String note = resultSet.getString("note");
            String date = resultSet.getString("date");
            ExpenseModel.Expense expense = new ExpenseModel.Expense(category, amount, note, date);
            expenses.add(expense);
        }
        resultSet.close();
        statement.close();
        close();
        return expenses;
    }
}
