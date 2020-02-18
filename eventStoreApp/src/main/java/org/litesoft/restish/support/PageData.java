package org.litesoft.restish.support;

import java.util.Arrays;
import java.util.List;

public class PageData<T> {
    private final String mNextToken;
    private final List<T> mData;

    public PageData( List<T> pData, String pNextToken ) {
        mData = pData;
        mNextToken = pNextToken;
    }

    public PageData( List<T> pData ) {
        this( pData, null );
    }

    @SafeVarargs
    public PageData( T... pData ) {
        this( Arrays.asList( pData ) );
    }

    public String getNextToken() {
        return mNextToken;
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public String toString() {
        return "PageData{" +
               "mNextToken='" + mNextToken + '\'' +
               ", mData=" + mData +
               '}';
    }
}
