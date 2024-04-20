package com.example.budgetmanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


// ExpenseModel 类负责管理支出数据
public class ExpenseModel {
    private List<Expense> expenses;
    private final DatabaseManager databaseManager;

    public ExpenseModel(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        expenses = new ArrayList<>();
    }

    // 添加支出信息  预留接口
    public void addExpense(String category, double amount, String note, String date) throws SQLException {
        // 检查参数的合法性
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty.");
        }
        // 将支出信息添加到列表中
        expenses.add(new ExpenseModel.Expense(category, amount, note, date));

        // 将支出信息添加到数据库
        databaseManager.addExpenseToDatabase(category, amount, note, date);
    }

    // 返回不可修改的支出信息列表 接口
    public List<Expense> getExpenses() throws SQLException {
        // 如果 expenses 还没有被初始化，可以在这里进行初始化
        if (expenses == null) {
            expenses = new ArrayList<>();
        }

        // 加载支出数据并赋值给 expenses
        expenses = databaseManager.loadExpensesFromDatabase();

        // 返回一个不可修改的支出列表
        return Collections.unmodifiableList(expenses);
    }

    // 清空支出信息列表 待开发 接口
    public void clearExpenses() {
        expenses.clear();
    }

    // 内部静态类，表示支出信息
    public static class Expense {
        private final String category; // 支出类别
        private final double amount;   // 支出金额
        private final String note;     // 备注
        private final String date;     // 支出日期

        // 支出信息的构造函数
        public Expense(String category, double amount, String note, String date) {
            this.category = category;
            this.amount = amount;
            this.note = note;
            this.date = date;
        }

        // 获取支出类别 待开发
        public String getCategory() {
            return category;
        }

        // 获取支出金额 待开发
        public double getAmount() {
            return amount;
        }

        // 获取备注 待开发
        public String getNote() {
            return note;
        }

        // 获取支出日期 待开发
        public String getDate() {
            return date;
        }
    }
}
