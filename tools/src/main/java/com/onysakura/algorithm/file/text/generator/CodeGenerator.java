package com.onysakura.algorithm.file.text.generator;

import com.onysakura.algorithm.utilities.basic.str.StringUtils;
import com.onysakura.algorithm.utilities.db.enums.Sort;
import com.onysakura.algorithm.utilities.db.mysql.MySQL;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class CodeGenerator {

    private static final String URL = "127.0.0.1:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private static final String TABLE_SCHEMA = "test";
    private static final String TABLE_NAME = "test";

    private static final String TEMPLATE_PATH = "tools/src/main/resources/template/java";
    private static final String CLASS_PATH = "tools/src/main/resources/output";

    private static final Connection CONNECTION;
    private static final Statement STATEMENT;
    private static final HashMap<String, Class<?>> TYPE_MAP;

    static {
        String url = "jdbc:mysql://" + URL + "/information_schema?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&";
        CONNECTION = MySQL.getConnection(url, USER, PASSWORD);
        STATEMENT = MySQL.createStatement(CONNECTION);

        TYPE_MAP = new HashMap<>();
        TYPE_MAP.put("varchar", String.class);
        TYPE_MAP.put("bigint", Long.class);
        TYPE_MAP.put("tinyint", Integer.class);
        TYPE_MAP.put("int", Integer.class);
        TYPE_MAP.put("datetime", LocalDateTime.class);
        TYPE_MAP.put("timestamp", LocalDateTime.class);
        TYPE_MAP.put("decimal", BigDecimal.class);
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        File file = new File(TEMPLATE_PATH);
        configuration.setDirectoryForTemplateLoading(file);
        Template template = configuration.getTemplate("Entity.ftl");
        List<Table> tables = getTableInfo();
        for (Table table : tables) {
            List<Map<String, String>> columns = getColumns(table);
            String className = StringUtils.upperFirst(StringUtils.underlineToHump(table.getTableName()));
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("className", className);
            dataMap.put("tableName", table.getTableName());
            dataMap.put("tableComment", table.getTableComment());
            dataMap.put("columns", columns);
            File docFile = new File(CLASS_PATH + "/" + className + ".java");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            template.process(dataMap, writer);
            log.info("done");
        }
        close();
    }

    private static List<Table> getTableInfo() throws Exception {
        Table queries = new Table();
        queries.setTableSchema(TABLE_SCHEMA);
        if (!StringUtils.isBlank(TABLE_NAME)) {
            queries.setTableName(TABLE_NAME);
        }
        return MySQL.queryList(STATEMENT, Table.class, queries);
    }

    private static List<Map<String, String>> getColumns(Table table) throws Exception {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        Column queries = new Column();
        queries.setTableSchema(table.getTableSchema());
        queries.setTableName(table.getTableName());
        LinkedHashMap<String, Sort> sort = new LinkedHashMap<>();
        sort.put("ordinalPosition", Sort.ASC);
        List<Column> columns = MySQL.queryList(STATEMENT, Column.class, queries, sort);
        for (Column column : columns) {
            Map<String, String> map = new HashMap<>();
            map.put("comment", column.getColumnComment());
            map.put("name", StringUtils.lowerFirst(StringUtils.underlineToHump(column.getColumnName())));
            Class<?> type = TYPE_MAP.get(column.getDataType());
            map.put("type", type.getSimpleName());
            addValidAnnotation(type, column, map);
            list.add(map);
        }
        return list;
    }

    private static void addValidAnnotation(Class<?> type, Column column, Map<String, String> map) {
        String fieldName = StringUtils.lowerFirst(StringUtils.underlineToHump(column.getColumnName()));
        // 长度验证
        String maximumLength = column.getCharacterMaximumLength();
        if (!StringUtils.isBlank(maximumLength)) {
            String maximumLengthMessage = fieldName + "不能超过此长度：" + maximumLength;
            if (type.getTypeName().equals(String.class.getTypeName())) {
                String msg = String.format("@Length(max = %s, message = \"%s\")", maximumLength, maximumLengthMessage);
                map.put("lengthValid", msg);
            }
            if (type.getTypeName().equals(Long.class.getTypeName()) || type.getTypeName().equals(Integer.class.getTypeName())) {
                String msg = String.format("@Digits(integer = %s, message = \"%s\", fraction = 0)", maximumLength, maximumLengthMessage);
                map.put("lengthValid", msg);
            }
        }
        // 空验证
        if (!"YES".equalsIgnoreCase(column.getIsNullable())) {
            String nullableMessage = fieldName + "不能为空";
            String msg = String.format("@NotNull(message = \"%s\")", nullableMessage);
            map.put("nullableValid", msg);
        }
    }

    private static void close() {
        try {
            if (STATEMENT != null) {
                STATEMENT.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (CONNECTION != null) {
                CONNECTION.close();
            }
        } catch (SQLException ignored) {
        }
    }
}
