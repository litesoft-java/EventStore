package org.litesoft.restish.support.exceptions;

@SuppressWarnings("unused")
public class RestishBadParamException extends RestishException {
  public RestishBadParamException() {
    this( "" );
  }

  public RestishBadParamException( String pMessageExtension ) {
    this( 404, "Error - bad input parameter" + pMessageExtension );
  }

  public RestishBadParamException( int pStatusCode, String message ) {
    super( pStatusCode, message );
  }
}
