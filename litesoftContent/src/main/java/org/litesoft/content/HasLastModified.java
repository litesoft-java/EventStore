package org.litesoft.content;

import java.sql.Timestamp;

public interface HasLastModified {
  /**
   * @return nullable
   */
  Timestamp getLastModified();
}
