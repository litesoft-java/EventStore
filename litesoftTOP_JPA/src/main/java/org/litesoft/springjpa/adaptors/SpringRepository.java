package org.litesoft.springjpa.adaptors;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface SpringRepository<T> {
  <S extends T> S save( S entity );

  void delete( T entity );

  List<T> findFirst( Pageable limit );

  List<T> findNext( Pageable limit, String after );
}
