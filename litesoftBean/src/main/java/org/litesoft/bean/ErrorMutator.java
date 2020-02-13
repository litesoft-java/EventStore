package org.litesoft.bean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings("unused")
public interface ErrorMutator<T extends ErrorMutator<T>> extends ErrorAccessor {
  void setError( String pError );

  default T withError( String pError ) {
    setError( pError );
    return Cast.it( this );
  }

  default T withError( ErrorAccessor pAccessor ) {
    return withError( ErrorAccessor.deNull( pAccessor ).getError() );
  }
}
