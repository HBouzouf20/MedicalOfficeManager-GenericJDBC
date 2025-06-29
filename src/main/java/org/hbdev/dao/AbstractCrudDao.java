package org.hbdev.dao;

import org.hbdev.annotations.Column;
import org.hbdev.annotations.Id;
import org.hbdev.annotations.Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Generic abstract DAO class that provides basic CRUD operations
 * using reflection and custom annotations to map entities to database tables.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public abstract class AbstractCrudDao<T, ID> implements CrudDao<T, ID> {

    /** Database connection instance */
    protected final Connection connection;

    /** Class type of the target entity */
    private final Class<T> clazz;

    /**
     * Constructor to initialize the DAO with the entity class type.
     *
     * @param clazz the class of the entity (e.g., User.class)
     */
    public AbstractCrudDao(Class<T> clazz) {
        this.clazz = clazz;
        this.connection = Database.getInstance().getConnection();
    }

    /**
     * Retrieves the table name from the @Table annotation on the entity class.
     *
     * @return the table name
     * @throws RuntimeException if the @Table annotation is missing
     */
    private String getTableName() {
        return Optional.ofNullable(clazz.getAnnotation(Table.class))
                .map(Table::name)
                .orElseThrow(() -> new RuntimeException("Missing @Table annotation on " + clazz.getName()));
    }

    /**
     * Retrieves the name of the ID column from the field annotated with @Id and @Column.
     *
     * @return the name of the ID column
     * @throws RuntimeException if no field with both @Id and @Column is found
     */
    private String getIdColumn() {
        return List.of(clazz.getDeclaredFields()).stream()
                .filter(field -> field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class))
                .findFirst()
                .map(field -> field.getAnnotation(Column.class).name())
                .orElseThrow(() -> new RuntimeException("No field with @Id and @Column found in " + clazz.getName()));
    }

    /**
     * Saves a new entity to the database.
     *
     * @param entity the entity to save
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void save(T entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getInsertQuery())) {
            setInsertStatement(stmt, entity);
            stmt.executeUpdate();
        }
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity
     * @return an Optional containing the entity if found, otherwise empty
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Optional<T> findById(ID id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
            }
        }
    }

    /**
     * Retrieves all entities from the database.
     *
     * @return a list of all entities
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<T> findAll() throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
        }
        return results;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void update(T entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getUpdateQuery())) {
            setUpdateStatement(stmt, entity);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an entity from the database by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void deleteById(ID id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Maps a ResultSet row to an entity instance using reflection.
     *
     * @param rs the ResultSet
     * @return the mapped entity
     * @throws SQLException if a mapping or database access error occurs
     */
    protected T mapResultSet(ResultSet rs) throws SQLException {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    String columnName = field.getAnnotation(Column.class).name();
                    field.setAccessible(true);
                    Object value = rs.getObject(columnName);

                    if (value != null) {
                        assignFieldValue(field, instance, value);
                    }
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map ResultSet to entity: " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Assigns a value to a field in the given instance based on the field's type.
     *
     * @param field    the field to set
     * @param instance the entity instance
     * @param value    the value to assign
     * @throws IllegalAccessException if the field cannot be accessed
     */
    private void assignFieldValue(Field field, T instance, Object value) throws IllegalAccessException {
        Class<?> type = field.getType();

        if (type == int.class) {
            field.setInt(instance, ((Number) value).intValue());
        } else if (type == long.class) {
            field.setLong(instance, ((Number) value).longValue());
        } else if (type == double.class) {
            field.setDouble(instance, ((Number) value).doubleValue());
        } else {
            field.set(instance, value);
        }
    }

    /**
     * Sets the parameters for the insert PreparedStatement.
     *
     * @param stmt   the PreparedStatement
     * @param entity the entity to insert
     * @throws SQLException if a database access error occurs
     */
    protected abstract void setInsertStatement(PreparedStatement stmt, T entity) throws SQLException;

    /**
     * Sets the parameters for the update PreparedStatement.
     *
     * @param stmt   the PreparedStatement
     * @param entity the entity to update
     * @throws SQLException if a database access error occurs
     */
    protected abstract void setUpdateStatement(PreparedStatement stmt, T entity) throws SQLException;

    /**
     * Retrieves the ID value of the given entity.
     *
     * @param entity the entity
     * @return the ID value
     */
    protected abstract ID getEntityId(T entity);

    /**
     * Returns the SQL insert query for the entity.
     *
     * @return the insert SQL query string
     */
    protected abstract String getInsertQuery();

    /**
     * Returns the SQL update query for the entity.
     *
     * @return the update SQL query string
     */
    protected abstract String getUpdateQuery();
}
