package org.litesoft.alleviative;

import java.util.Objects;

import org.litesoft.alleviative.validation.NotNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractCharacters implements CharPredicate {
    private final CharPredicate mCharacterMatcher;
    private final char mReplacementChar;

    protected AbstractCharacters( char pReplacementChar, CharPredicate pCharacterMatcher ) {
        mReplacementChar = pReplacementChar;
        mCharacterMatcher = Objects.requireNonNull( pCharacterMatcher, "No CharacterMatcher" );
    }

    protected AbstractCharacters( char pReplacementAndMatchingChar ) {
        this( pReplacementAndMatchingChar, value -> (value == pReplacementAndMatchingChar) );
    }

    @Override
    public boolean test( char c ) {
        return mCharacterMatcher.test( c );
    }

    public boolean in( CharSequence pSequence ) {
        return anyCharacters( pSequence ) && (-1 != nextIn( pSequence, 0 ));
    }

    public int findIn( CharSequence pSequence ) {
        return findNextIn( pSequence, 0 );
    }

    public int findNextIn( CharSequence pSequence, int pFrom ) {
        return (anyCharacters( pSequence ) && (0 <= pFrom)) ? nextIn( pSequence, pFrom ) : -1;
    }

    public boolean starts( CharSequence pSequence ) {
        return anyCharacters( pSequence ) && test( pSequence.charAt( 0 ) ); // Left to Right!
    }

    public boolean ends( CharSequence pSequence ) {
        return anyCharacters( pSequence ) && test( pSequence.charAt( pSequence.length() - 1 ) ); // Left to Right!
    }

    /**
     * Remove all matching characters from passed <code>String</code> (a <code>null</code> passed-in becomes <code>""</code>).
     *
     * @param pString Nullable
     * @return !null
     */
    public String removeAllFrom( String pString ) {
        pString = deNull( pString );
        int zAt = nextIn( pString, 0 );
        return (zAt == -1) ? pString : removeAllFrom( zAt, new StringBuilder( pString ) ).toString();
    }

    /**
     * Remove leading and trailing matching characters AND replace all remaining matching character sequences with a single replacement character from passed <code>String</code> (a <code>null</code> passed-in becomes <code>""</code>).
     *
     * @param pString Nullable
     * @return !null
     */
    public String replaceLeadingTrailingAndAllInnerWithSingleFrom( String pString ) {
        pString = deNull( pString );
        if ( !in( pString ) ) {
            return pString;
        }
        StringBuilder zSB = new StringBuilder( pString );
        if ( starts( zSB ) ) {
            removeLeadingFrom( zSB );
        }
        if ( ends( zSB ) ) {
            removeTrailingFrom( zSB );
        }
        int zAt = findIn( pString );
        if ( zAt != -1 ) {
            replaceAllInnerWithSingleFrom( zAt, zSB );
        }
        return zSB.toString();
    }

    public String removeLeadingFrom( String pString ) {
        return !starts( pString ) ? deNull( pString ) : removeLeadingFrom( new StringBuilder( pString ) ).toString();
    }

    public String removeTrailingFrom( String pString ) {
        return !ends( pString ) ? deNull( pString ) : removeTrailingFrom( new StringBuilder( pString ) ).toString();
    }

    public String removeLeadingAndTrailingFrom( String pString ) {
        if ( !starts( pString ) ) {
            return removeTrailingFrom( pString );
        }
        StringBuilder zSB = removeLeadingFrom( new StringBuilder( pString ) );
        if ( ends( zSB ) ) {
            removeTrailingFrom( zSB );
        }
        return zSB.toString();
    }

    private String deNull( String pString ) {
        return NotNull.or( pString, "" );
    }

    private boolean anyCharacters( CharSequence pSequence ) {
        return (pSequence != null) && (pSequence.length() != 0);
    }

    private int nextIn( CharSequence pSequence, int pFrom ) {
        for ( int i = pFrom; i < pSequence.length(); i++ ) {
            if ( test( pSequence.charAt( i ) ) ) {
                return i;
            }
        }
        return -1;
    }

    private StringBuilder removeLeadingFrom( StringBuilder pSB ) {
        do {
            pSB.deleteCharAt( 0 );
        } while ( starts( pSB ) );
        return pSB;
    }

    private StringBuilder removeTrailingFrom( StringBuilder pSB ) {
        do {
            pSB.deleteCharAt( pSB.length() - 1 );
        } while ( ends( pSB ) );
        return pSB;
    }

    private StringBuilder removeAllFrom( int i, StringBuilder pSB ) {
        do {
            pSB.deleteCharAt( i );
        } while ( -1 != (i = findNextIn( pSB, i )) );
        return pSB;
    }

    @SuppressWarnings("UnusedReturnValue")
    private StringBuilder replaceAllInnerWithSingleFrom( int i, StringBuilder pSB ) {
        do {
            pSB.setCharAt( i++, mReplacementChar );
            for ( int zDeleteMatchingCharacterAt = i; zDeleteMatchingCharacterAt == (i = findNextIn( pSB, i )); ) {
                pSB.deleteCharAt( i ); // adjacent
            }
        } while ( i != -1 );
        return pSB;
    }
}
