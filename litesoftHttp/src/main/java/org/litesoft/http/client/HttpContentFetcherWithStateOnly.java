package org.litesoft.http.client;

import org.litesoft.content.ContentFetcherWithStateOnly;
import org.litesoft.content.ContentState;

@SuppressWarnings("unused")
public class HttpContentFetcherWithStateOnly<Request extends ToHttpURL> extends HttpContentFetcher<Request> implements ContentFetcherWithStateOnly<Request> {
  private final HttpHead<Request> mHeadFetcher;

  private HttpContentFetcherWithStateOnly() {
    mHeadFetcher = HttpHead.instance();
  }

  public static <Request extends ToHttpURL> HttpContentFetcherWithStateOnly<Request> instance() {
    return new HttpContentFetcherWithStateOnly<>();
  }

  @Override
  public ContentState<Request> fetchState( Request pRequest ) {
    return mHeadFetcher.fetch( pRequest );
  }
}
