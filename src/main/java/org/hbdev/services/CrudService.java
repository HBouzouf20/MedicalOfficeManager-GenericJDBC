package org.hbdev.services;

import java.util.List;
/**
 * Generic CRUD Service
 */
public interface CrudService<T, ID> {
    T create(T object);
    List<T> getAll();
    T getById(ID id);
    T update(ID id, T object);
    boolean delete(T object);
}
