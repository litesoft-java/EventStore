package org.litesoft.codecs;

@SuppressWarnings("unused")
public interface Codec {
    char SHORT_NAME_VERSION_SEP = ':';

    String encodedMethodVersionPrefix();

    /**
     * A Short Name which must start with a 7 Bit Ascii Letter, followed by a few 7 Bit Ascii Letters or Digits!
     *
     * @return !Null
     */
    String shortName();

    /**
     * Version Number - Positive!
     *
     * @return Positive integer
     */
    int version();
}
