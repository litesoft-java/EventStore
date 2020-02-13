package org.litesoft.springjpa.adaptors;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface SpringRepository<T> {
  List<T> findFirst( Pageable limit );

  List<T> findNext( Pageable limit, String after );
}
