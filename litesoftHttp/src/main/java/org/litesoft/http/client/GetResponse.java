package org.litesoft.http.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.litesoft.alleviative.AugmentedCloseInputStreamProxy;
import org.litesoft.alleviative.Closeables;
import org.litesoft.content.ContentResponse;
import org.litesoft.http.client.support.AbstractResponse;

@SuppressWarnings("WeakerAccess")
public class GetResponse<Request extends ToHttpURL> extends AbstractResponse<Request> implements ContentResponse<Request> {
  private final long mContentLength;
  private final InputStream mContentIS;

  private GetResponse( Request pRequest, Timestamp pLastModified, int pStatusCode, String pError, long pContentLength, InputStream pContentIS ) {
    super( pRequest, pLastModified, pStatusCode, pError );
    mContentLength = pContentLength;
    mContentIS = pContentIS;
  }

  private GetResponse( Request pRequest, Timestamp pLastModified, int pStatusCode, String pError ) {
    this( pRequest, pLastModified, pStatusCode, pError, NO_CONTENT, null );
  }

  @Override
  public long getContentLength() {
    return mContentLength;
  }

  @Override
  public InputStream getContentIS() {
    return mContentIS;
  }

  @Override
  protected void cleanup() {
    Closeables.closeQuietly( mContentIS );
  }

  public static class Builder<Request extends ToHttpURL> extends AbstractBuilder<Request, GetResponse<Request>, Builder<Request>> {
    private CloseableHttpResponse mResponse;
    private HttpEntity mEntity;

    @Override
    public Builder<Request> withCloseableHttpResponse( CloseableHttpResponse pCloseableHttpResponse ) {
      mResponse = pCloseableHttpResponse;
      try {
        mEntity = mResponse.getEntity(); // Response should NOT be null!
      }
      catch ( RuntimeException e ) {
        cleanup();
      }
      return this;
    }

    private void cleanup() {
      EntityUtils.consumeQuietly( mEntity );
      mEntity = null;
      Closeables.closeQuietly( mResponse );
      mResponse = null;
    }

    @Override
    protected GetResponse<Request> build( int pStatusCode, String pError ) {
      if ( pError != null ) {
        cleanup();
        return new GetResponse<>( mRequest, mLastModified, pStatusCode, pError );
      }
      if ( mEntity == null ) {
        cleanup();
        return new GetResponse<>( mRequest, mLastModified, 500, "No HttpResponse.Entity" );
      }
      InputStream zContentIS;
      long zContentLength;
      try {
        zContentLength = mEntity.getContentLength();
        zContentIS = mEntity.getContent();
      }
      catch ( IOException | RuntimeException e ) {
        e.printStackTrace();
        cleanup();
        return new GetResponse<>( mRequest, mLastModified, 500, "Unable to get Content info: " + e.getMessage() );
      }
      if ( zContentIS == null ) {
        cleanup();
        return new GetResponse<>( mRequest, mLastModified, 500, "No Content stream available" );
      }
      return new GetResponse<>( mRequest, mLastModified, pStatusCode, null, zContentLength,
                                new ProxyIS( mResponse, mEntity, zContentIS ) );
    }

    private static class ProxyIS extends AugmentedCloseInputStreamProxy {
      private final Closeable mResponse;
      private final HttpEntity mEntity;

      public ProxyIS( Closeable pResponse, HttpEntity pEntity, InputStream pProxied ) {
        super( pProxied );
        mResponse = pResponse;
        mEntity = pEntity;
      }

      @Override
      public void closing() {
        EntityUtils.consumeQuietly( mEntity );
        Closeables.closeQuietly( mResponse );
      }
    }
  }
}
