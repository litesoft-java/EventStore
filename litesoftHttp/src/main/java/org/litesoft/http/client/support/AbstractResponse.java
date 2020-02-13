package org.litesoft.http.client.support;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.litesoft.alleviative.Cast;
import org.litesoft.content.ContentResponseStatus;
import org.litesoft.content.HasLastModified;
import org.litesoft.http.client.ToHttpURL;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractResponse<Request extends ToHttpURL> implements HasLastModified,
                                                                             ContentResponseStatus<Request>,
                                                                             ResponseErrorHandler {
  private final Request mRequest;
  private final Timestamp mLastModified;
  private final int mStatusCode;
  private final String mError;

  protected AbstractResponse( Request pRequest, Timestamp pLastModified, int pStatusCode, String pError ) {
    mRequest = pRequest;
    mLastModified = pLastModified;
    mStatusCode = pStatusCode;
    mError = pError;
  }

  @Override
  public Timestamp getLastModified() {
    return mLastModified;
  }

  @Override
  public Request request() {
    return mRequest;
  }

  @Override
  public boolean hasError() {
    return (mError != null);
  }

  @Override
  public int getStatusCode() {
    return mStatusCode;
  }

  @Override
  public String getError() {
    return mError;
  }

  @Override
  public String toString() {
    return (mRequest == null) ? null : mRequest.toString();
  }

  @Override
  public void errored( HttpServletResponse pResponse, int pStatusCode, String pError ) {
    try {
      errorReply( pResponse, pStatusCode, pError );
    }
    finally {
      cleanup();
    }
  }

  public static void errorReply( HttpServletResponse pResponse, int pStatusCode, String pError ) {
    if ( pResponse == null ) {
      throw new IllegalStateException( "No HttpServletResponse, but had processing error (with status code: " +
                                       pStatusCode + ") of: " + pError );
    }
    try {
      pResponse.setStatus( pStatusCode );
      pResponse.getWriter().println( pError );
    }
    catch ( IOException e ) {
      throw new RuntimeException( "Unable to set error state on HttpServletResponse, but had processing error (with status code: " +
                                  pStatusCode + ") of: " + pError, e );
    }
  }

  abstract protected void cleanup();

  @SuppressWarnings("UnusedReturnValue")
  public static abstract class AbstractBuilder<Request extends ToHttpURL, Response extends AbstractResponse, Builder extends AbstractBuilder<Request, Response, Builder>> {
    protected Request mRequest;
    protected Timestamp mLastModified;

    public Builder withRequest( Request pRequest ) {
      mRequest = pRequest;
      return us();
    }

    public Builder withLastModified( Date pLastModified ) {
      mLastModified = (pLastModified == null) ? null : new Timestamp( pLastModified.getTime() );
      return us();
    }

    abstract public Builder withCloseableHttpResponse( CloseableHttpResponse pCloseableHttpResponse );

    public Response ofException( Exception e ) {
      e.printStackTrace();
      return of500( e.getMessage() );
    }

    public Response ofNoHttpResponse() {
      return of500( "No HttpResponse" );
    }

    public Response ofNoStatusLine() {
      return of500( "No HttpResponse.StatusLine" );
    }

    public Response of500( String pError ) {
      return build( 500, normalizeError( pError ) );
    }

    public Response ofOddStatus( int pStatusCode ) {
      return build( pStatusCode, "Odd StatusCode: " + pStatusCode );
    }

    public Response ok( int pStatusCode ) {
      return build( pStatusCode, null );
    }

    public boolean acceptableStatusCode( int pStatusCode ) {
      return (200 <= pStatusCode) && (pStatusCode < 300);
    }

    abstract protected Response build( int pStatusCode, String pError );

    private Builder us() {
      return Cast.it( this );
    }

    private String normalizeError( String pError ) {
      if ( pError != null ) {
        pError = pError.trim();
        if ( !pError.isEmpty() ) {
          return pError;
        }
      }
      throw new IllegalStateException( "No Error provided" );
    }
  }
}
