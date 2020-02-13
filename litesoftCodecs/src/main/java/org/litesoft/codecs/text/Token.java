package org.litesoft.codecs.text;

public abstract class Token extends StringCodecs {
  private static final String SHORT_NAME = "TKN";

  /**
   * @param pVersion Positive!
   */
  protected Token( boolean pRegister, int pVersion ) {
    super( pRegister, SHORT_NAME, pVersion );
  }
}
