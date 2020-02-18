package org.litesoft.persisted;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Page<T extends IPersistedObject> {
    private final List<T> mImmutablePOs;
    private final NextPageToken mNextPageToken;

    public Page( List<T> pEffectivelyImmutablePOs, NextPageToken pNextPageToken ) {
        mImmutablePOs = pEffectivelyImmutablePOs;
        mNextPageToken = pNextPageToken;
    }

    public Page( List<T> pEffectivelyImmutablePOs ) {
        this( pEffectivelyImmutablePOs, null );
    }

    public List<T> getPOs() {
        return mImmutablePOs;
    }

    public NextPageToken getNextPageToken() {
        return mNextPageToken;
    }

    public int size() {
        return mImmutablePOs.size();
    }

    public boolean isEmpty() {
        return mImmutablePOs.isEmpty();
    }

    public boolean anotherPageExisted() {
        return (null != mNextPageToken);
    }

    public static <DT extends IPersistedObject> Page<DT> empty() {
        return new Page<>( Collections.emptyList() );
    }

    public static <DT extends IPersistedObject> Page<DT> deNull( Page<DT> pInstance ) {
        return (pInstance != null) ? pInstance : empty();
    }
}
