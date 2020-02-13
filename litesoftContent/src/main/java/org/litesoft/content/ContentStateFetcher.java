package org.litesoft.content;

@SuppressWarnings("unused")
public interface ContentStateFetcher<Key> {
  ContentState fetchState( Key pKey );
}
