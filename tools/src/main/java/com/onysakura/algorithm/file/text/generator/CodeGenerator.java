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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class CodeGenerator {

    private static final String URL = "127.0.0.1:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private static final String TABLE_SCHEMA = "test";
    private static final String TABLE_NAME = "test";

    private static final String TEMPLATE_PATH = "tools/src/main/resources/template";
    private static final String CLASS_PATH = "tools/src/main/resources/output";

    private static final Connection CONNECTION;
    private static final Statement STATEMENT;
    private static final HashMap<String, Class<?>> TYPE_MAP;

    private static Configuration configuration;

    static {
        String url = "jdbc:mysql://" + URL + "/information_schema?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&";
        CONNECTION = MySQL.getConnection(url, USER, PASSWORD);
        STATEMENT = MySQL.createStatement(CONNECTION);

        TYPE_MAP = new HashMap<>();
        TYPE_MAP.put("varchar", String.class);
        TYPE_MAP.put("bigint", Long.class);
        TYPE_MAP.put("tinyint", Integer.class);
        TYPE_MAP.put("int", Integer.class);
        TYPE_MAP.put("double", Double.class);
        TYPE_MAP.put("decimal", BigDecimal.class);
        TYPE_MAP.put("datetime", LocalDateTime.class);
        TYPE_MAP.put("date", LocalDateTime.class);
        TYPE_MAP.put("timestamp", LocalDateTime.class);
        TYPE_MAP.put("char", String.class);
        TYPE_MAP.put("text", String.class);
        TYPE_MAP.put("longtext", String.class);
        TYPE_MAP.put("longblob", Object.class);

        configuration = new Configuration(Configuration.VERSION_2_3_0);
        File file = new File(TEMPLATE_PATH);
        try {
            configuration.setDirectoryForTemplateLoading(file);
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) throws Exception {
        List<Table> tables = getTableInfo();
        List<TableInfo> list = getTableInfos(tables);
//        generateDoc(list);
//        generateEntityAndMapper(list);
        log.info("done");
        close();
    }

    public static void generateDoc(List<TableInfo> list) throws Exception {
        Template template = configuration.getTemplate("DatabaseDoc.ftl");
        File docFile = new File(CLASS_PATH + "/database_" + TABLE_SCHEMA + ".md");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
        HashMap<String, List<TableInfo>> map = new HashMap<>();
        map.put("tables", list);
        template.process(map, writer);
    }

    public static void generateEntityAndMapper(List<TableInfo> list) throws Exception {
        Template entityTemplate = configuration.getTemplate("Entity.ftl");
        Template mapperTemplate = configuration.getTemplate("Mapper.ftl");
        Template mapperXmlTemplate = configuration.getTemplate("MapperXml.ftl");
        for (TableInfo tableInfo : list) {
            String className = String.valueOf(tableInfo.getClassName());
            String entityPath = CLASS_PATH + "/" + className;
            new File(entityPath).mkdirs();
            String prefix = entityPath + "/" + className;
            File entityFile = new File(prefix + ".java");
            File mapperFile = new File(prefix + "Mapper.java");
            File mapperXmlFile = new File(prefix + "Mapper.xml");
            BufferedWriter entityWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(entityFile)));
            BufferedWriter mapperWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile)));
            BufferedWriter mapperXmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
            entityTemplate.process(tableInfo, entityWriter);
            mapperTemplate.process(tableInfo, mapperWriter);
            mapperXmlTemplate.process(tableInfo, mapperXmlWriter);
        }
    }

    private static List<TableInfo> getTableInfos(List<Table> tables) throws Exception {
        List<TableInfo> list = new ArrayList<>();
        for (Table table : tables) {
            List<ColumnInfo> columns = getColumns(table);
            String className = StringUtils.upperFirst(StringUtils.underlineToHump(table.getTableName()));
            TableInfo tableInfo = new TableInfo();
            tableInfo.setSchema(TABLE_SCHEMA);
            tableInfo.setTableName(table.getTableName());
            tableInfo.setTableComment(table.getTableComment());
            tableInfo.setClassName(className);
            tableInfo.setColumns(columns);
            list.add(tableInfo);
        }
        return list;
    }

    private static List<Table> getTableInfo() throws Exception {
        Table queries = new Table();
        queries.setTableSchema(TABLE_SCHEMA);
        if (!StringUtils.isBlank(TABLE_NAME)) {
            queries.setTableName(TABLE_NAME);
        }
        return MySQL.queryList(STATEMENT, Table.class, queries);
    }

    private static List<ColumnInfo> getColumns(Table table) throws Exception {
        List<ColumnInfo> list = new ArrayList<>();
        Column queries = new Column();
        queries.setTableSchema(table.getTableSchema());
        queries.setTableName(table.getTableName());
        LinkedHashMap<String, Sort> sort = new LinkedHashMap<>();
        sort.put("ordinalPosition", Sort.ASC);
        List<Column> columns = MySQL.queryList(STATEMENT, Column.class, queries, sort);
        for (Column column : columns) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setComment(column.getColumnComment());
            columnInfo.setColumnName(column.getColumnName());
            columnInfo.setName(StringUtils.lowerFirst(StringUtils.underlineToHump(column.getColumnName())));
            columnInfo.setDataType(column.getDataType());
            Class<?> type = TYPE_MAP.get(column.getDataType());
            if (type == null) {
                log.warn("unknown type: {}", column.getDataType());
            }
            columnInfo.setType(type.getSimpleName());
            addValidAnnotation(type, column, columnInfo);
            list.add(columnInfo);
        }
        return list;
    }

    private static void addValidAnnotation(Class<?> type, Column column, ColumnInfo columnInfo) {
        String fieldName = StringUtils.lowerFirst(StringUtils.underlineToHump(column.getColumnName()));
        // 长度验证
        String maximumLength = column.getCharacterMaximumLength();
        Check:
        if (!StringUtils.isBlank(maximumLength)) {
            columnInfo.setLength(maximumLength);
            String maximumLengthMessage = fieldName + "不能超过此长度：" + maximumLength;
            if (type.getTypeName().equals(String.class.getTypeName())) {
                String anno = String.format("@Length(max = %s, message = \"%s\")", maximumLength, maximumLengthMessage);
                columnInfo.setLengthValid(anno);
                break Check;
            }
            if (type.getTypeName().equals(Long.class.getTypeName()) || type.getTypeName().equals(Integer.class.getTypeName())) {
                String anno = String.format("@Digits(integer = %s, message = \"%s\", fraction = 0)", maximumLength, maximumLengthMessage);
                columnInfo.setLengthValid(anno);
                break Check;
            }
        }
        // 空验证
        if (!"YES".equalsIgnoreCase(column.getIsNullable())) {
            columnInfo.setNullable(column.getIsNullable());
            String nullableMessage = fieldName + "不能为空";
            String anno = String.format("@NotNull(message = \"%s\")", nullableMessage);
            columnInfo.setNullableValid(anno);
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
