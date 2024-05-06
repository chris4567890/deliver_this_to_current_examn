package org.dao;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
//source for this code is one of my group projects cannot link as that gives name away
public interface IDAO<T,K> {
    List<T> getAll();

    T getById(K id);

    T getById(K in, @NotNull Consumer<T> initializer);

    void create(T in);

    T update(T in, K id);

    T delete(K id);
}
