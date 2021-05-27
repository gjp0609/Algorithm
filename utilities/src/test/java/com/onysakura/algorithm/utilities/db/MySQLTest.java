package com.onysakura.algorithm.utilities.db;

import com.onysakura.algorithm.utilities.db.mysql.MySQL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySQLTest {

    static Connection connection;
    static Statement statement;

    //    static String url = "jdbc:mysql://120.52.8.243:13306/test_epay_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
//    static String user = "test_general";
//    static String pwd = "WzkMOqUatLBXDEM";

    static String url = "jdbc:mysql://127.0.0.1:3306/mysql?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    static String user = "me";
    static String pwd = "1234567";

    static String schema = "mysql";
    static String table = "user";

    
    @BeforeAll
    static void init() {
        connection = MySQL.getConnection(url, user, pwd);
        try {
            if (connection != null) {
                statement = connection.createStatement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void select() throws Exception {
        List<User> users = MySQL.queryList(statement, User.class);
        System.out.println(users);
    }

    @AfterAll
    static void after() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
