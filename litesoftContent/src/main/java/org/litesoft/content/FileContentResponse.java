package org.litesoft.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import org.litesoft.alleviative.AugmentedCloseInputStreamProxy;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FileContentResponse implements ContentResponse<String> {
  private static final int MAX_OK_STATUS_CODE = (OK_STATUS_CODE + STATUS_CODE_GROUP_SIZE) - 1;

  private final File mFile;
  private final Timestamp mLastModified;
  private final boolean mDeleteFileOnClose;
  private final String mError;
  private final int mStatusCode;

  protected FileContentResponse( File pFile, Timestamp pLastModified, boolean pDeleteFileOnClose, String pError, int pStatusCode ) {
    mFile = pFile;
    mLastModified = pLastModified;
    mDeleteFileOnClose = pDeleteFileOnClose;
    mError = pError;
    mStatusCode = pStatusCode;
  }

  public static FileContentResponse withFile( File pFile, Timestamp pLastModified, boolean pDeleteFileOnClose ) {
    return (pFile == null) ? withErrorNoFile() :
           new FileContentResponse( pFile, pLastModified, pDeleteFileOnClose, null, OK_STATUS_CODE );
  }

  public static FileContentResponse withFile( File pFile, boolean pDeleteFileOnClose ) {
    return withFile( pFile, null, pDeleteFileOnClose );
  }

  public static FileContentResponse withFile( File pFile, Timestamp pLastModified ) {
    return withFile( pFile, pLastModified, false );
  }

  public static FileContentResponse withFile( File pFile ) {
    return withFile( pFile, null );
  }

  public static FileContentResponse withError( String pError, int pStatusCode ) {
    pError = (pError == null) ? "" : pError.trim();
    if ( (OK_STATUS_CODE <= pStatusCode) && (pStatusCode <= MAX_OK_STATUS_CODE) ) {
      throw new IllegalArgumentException( "2xx Status code not appropriate for Error(s): " + pError );
    }
    if ( pError.isEmpty() ) {
      throw new IllegalArgumentException( "Status Code " + pStatusCode + ", but Error was Empty" );
    }
    return new FileContentResponse( null, null, false, pError, pStatusCode );
  }

  public static FileContentResponse withErrorNoFile() {
    return withError( "No File", NOT_FOUND_STATUS_CODE );
  }

  public static FileContentResponse withError( String pError ) {
    return withError( pError, SERVER_ERROR_STATUS_CODE );
  }

  public File getFile() {
    return mFile;
  }

  @Override
  public String request() {
    return "File: " + mFile;
  }

  @Override
  public boolean hasError() {
    return (mError != null);
  }

  @Override
  public String getError() {
    return mError;
  }

  @Override
  public int getStatusCode() {
    return mStatusCode;
  }

  @Override
  public Timestamp getLastModified() {
    return (mLastModified != null) ? mLastModified :
           ((mFile == null) ? null : new Timestamp( mFile.lastModified() ));
  }

  @Override
  public long getContentLength() {
    return (mFile == null) ? NO_CONTENT : mFile.length();
  }

  @Override
  public InputStream getContentIS() {
    if ( mFile != null ) {
      try {
        InputStream zInputStream = new FileInputStream( mFile );
        return !mDeleteFileOnClose ? zInputStream : new ProxyIS( mFile, zInputStream );
      }
      catch ( IOException e ) {
        throw new RuntimeException( e );
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return request();
  }

  private static class ProxyIS extends AugmentedCloseInputStreamProxy {
    private final File mFile;

    public ProxyIS( File pFile, InputStream pProxied ) {
      super( pProxied );
      mFile = pFile;
    }

    @Override
    public void closing()
            throws IOException {
      if ( !mFile.delete() && mFile.exists() ) {
        throw new IOException( "Unable to delete: " + mFile );
      }
    }
  }
}
