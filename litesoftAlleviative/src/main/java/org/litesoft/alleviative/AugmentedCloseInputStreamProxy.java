package org.litesoft.alleviative;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public abstract class AugmentedCloseInputStreamProxy extends InputStream {
  private final InputStream mProxied;
  private boolean mClosed;

  protected AugmentedCloseInputStreamProxy( InputStream pProxied ) {
    mProxied = pProxied;
  }

  @Override
  public int read()
          throws IOException {
    return mProxied.read();
  }

  @Override
  public int read( byte[] b )
          throws IOException {
    return mProxied.read( b );
  }

  @Override
  public int read( byte[] b, int off, int len )
          throws IOException {
    return mProxied.read( b, off, len );
  }

  @Override
  public int available()
          throws IOException {
    return mProxied.available();
  }

  @Override
  public void close()
          throws IOException {
    if ( !mClosed ) {
      mClosed = true;
      Closeables.closeQuietly( mProxied );
      try {
        closing();
      }
      catch ( IOException | RuntimeException e ) {
        e.printStackTrace();
      }
    }
    super.close();
  }

  @Override
  protected void finalize()
          throws Throwable {
    if ( !mClosed ) {
      close();
    }
    super.finalize();
  }

  abstract protected void closing()
          throws IOException;
}

