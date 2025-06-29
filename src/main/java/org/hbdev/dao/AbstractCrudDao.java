package org.hbdev.dao;
import org.hbdev.annotations.Column;
import org.hbdev.annotations.Id;
import org.hbdev.annotations.Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudDao<T, ID> implements CrudDao<T, ID>{
        protected Connection connection;

        public AbstractCrudDao() {
            this.connection = Database.getInstance().getConnection();
        }
        private Class<T> clazz = (Class<T>) ((Object) this).getClass().getSuperclass();
        private String getTableName() {
            if (clazz.isAnnotationPresent(Table.class)) {
                return clazz.getAnnotation(Table.class).name();
            }
            throw new RuntimeException("Missing @Table annotation on " + clazz.getName());
        }
        private String getIdColumn() {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
            }
            throw new RuntimeException("No field with @Id and @Column annotations found in " + clazz.getName());
        }

        protected abstract void setInsertStatement(PreparedStatement stmt, T entity) throws SQLException;
        protected abstract void setUpdateStatement(PreparedStatement stmt, T entity) throws SQLException;
        protected abstract ID getEntityId(T entity);

        protected T mapResultSet(ResultSet rs) throws SQLException {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Column.class)) {
                        String columnName = field.getAnnotation(Column.class).name();
                        field.setAccessible(true);

                        Object value = rs.getObject(columnName);

                        // Handle primitives separately
                        if (value != null) {
                            if (field.getType() == int.class) {
                                field.setInt(instance, ((Number) value).intValue());
                            } else if (field.getType() == long.class) {
                                field.setLong(instance, ((Number) value).longValue());
                            } else if (field.getType() == double.class) {
                                field.setDouble(instance, ((Number) value).doubleValue());
                            } else {
                                field.set(instance, value);
                            }
                        }
                    }
                }

                return instance;
            } catch (Exception e) {
                throw new RuntimeException("Failed to map ResultSet to entity: " + clazz.getSimpleName(), e);
            }
        }

    @Override
        public void save(T entity) throws SQLException {
            try (PreparedStatement stmt = connection.prepareStatement(getInsertQuery())) {
                setInsertStatement(stmt, entity);
                stmt.executeUpdate();
            }
        }

        @Override
        public Optional<T> findById(ID id) throws SQLException {
            String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setObject(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
            return Optional.empty();
        }

        @Override
        public List<T> findAll() throws SQLException {
            List<T> list = new ArrayList<>();
            String sql = "SELECT * FROM " + getTableName();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
            return list;
        }

        @Override
        public void update(T entity) throws SQLException {
            try (PreparedStatement stmt = connection.prepareStatement(getUpdateQuery())) {
                setUpdateStatement(stmt, entity);
                stmt.executeUpdate();
            }
        }

        @Override
        public void deleteById(ID id) throws SQLException {
            String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setObject(1, id);
                stmt.executeUpdate();
            }
        }

        // These are to be implemented in concrete DAO to reflect table structure
        protected abstract String getInsertQuery();
        protected abstract String getUpdateQuery();


}
