package org.litesoft.http.client;

import org.litesoft.http.client.support.AbstractClient;

@SuppressWarnings("unused")
public class HttpHead<Request extends ToHttpURL> extends AbstractClient<Request, HeadResponse<Request>, HeadResponse.Builder<Request>> {
  private HttpHead() {
    super( HeadResponse.Builder::new );
  }

  public static <Request extends ToHttpURL> HttpHead<Request> instance() {
    return new HttpHead<>();
  }

  public HeadResponse<Request> fetch( Request pToURL ) {
    return process( pToURL,
                    new org.apache.http.client.methods.HttpHead( pToURL.toURL() ) );
  }
}
