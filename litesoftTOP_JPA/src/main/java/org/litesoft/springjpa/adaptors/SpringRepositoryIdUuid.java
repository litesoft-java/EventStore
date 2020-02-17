package org.litesoft.springjpa.adaptors;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringRepositoryIdUuid<T> extends SpringRepository<T>,
                                                   CrudRepository<T, String> {
  @Override
  <S extends T> S save( S entity );

  @Override
  void delete( T entity );

  @Override
  Optional<T> findById( String pId );
}
