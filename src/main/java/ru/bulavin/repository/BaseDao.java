package ru.bulavin.repository;

import java.util.List;

public interface BaseDao<T> {
    boolean insert(T t);

    List<T> selectAll();
    T selectById(Long id);
    int delete(Long id);
    int update(T t);
}
