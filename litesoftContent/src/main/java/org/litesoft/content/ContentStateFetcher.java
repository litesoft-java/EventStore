package org.litesoft.content;

@SuppressWarnings({"unused", "rawtypes"})
public interface ContentStateFetcher<Key> {
    ContentState fetchState( Key pKey );
}
