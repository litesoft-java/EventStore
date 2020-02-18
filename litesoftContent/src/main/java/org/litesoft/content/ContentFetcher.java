package org.litesoft.content;

/**
 * Interface to isolate accessing the "content" files (usually via <code>ToHttpURL</code> as the <code>Key</code> (or local files if mocked).
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface ContentFetcher<Key> {
    default void clearCaches() {
        // No Op
    }

    ContentResponse fetchResponse( Key pKey );
}
