package org.litesoft.persisted;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface IPersistedObjectRepository<T extends IPersistedObject> {

  Class<T> getPoType();

  /**
   * @param pPO !null
   *
   * @return updated PO (w/ ID & Version)
   *
   * @throws PersistorConstraintViolationException if the underlying store has a Constraint Violation
   */
  T insert( T pPO )
          throws PersistorConstraintViolationException;

  /**
   * @param pPO !null
   *
   * @return updated PO (w/ updated Version)
   *
   * @throws PersistorConstraintViolationException if the underlying store has a Constraint Violation
   */
  T update( T pPO )
          throws PersistorConstraintViolationException;

  /**
   * @param pPO Nullable - note: null will be returned if a null is passed in
   *
   * @return Nullable (null means not found: null ID or instance has been deleted)
   */
  T refresh( T pPO );

  /**
   * Load the First Page with a maximum if <code>pLimit</code> results.
   *
   * @param pLimit if <code>pLimit</code> is not between (inclusive) 1 and 10000, it is treated as 1.
   *
   * @return !null (if the contained <code>NextPageToken</code> is null, then there are no more pages).
   */
  Page<T> firstPage( int pLimit );

  /**
   * Load the Next Page with a maximum if <code>pLimit</code> results.
   *
   * @param pNextPageToken !null (see @throws)
   * @param pLimit         if <code>pLimit</code> is not between (inclusive) 1 and 10000, it is treated as 1.
   *
   * @return !null (if the contained <code>NextPageToken</code> is null, then there are no more pages).
   *
   * @throws IllegalArgumentException if the <code>pNextPageToken</code> is null
   */
  Page<T> nextPage( NextPageToken pNextPageToken, int pLimit )
          throws IllegalArgumentException;
}
