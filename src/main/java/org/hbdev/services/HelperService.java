package org.hbdev.services;

/**
 * Helper Generic service
 */
public interface HelperService<T> {
    void printInfo(T object);
    boolean exist(T object);
    int count();
}
