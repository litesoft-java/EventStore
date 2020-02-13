package org.litesoft.content;

@SuppressWarnings("unused")
public interface ContentFetcherWithStateOnly<Key> extends ContentFetcher<Key>,
                                                          ContentStateFetcher<Key> {
}
