package org.litesoft.springjpa.adaptors;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface SpringRepositoryId32<T> extends SpringRepository<T>,
                                                 CrudRepository<T, Integer> {
  @Override
  <S extends T> S save( S var1 );

  @Override
  Optional<T> findById( Integer pId );
}
