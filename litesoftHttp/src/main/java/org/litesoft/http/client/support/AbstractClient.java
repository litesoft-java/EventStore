package org.litesoft.http.client.support;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.litesoft.http.client.ToHttpURL;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractClient<Request extends ToHttpURL, Response extends AbstractResponse<Request>,
        Builder extends AbstractResponse.AbstractBuilder<Request, Response, Builder>> {

    public static final String LAST_MODIFIED_HEADER = "Last-Modified";

    private static final CloseableHttpClient sClient = HttpClients.createDefault();

    private final Supplier<Builder> mBuilderFactory;

    protected AbstractClient( Supplier<Builder> pBuilderFactory ) {
        mBuilderFactory = pBuilderFactory;
    }

    protected Response process( Request pRequest, HttpUriRequest request ) {
        // System.out.println( "Client.request: " + pRequest.toString() );
        // System.out.println( "Client.request: " + pRequest.toURL() );
        Builder zBuilder = mBuilderFactory.get().withRequest( pRequest );
        CloseableHttpResponse zResponse;
        try {
            zResponse = sClient.execute( request );
            if ( zResponse == null ) {
                return zBuilder.ofNoHttpResponse();
            }

            zBuilder.withCloseableHttpResponse( zResponse );

            Header zHeader = zResponse.getLastHeader( LAST_MODIFIED_HEADER );
            if ( zHeader != null ) {
                String zValue = zHeader.getValue();
                if ( zValue != null ) {
                    zBuilder = zBuilder.withLastModified( DateUtils.parseDate( zValue ) );
                }
            }

            StatusLine zStatusLine = zResponse.getStatusLine();
            if ( zStatusLine == null ) {
                return zBuilder.ofNoStatusLine();
            }

            int zStatusCode = zStatusLine.getStatusCode();
            if ( !zBuilder.acceptableStatusCode( zStatusCode ) ) {
                return zBuilder.ofOddStatus( zStatusCode );
            }

            return zBuilder.ok( zStatusCode );
        }
        catch ( IOException | RuntimeException e ) {
            return zBuilder.ofException( e );
        }
    }
}
