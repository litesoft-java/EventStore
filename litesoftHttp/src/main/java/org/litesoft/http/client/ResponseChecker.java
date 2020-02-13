package org.litesoft.http.client;

import javax.servlet.http.HttpServletResponse;

import org.litesoft.content.ContentResponseStatus;
import org.litesoft.http.client.support.AbstractResponse;
import org.litesoft.http.client.support.ResponseErrorHandler;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class ResponseChecker {
  public static boolean errored( Object pNullMeans404, String pWhat, ContentResponseStatus pStatus, HttpServletResponse pResponse ) {
    if ( (pNullMeans404 != null) || (pStatus != null) ) {
      return errored( pStatus, pResponse );
    }
    AbstractResponse.errorReply( pResponse, 404, pWhat + " Not Found" );
    return true;
  }

  public static boolean errored( ContentResponseStatus pStatus, HttpServletResponse pResponse ) {
    if ( pStatus != null ) {
      if ( !pStatus.hasError() ) {
        return false;
      }
      int zStatusCode = pStatus.getStatusCode();
      String zError = pStatus.getError();
      if ( pStatus instanceof ResponseErrorHandler ) {
        ((ResponseErrorHandler)pStatus).errored( pResponse, zStatusCode, zError );
      } else {
        AbstractResponse.errorReply( pResponse, zStatusCode, zError );
      }
      return true;
    }
    AbstractResponse.errorReply( pResponse, 500, "No ContentResponseStatus" );
    return true;
  }
}
