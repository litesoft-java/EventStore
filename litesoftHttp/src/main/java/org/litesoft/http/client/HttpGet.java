package org.litesoft.http.client;

import org.litesoft.http.client.support.AbstractClient;

@SuppressWarnings("unused")
public class HttpGet<Request extends ToHttpURL> extends AbstractClient<Request, GetResponse<Request>, GetResponse.Builder<Request>> {
    private HttpGet() {
        super( GetResponse.Builder::new );
    }

    public static <Request extends ToHttpURL> HttpGet<Request> instance() {
        return new HttpGet<>();
    }

    public GetResponse<Request> fetch( Request pToURL ) {
        return process( pToURL, new org.apache.http.client.methods.HttpGet( pToURL.toURL() ) );
    }
}
