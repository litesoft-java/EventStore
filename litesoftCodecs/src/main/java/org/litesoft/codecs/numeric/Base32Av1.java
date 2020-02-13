package org.litesoft.codecs.numeric;

import java.util.Arrays;

import org.litesoft.codecs.AbstractCodecs;
import org.litesoft.codecs.RandomIntSource;
import org.litesoft.codecs.UnacceptableEncodingException;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Base32Av1 extends Base32A {
  public static final char ENTRY_SEP_CHAR = '.';
  private static final int CEILING = 32;
  private static final int MUTATION4CONSISTENT = makeConsistentMutation( CEILING );
  public static final Base32Av1 INSTANCE = new Base32Av1( null );
  public static final Base32Av1 CONSISTENT = new Base32Av1( false, t -> MUTATION4CONSISTENT );

  private final RandomIntSource mIntSource;

  protected Base32Av1( boolean pRegister, RandomIntSource pIntSource ) {
    super( pRegister, 1 );
    mIntSource = RandomIntSource.deNull( pIntSource );
  }

  protected Base32Av1( RandomIntSource pIntSource ) {
    this( true, pIntSource );
  }

  /**
   * @param pEncodedMethodVersionPrefix !Empty (assumed correct)
   * @param pToEncode                   !null, but could contain nulls or be an empty array
   */
  @Override
  protected ArrayEncoder createArrayEncoder( String pEncodedMethodVersionPrefix, Integer[] pToEncode ) {
    return new OurArrayEncoder( pEncodedMethodVersionPrefix, pToEncode, mIntSource );
  }

  /**
   * Only called if there are characters after the prefix!
   *
   * @param pDecodeAt points to the first character after the prefix
   *
   * @return !null
   */
  @Override
  protected ArrayDecoder createArrayDecoder( String pSource, int pDecodeAt ) {
    return new OurArrayDecoder( pSource, pDecodeAt, this::validArrayEntryPrefixChar );
  }

  private static class OurArrayEncoder extends AbstractArrayEncoder<Integer> implements ArrayEncoder {
    private final RandomIntSource mIntSource;

    public OurArrayEncoder( String pEncodedMethodVersionPrefix, Integer[] pToEncode, RandomIntSource pIntSource ) {
      super( pEncodedMethodVersionPrefix, Arrays.asList( pToEncode ) );
      mIntSource = pIntSource;
    }

    @Override
    protected AbstractCodecs.EntryEncoder<Integer> createInitialEncoder() {
      return new InitialEncoder();
    }

    private abstract class AbstractEntryEncoder implements EntryEncoder<Integer> {
      StringBuilder addSep() {
        return mCollector.append( ENTRY_SEP_CHAR );
      }

      boolean addSimple( Integer pEntry ) {
        if ( pEntry == null ) {
          addSep().append( CHAR_FOR_NULL );
          return true;
        }
        int zEntry = pEntry;
        if ( zEntry == NON_ABS_ABLE_NEGATIVE_VALUE ) {
          addSep().append( CHAR_FOR_NON_ABS_ABLE_NEGATIVE_VALUE );
          return true;
        }
        if ( zEntry == 0 ) {
          addSep(); // Empty == 0
          return true;
        }
        return false;
      }
    }

    private class InitialEncoder extends AbstractEntryEncoder {
      @Override
      public AbstractEntryEncoder add( Integer pEntry ) {
        return addSimple( pEntry ) ? this : new WithDataEncoder( pEntry );
      }
    }

    private class WithDataEncoder extends AbstractEntryEncoder {
      private final ByteStreamMutator mMutator;

      WithDataEncoder( Integer pNonNullOrEmptyEntry ) {
        mMutator = new ByteStreamMutatorImpl( CEILING, (byte)mIntSource.nextInt( CEILING ) );
        // mMutator = ByteStreamMutator.NO_OP;
        addSep();
        addByte( mMutator.getKeyByte() );
        addNonSimple( pNonNullOrEmptyEntry );
      }

      @Override
      public AbstractEntryEncoder add( Integer pEntry ) {
        if ( addSimple( pEntry ) ) {
          return this;
        }
        addSep();
        addNonSimple( pEntry );
        return this;
      }

      /**
       * Non-Simple: !null & !0 & !Min_INT
       */
      private void addNonSimple( int pEntry ) {
        if ( pEntry < 0 ) {
          mCollector.append( '-' );
          pEntry = -pEntry;
        }
        while ( pEntry != 0 ) {
          addByte( mMutator.encode( (byte)pEntry ) );
          pEntry = pEntry >>> 5;
        }
      }

      private void addByte( byte pByte ) {
        appendBase32( mCollector, pByte );
      }
    }
  }

  private class OurArrayDecoder extends AbstractArrayDecoder<Integer> implements ArrayDecoder {
    public OurArrayDecoder( String pSource, int pToDecodeOffset, ArrayEntryPrefixValidator pPrefixValidator ) {
      super( pSource, pToDecodeOffset, pPrefixValidator );
    }

    @Override
    public Integer[] decode()
            throws UnacceptableEncodingException {
      return decodeEntries().toArray( EMPTY_ARRAY );
    }

    @Override
    protected EntryDecoder<Integer> createInitialDecoder() {
      return new InitialDecoder();
    }

    private abstract class AbstractEntryDecoder implements EntryDecoder<Integer> {
      public final AbstractEntryDecoder decodeEntry( Entry pEntry ) {
        if ( pEntry.isEmpty() ) {
          mCollector.add( 0 );
          return this;
        }
        if ( pEntry.is( CHAR_FOR_NULL ) ) {
          mCollector.add( null );
          return this;
        }
        if ( pEntry.is( CHAR_FOR_NON_ABS_ABLE_NEGATIVE_VALUE ) ) {
          mCollector.add( NON_ABS_ABLE_NEGATIVE_VALUE );
          return this;
        }
        return decodeContent( pEntry );
      }

      abstract protected AbstractEntryDecoder decodeContent( Entry pEntry );
    }

    private class InitialDecoder extends AbstractEntryDecoder {
      @Override
      protected AbstractEntryDecoder decodeContent( Entry pEntry ) {
        return new WithDataDecoder( pEntry ).decodeContent( pEntry );
      }
    }

    private class WithDataDecoder extends AbstractEntryDecoder {
      private final ByteStreamMutator mMutator;

      WithDataDecoder( Entry pEntry ) {
        byte zMutatorSeedByte = readByte( pEntry ); // Consume
        mMutator = new ByteStreamMutatorImpl( CEILING, zMutatorSeedByte );
        // mMutator = ByteStreamMutator.NO_OP;
      }

      private byte readByte( Entry pEntry ) {
        int z5Bits = fromBase32Char( pEntry.readChar() );
        if ( z5Bits == -1 ) {
          throw pEntry.error( -1, "Not a Base32 character" );
        }
        return (byte)z5Bits;
      }

      @Override
      protected AbstractEntryDecoder decodeContent( Entry pEntry ) {
        boolean zNegative = ('-' == pEntry.peekChar());
        if ( zNegative ) {
          pEntry.readChar(); // Consume the minus sign
        }
        int zAvailable = pEntry.available();
        if ( zAvailable < 1 ) {
          throw pEntry.error( null, "No Base32 characters available" );
        }
        byte[] zBase32s = new byte[zAvailable];
        for ( int i = 0; i < zAvailable; i++ ) {
          zBase32s[i] = mMutator.decode( readByte( pEntry ) );
        }
        int zEntry = 0;
        while ( zAvailable-- > 0 ) {
          zEntry = (zEntry << 5) + zBase32s[zAvailable];
        }
        if ( zNegative ) {
          zEntry = -zEntry;
        }
        mCollector.add( zEntry );
        return this;
      }
    }
  }
}
