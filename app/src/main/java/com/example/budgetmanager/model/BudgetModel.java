package com.example.budgetmanager.model;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BudgetModel {
    private final Map<String, Double> budgets;
    private final DatabaseManager databaseManager;

    // 构造函数，初始化 budgets
    public BudgetModel() {
        budgets = new HashMap<>();
        databaseManager = new DatabaseManager();
    }

    // 添加新的预算类别及其金额到内存映射和数据库中 这里是个接口
    public void addBudget(String category, double amount) throws SQLException {
        // 检查参数的合法性
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        // 将预算信息添加到内存映射中
        budgets.put(category, amount);
        // 将预算信息添加到数据库中
        databaseManager.addBudgetToDatabase(this);
    }

// 获取指定类别的预算金额，如果不存在则返回默认值 0.0
public double getBudgetAmount(String category) throws SQLException {
    if (category == null) {
        throw new IllegalArgumentException("Category cannot be null.");
    }

    // 如果 budgets 中不包含该类别的预算数据，则从数据库加载
    if (!budgets.containsKey(category)) {
        Map<String, Double> loadedBudgets = databaseManager.loadBudgetFromDatabase();
        budgets.putAll(loadedBudgets); // 更新内存映射
    }

    // 获取预算金额，如果 budgets 中没有该类别的预算数据，则返回 0.0
    Double budgetAmount = budgets.get(category);
    return budgetAmount != null ? budgetAmount : 0.0;
}


    // 从数据库加载预算数据并计算总预算
    public Map<String, Double> getAllBudgets() throws SQLException {
        Map<String, Double> totalBudgets = new HashMap<>();

        // 从数据库加载预算数据
        Map<String, Double> loadedBudgets = databaseManager.loadBudgetFromDatabase();

        // 计算总预算
        double totalAmount = 0.0;
        for (double amount : loadedBudgets.values()) {
            totalAmount += amount;
        }
        totalBudgets.put("Total Budget", totalAmount);

        // 更新数据库中的 budget 表
        databaseManager.clearBudgetsInDatabase();
        for (Map.Entry<String, Double> entry : totalBudgets.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            databaseManager.addTotalBudgetToDatabase(category, amount);
        }

        return totalBudgets;
    }

    // 返回所有预算类别的迭代器 它会返回所有已定义的预算类别 不知道有什么用
    public Iterable<String> getBudgetCategories() {
        return budgets.keySet();
    }

    // 清空预算数据并从数据库中清空 准备用于撤销数据，待开发
    public void clearBudgets() throws SQLException {
        // 清空内存映射
        budgets.clear();
        // 清空数据库
        databaseManager.clearBudgetsInDatabase();
    }
}
