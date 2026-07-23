package com.ephirious.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void add(T object);
    void removeById(ID id);
    void remove(T object);
    void update(T object);
}
