package org.litesoft.http.client;

import java.sql.Timestamp;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.litesoft.alleviative.Closeables;
import org.litesoft.content.ContentState;
import org.litesoft.http.client.support.AbstractResponse;

public class HeadResponse<Request extends ToHttpURL> extends AbstractResponse<Request> implements ContentState<Request> {
  private HeadResponse( Request pRequest, Timestamp pLastModified, int pStatusCode, String pError ) {
    super( pRequest, pLastModified, pStatusCode, pError );
  }

  @Override
  protected void cleanup() {
    // For HeadResponse nothing to clean up!
  }

  public static class Builder<Request extends ToHttpURL> extends AbstractBuilder<Request, HeadResponse<Request>, Builder<Request>> {
    private CloseableHttpResponse mResponse;

    @Override
    public Builder<Request> withCloseableHttpResponse( CloseableHttpResponse pCloseableHttpResponse ) {
      mResponse = pCloseableHttpResponse;
      return this;
    }

    @Override
    protected HeadResponse<Request> build( int pStatusCode, String pError ) {
      Closeables.closeQuietly( mResponse );
      return new HeadResponse<>( mRequest, mLastModified, pStatusCode, pError );
    }
  }
}
