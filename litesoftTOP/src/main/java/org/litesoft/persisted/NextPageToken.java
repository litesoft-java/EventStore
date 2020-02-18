package org.litesoft.persisted;

public class NextPageToken {
    private final String mEncodedToken;

    public NextPageToken( String pEncodedToken ) {
        mEncodedToken = pEncodedToken;
    }

    public String getEncodedToken() {
        return mEncodedToken;
    }
}
