package fun.onysakura.algorithm.utils.db.mysql;

import fun.onysakura.algorithm.utils.core.basic.str.StringUtils;
import fun.onysakura.algorithm.utils.db.anno.ColumnName;
import fun.onysakura.algorithm.utils.db.anno.TableName;
import fun.onysakura.algorithm.utils.db.enums.Sort;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MySQL {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            log.warn("加载MySQL驱动失败", e);
        }
    }

    public static Connection getConnection(String url, String user, String pwd) {
        try {
            return DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            log.warn("创建连接失败", e);
        }
        return null;
    }

    public static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (Exception e) {
            log.warn("创建连接失败", e);
        }
        return null;
    }

    public static <Entity> List<Entity> queryList(Statement statement, Class<Entity> entityClass) throws Exception {
        return queryList(statement, entityClass, null, null);
    }

    public static <Entity> List<Entity> queryList(Statement statement, Class<Entity> entityClass, LinkedHashMap<String, Sort> sort) throws Exception {
        return queryList(statement, entityClass, null, sort);
    }

    public static <Entity> List<Entity> queryList(Statement statement, Class<Entity> entityClass, Entity queries) throws Exception {
        return queryList(statement, entityClass, queries, null);
    }

    public static <Entity> List<Entity> queryList(Statement statement, Class<Entity> entityClass, Entity queries, LinkedHashMap<String, Sort> sort) throws Exception {
        String sql = "SELECT $COLUMNS FROM `$TABLE`";
        List<FieldDef> fieldDefs = getTableColumns(entityClass);
        String columns = fieldDefs.stream().map(FieldDef::getColumnName).collect(Collectors.joining(", "));
        sql = sql.replace("$COLUMNS", columns);
        sql = sql.replace("$TABLE", getTableName(entityClass));
        if (queries != null) {
            sql += getQueries(fieldDefs, queries);
        }
        if (sort != null && !sort.isEmpty()) {
            sql += getSort(fieldDefs, sort);
        }
        log.debug("sql: {}", sql);
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Entity> list = new ArrayList<>();
        while (resultSet.next()) {
            Entity entity = entityClass.getDeclaredConstructor().newInstance();
            for (int i = 0; i < fieldDefs.size(); i++) {
                FieldDef fieldDef = fieldDefs.get(i);
                setValue(entity, fieldDef, resultSet, i + 1);
            }
            list.add(entity);
        }
        return list;
    }

    public static String getSort(List<FieldDef> fieldDefs, LinkedHashMap<String, Sort> sort) {
        ArrayList<String> sorts = new ArrayList<>();
        for (Map.Entry<String, Sort> sortEntry : sort.entrySet()) {
            for (FieldDef fieldDef : fieldDefs) {
                if (fieldDef.getName().equals(sortEntry.getKey()) || fieldDef.getColumnName().equals(sortEntry.getKey())) {
                    sorts.add(fieldDef.getColumnName() + " " + sortEntry.getValue());
                    break;
                }
            }
        }
        return "ORDER BY " + String.join(", ", sorts);
    }

    private static <Entity> String getQueries(List<FieldDef> fieldDefs, Entity queries) throws Exception {
        ArrayList<String> queryString = new ArrayList<>();
        for (FieldDef fieldDef : fieldDefs) {
            String getMethodName = "get" + fieldDef.getName().substring(0, 1).toUpperCase() + fieldDef.getName().substring(1);
            Method getMethod = queries.getClass().getMethod(getMethodName);
            Object value = getMethod.invoke(queries);
            if (value != null) {
                queryString.add(fieldDef.getColumnName() + " = '" + value + "'");
            }
        }
        return queryString.isEmpty() ? "" : " WHERE " + String.join(" and ", queryString);
    }

    private static <Entity> String getTableName(Class<Entity> entityClass) {
        String tableName;
        TableName annotation = entityClass.getAnnotation(TableName.class);
        if (annotation != null) {
            tableName = annotation.value();
        } else {
            tableName = StringUtils.humpToUnderline(entityClass.getName()).toLowerCase();
        }
        return tableName;
    }

    private static <Entity> List<FieldDef> getTableColumns(Class<Entity> entityClass) {
        ArrayList<FieldDef> fieldDefs = new ArrayList<>();
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ColumnName annotation = declaredField.getAnnotation(ColumnName.class);
            if (annotation != null) {
                String columnName = annotation.value();
                FieldDef fieldDef = new FieldDef();
                fieldDef.setName(declaredField.getName());
                fieldDef.setType(declaredField.getType());
                fieldDef.setColumnName(columnName);
                fieldDefs.add(fieldDef);
            }
        }
        return fieldDefs;
    }

    private static <Entity> void setValue(Entity entity, FieldDef fieldDef, ResultSet resultSet, int index) throws Exception {
        String setMethodName = "set" + fieldDef.getName().substring(0, 1).toUpperCase() + fieldDef.getName().substring(1);
        Method setMethod = entity.getClass().getMethod(setMethodName, fieldDef.getType());
        Class<?> type = fieldDef.getType();
        if (type.getTypeName().equals(String.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getString(index));
        } else if (type.getTypeName().equals(Long.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getLong(index));
        } else if (type.getTypeName().equals(Integer.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getInt(index));
        } else if (type.getTypeName().equals(Date.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getDate(index));
        } else if (type.getTypeName().equals(LocalDateTime.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getDate(index).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else if (type.getTypeName().equals(BigDecimal.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getBigDecimal(index));
        } else if (type.getTypeName().equals(Boolean.class.getTypeName())) {
            setMethod.invoke(entity, resultSet.getBoolean(index));
        } else {
            setMethod.invoke(entity, resultSet.getObject(index));
        }
    }

}
