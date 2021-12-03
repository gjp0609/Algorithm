package fun.onysakura.algorithm.utils.db.sqlite;

import fun.onysakura.algorithm.utils.core.basic.idGenerator.IdUtils;
import fun.onysakura.algorithm.utils.core.basic.str.StringUtils;
import fun.onysakura.algorithm.utils.db.anno.TableName;
import fun.onysakura.algorithm.utils.db.enums.Sort;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("unused")
public class BaseRepository<T> {

    private final String tableName;
    private final String[] fieldNames;
    private final Class<T> modelClass;

    @SuppressWarnings({"unchecked"})
    public Class<T> getModelClass() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] pType = ((ParameterizedType) type).getActualTypeArguments();
            return (Class<T>) pType[0];
        } else {
            return null;
        }
    }

    public BaseRepository(Class<T> modelClass) {
        if (modelClass == null) {
            modelClass = getModelClass();
        }
        this.modelClass = modelClass;
        Field[] fields = modelClass.getDeclaredFields();
        fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = StringUtils.humpToUnderline(fields[i].getName());
        }
        TableName tableName = modelClass.getAnnotation(TableName.class);
        this.tableName = tableName.value();
        createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($CONTENT);";
        List<String> content = new ArrayList<>();
        for (String fieldName : fieldNames) {
            if ("ID".equalsIgnoreCase(fieldName)) {
                content.add("ID TEXT PRIMARY KEY NOT NULL");
            } else {
                content.add(fieldName + " TEXT");
            }
        }
        sql = sql.replace("$TABLE_NAME", this.tableName)
                .replace("$CONTENT", String.join(", ", content));
        log.debug("create table sql: " + sql);
        int i = SQLite.executeUpdate(sql);
        if (i >= 0) {
            log.debug("create table " + this.tableName + " successfully");
        } else {
            log.warn("create table fail");
        }
    }

    public List<T> selectAll() {
        String sql = "SELECT * FROM $TABLE_NAME;";
        sql = sql.replace("$TABLE_NAME", tableName);
        log.debug("select sql: " + sql);
        ResultSet resultSet = SQLite.executeQuery(sql);
        return getResultList(resultSet, modelClass);
    }

    public List<T> selectAll(LinkedHashMap<String, Sort> sort) {
        if (sort == null || sort.isEmpty()) {
            return selectAll();
        }
        String sql = "SELECT * FROM $TABLE_NAME ORDER BY $ORDER;";
        ArrayList<String> order = new ArrayList<>();
        for (Map.Entry<String, Sort> sortEntry : sort.entrySet()) {
            order.add(StringUtils.humpToUnderline(sortEntry.getKey()) + " " + sortEntry.getValue().toString());
        }
        sql = sql.replace("$TABLE_NAME", tableName)
                .replace("$ORDER", String.join(", ", order));
        log.debug("select sql: " + sql);
        ResultSet resultSet = SQLite.executeQuery(sql);
        return getResultList(resultSet, modelClass);
    }

    public List<T> select(T model) {
        List<String> queries = new ArrayList<>();
        boolean hasQueries = false;
        for (String fieldName : fieldNames) {
            try {
                Method method = modelClass.getDeclaredMethod(generateGetMethodName(fieldName));
                Object invoke = method.invoke(model);
                if (invoke != null) {
                    hasQueries = true;
                    queries.add(fieldName + " = '" + SQLite.escape(invoke.toString()) + "'");
                }
            } catch (ReflectiveOperationException e) {
                log.warn("select fail", e);
            }
        }
        if (!hasQueries) {
            return selectAll();
        }
        String sql = "SELECT * FROM $TABLE_NAME WHERE $QUERIES;";
        sql = sql.replace("$TABLE_NAME", tableName)
                .replace("$QUERIES", String.join(", ", queries));
        log.debug("select sql: " + sql);
        ResultSet resultSet = SQLite.executeQuery(sql);
        return getResultList(resultSet, modelClass);
    }

    public List<T> select(T model, LinkedHashMap<String, Sort> sort) {
        if (sort == null || sort.isEmpty()) {
            return select(model);
        }
        List<String> queries = new ArrayList<>();
        boolean hasQueries = false;
        for (String fieldName : fieldNames) {
            try {
                Method method = modelClass.getDeclaredMethod(generateGetMethodName(fieldName));
                Object invoke = method.invoke(model);
                if (invoke != null) {
                    hasQueries = true;
                    queries.add(fieldName + " = '" + SQLite.escape(invoke.toString()) + "'");
                }
            } catch (ReflectiveOperationException e) {
                log.warn("select fail", e);
            }
        }
        if (!hasQueries) {
            return selectAll(sort);
        }
        String sql = "SELECT * FROM $TABLE_NAME WHERE $QUERIES ORDER BY $ORDER;";
        ArrayList<String> order = new ArrayList<>();
        for (Map.Entry<String, Sort> sortEntry : sort.entrySet()) {
            order.add(StringUtils.humpToUnderline(sortEntry.getKey()) + " " + sortEntry.getValue().toString());
        }
        sql = sql.replace("$TABLE_NAME", tableName)
                .replace("$QUERIES", String.join(", ", queries))
                .replace("$ORDER", String.join(", ", order));
        log.debug("select sql: " + sql);
        ResultSet resultSet = SQLite.executeQuery(sql);
        return getResultList(resultSet, modelClass);
    }

    public T insert(T t) {
        String info = null;
        try {
            Method toString = t.getClass().getMethod("toString");
            Object invoke = toString.invoke(t);
            if (invoke != null) {
                info = invoke.toString();
            }
        } catch (Exception ignored) {
        }
        log.debug("insert " + (info == null ? t.getClass().getName() : info));
        String sql = "INSERT INTO $TABLE_NAME ($FIELDS) VALUES ($VALUES);";
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        try {
            Method setId = modelClass.getDeclaredMethod("setId", String.class);
            setId.invoke(t, String.valueOf(IdUtils.uid()));
            for (String fieldName : fieldNames) {
                fields.add(fieldName);
                String methodName = generateGetMethodName(fieldName);
                Method method = modelClass.getMethod(methodName);
                Object invoke = method.invoke(t);
                if (invoke == null) {
                    values.add("NULL");
                } else {
                    values.add("'" + SQLite.escape(invoke.toString()) + "'");
                }
            }
        } catch (Exception e) {
            log.warn("insert fail", e);
        }
        sql = sql.replace("$TABLE_NAME", tableName)
                .replace("$FIELDS", String.join(", ", fields))
                .replace("$VALUES", String.join(", ", values));
        log.debug("insert sql: " + sql);
        int update = SQLite.executeUpdate(sql);
        if (update >= 0) {
            return t;
        } else {
            return null;
        }
    }

    public T update(T t) {
        String info = null;
        try {
            Method toString = t.getClass().getMethod("toString");
            Object invoke = toString.invoke(t);
            if (invoke != null) {
                info = invoke.toString();
            }
        } catch (Exception ignored) {
        }
        log.debug("update " + (info == null ? t.getClass().getName() : info));
        String sql = "UPDATE $TABLE_NAME SET $VALUES WHERE ID = $ID;";
        List<String> values = new ArrayList<>();
        String id = null;
        try {
            Method setId = modelClass.getDeclaredMethod("getId");
            Object idObject = setId.invoke(t);
            if (idObject != null) {
                id = idObject.toString();
                for (String fieldName : fieldNames) {
                    String methodName = generateGetMethodName(fieldName);
                    Method method = modelClass.getMethod(methodName);
                    Object invoke = method.invoke(t);
                    String value;
                    if (invoke != null && !"ID".equalsIgnoreCase(fieldName)) {
                        value = invoke.toString();
                        values.add(fieldName + " = '" + SQLite.escape(value) + "'");
                    }
                }
            }
        } catch (Exception e) {
            log.warn("execute sql fail", e);
        }
        if (id != null) {
            sql = sql.replace("$TABLE_NAME", tableName)
                    .replace("$ID", id)
                    .replace("$VALUES", String.join(", ", values));
            log.debug("update sql: " + sql);
            int update = SQLite.executeUpdate(sql);
            if (update >= 0) {
                return t;
            } else {
                return null;
            }
        } else {
            log.warn("update fail, no id value");
            return null;
        }
    }

    public int delete(String id) {
        String sql = "DELETE FROM $TABLE_NAME WHERE ID = $ID;";
        sql = sql.replace("$TABLE_NAME", tableName)
                .replace("$ID", id);
        log.debug("delete sql: " + sql);
        return SQLite.executeUpdate(sql);
    }

    private List<T> getResultList(ResultSet resultSet, Class<T> modelClass) {
        List<T> list = new ArrayList<>();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    T t = modelClass.getDeclaredConstructor().newInstance();
                    for (String fieldName : fieldNames) {
                        String resultString = resultSet.getString(fieldName);
                        String setMethodName = generateSetMethodName(fieldName);
                        Method method = modelClass.getDeclaredMethod(setMethodName, String.class);
                        method.invoke(t, resultString);
                    }
                    list.add(t);
                }
            } catch (SQLException | ReflectiveOperationException e) {
                log.warn("get result list fail", e);
            }
        }
        log.debug("select result size: " + list.size());
        return list;
    }

    private String generateGetMethodName(String fieldName) {
        String name = StringUtils.underlineToHump(fieldName);
        char c = name.charAt(0);
        String firstChar = String.valueOf(c);
        return "get" + firstChar.toUpperCase() + name.substring(1);
    }

    private String generateSetMethodName(String fieldName) {
        String name = StringUtils.underlineToHump(fieldName);
        char c = name.charAt(0);
        String firstChar = String.valueOf(c);
        return "set" + firstChar.toUpperCase() + name.substring(1);
    }
}
