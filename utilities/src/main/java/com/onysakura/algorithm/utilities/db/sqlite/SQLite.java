package com.onysakura.algorithm.utilities.db.sqlite;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.*;

@Slf4j
@SuppressWarnings("unused")
public class SQLite {

    private static Connection c;
    private static Statement stmt;

    public static void open(String file) {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = new File(file).getAbsolutePath();
            String sqlitePath = "jdbc:sqlite:" + path;
            c = DriverManager.getConnection(sqlitePath);
            log.debug("open database successfully, url: {}", sqlitePath);
            stmt = c.createStatement();
        } catch (Exception e) {
            log.warn("open database fail", e);
        }
    }

    public static ResultSet executeQuery(String sql) {
        log.debug("executeQuery: " + sql);
        try {
            c.setAutoCommit(false);
            ResultSet resultSet = stmt.executeQuery(sql);
            c.commit();
            log.debug("executeQuery has resultSet: " + (resultSet != null));
            return resultSet;
        } catch (SQLException e) {
            log.warn("executeUpdate sql fail, sql: " + sql, e);
            return null;
        }
    }

    public static int executeUpdate(String sql) {
        log.debug("executeUpdate: " + sql);
        try {
            c.setAutoCommit(false);
            int execute = stmt.executeUpdate(sql);
            c.commit();
            log.debug("executeUpdate result: " + execute);
            return execute;
        } catch (SQLException e) {
            log.warn("executeUpdate sql fail, sql: " + sql, e);
            return -1;
        }
    }

    public static boolean execute(String sql) {
        log.debug("execute: " + sql);
        boolean success = false;
        try {
            c.setAutoCommit(false);
            boolean execute = stmt.execute(sql);
            if (execute) {
                success = true;
                c.commit();
            }
            log.debug("execute result: " + execute);
        } catch (SQLException e) {
            log.warn("execute sql fail, sql: " + sql, e);
        }
        return success;
    }

    public static String escape(String keyWord) {
        keyWord = keyWord.replace("'", "''");
        return keyWord;
    }
}

