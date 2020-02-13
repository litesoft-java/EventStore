package org.litesoft.http.endpoints;

public interface DefaultPermissionsPathMetaData extends PathMetaData {
  /**
   * Cant NOT be added to a non-Default Role - Will throw an Exception!
   *
   * @return null means default Permissions!
   */
  @Override
  default String specialPermissionsPath() {
    return null;
  }
}
