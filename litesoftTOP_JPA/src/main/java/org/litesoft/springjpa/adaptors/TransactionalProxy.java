package org.litesoft.springjpa.adaptors;

import java.util.function.Supplier;
import javax.transaction.Transactional;

public class TransactionalProxy {
  @Transactional
  public <R> R execute( Supplier<R> pSupplier ) {
    return pSupplier.get();
  }

  @Transactional
  public void execute( Runnable pRunnable ) {
    pRunnable.run();
  }
}
