package org.litesoft.springjpa.adaptors;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringRepositoryId64<T> extends SpringRepository<T>,
                                                 CrudRepository<T, Long> {
    @Override
    <S extends T> S save( S entity );

    @Override
    void delete( T entity );

    @Override
    Optional<T> findById( Long pId );
}
