package org.litesoft.alleviative;

@SuppressWarnings("unused")
public class Characters extends AbstractCharacters {
  public Characters( char pReplacementChar, CharPredicate pCharacterMatcher ) {
    super( pReplacementChar, pCharacterMatcher );
  }

  public Characters( char c ) {
    super( c );
  }
}
