package org.litesoft.persisted;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface IPersistedObjectRepositoryId32<T extends IPersistedObjectId32> extends IPersistedObjectRepository<T> {
  /**
   * @param pId Nullable - note: null will be returned a null if passed in
   *
   * @return Nullable (null means not found)
   */
  T findById( Integer pId );

  @Override
  default T refresh( T pPO ) {
    return (pPO == null) ? null : findById( pPO.getId() );
  }
}
