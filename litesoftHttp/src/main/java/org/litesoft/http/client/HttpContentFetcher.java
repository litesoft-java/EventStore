package org.litesoft.http.client;

import org.litesoft.content.ContentFetcher;
import org.litesoft.content.ContentResponse;

@SuppressWarnings("unused")
public class HttpContentFetcher<Request extends ToHttpURL> implements ContentFetcher<Request> {
  private final HttpGet<Request> mBodyFetcher;

  HttpContentFetcher() { // Package Private for in package inheritance!
    mBodyFetcher = HttpGet.instance();
  }

  public static <Request extends ToHttpURL> HttpContentFetcher<Request> instance() {
    return new HttpContentFetcher<>();
  }

  @Override
  public ContentResponse<Request> fetchResponse( Request pRequest ) {
    return mBodyFetcher.fetch( pRequest );
  }
}
