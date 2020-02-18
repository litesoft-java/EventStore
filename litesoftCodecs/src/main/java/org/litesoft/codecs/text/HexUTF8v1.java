package org.litesoft.codecs.text;

import java.util.Arrays;

import org.litesoft.codecs.AbstractCodecs;
import org.litesoft.codecs.RandomIntSource;
import org.litesoft.codecs.UnacceptableEncodingException;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HexUTF8v1 extends HexUTF8 {
    public static final char ENTRY_SEP_CHAR = '.';
    private static final int CEILING = 256;
    private static final int MUTATION4CONSISTENT = makeConsistentMutation( CEILING );
    public static final HexUTF8v1 INSTANCE = new HexUTF8v1( null );
    public static final HexUTF8v1 CONSISTENT = new HexUTF8v1( false, t -> MUTATION4CONSISTENT );

    private final RandomIntSource mIntSource;

    protected HexUTF8v1( boolean pRegister, RandomIntSource pIntSource ) {
        super( pRegister, 1 );
        mIntSource = RandomIntSource.deNull( pIntSource );
    }

    protected HexUTF8v1( RandomIntSource pIntSource ) {
        this( true, pIntSource );
    }

    /**
     * @param pEncodedMethodVersionPrefix !Empty (assumed correct)
     * @param pToEncode                   !null, but could contain nulls or be an empty array
     */
    @Override
    protected ArrayEncoder createArrayEncoder( String pEncodedMethodVersionPrefix, String[] pToEncode ) {
        return new OurArrayEncoder( pEncodedMethodVersionPrefix, pToEncode, mIntSource );
    }

    /**
     * Only called if there are characters after the prefix!
     *
     * @param pDecodeAt points to the first character after the prefix
     * @return !null
     */
    @Override
    protected ArrayDecoder createArrayDecoder( String pSource, int pDecodeAt ) {
        return new OurArrayDecoder( pSource, pDecodeAt, this::validArrayEntryPrefixChar );
    }

    private static class OurArrayEncoder extends AbstractArrayEncoder<String> implements ArrayEncoder {
        private final RandomIntSource mIntSource;

        public OurArrayEncoder( String pEncodedMethodVersionPrefix, String[] pToEncode, RandomIntSource pIntSource ) {
            super( pEncodedMethodVersionPrefix, Arrays.asList( pToEncode ) );
            mIntSource = pIntSource;
        }

        @Override
        protected AbstractCodecs.EntryEncoder<String> createInitialEncoder() {
            return new InitialEncoder();
        }

        private abstract class AbstractEntryEncoder implements EntryEncoder<String> {
            StringBuilder addSep() {
                return mCollector.append( ENTRY_SEP_CHAR );
            }

            boolean addSimple( String pEntry ) {
                if ( pEntry == null ) {
                    addSep().append( CHAR_FOR_NULL );
                    return true;
                }
                if ( pEntry.isEmpty() ) {
                    addSep();
                    return true;
                }
                return false;
            }
        }

        private class InitialEncoder extends AbstractEntryEncoder {
            @Override
            public AbstractEntryEncoder add( String pEntry ) {
                return addSimple( pEntry ) ? this : new WithDataEncoder( pEntry );
            }
        }

        private class WithDataEncoder extends AbstractEntryEncoder {
            private final ByteStreamMutator mMutator;

            WithDataEncoder( String pNonNullOrEmptyEntry ) {
                mMutator = new ByteStreamMutatorImpl( CEILING, (byte)mIntSource.nextInt( CEILING ) );
                addSep();
                addByte( mMutator.getKeyByte() );
                addNonSimple( pNonNullOrEmptyEntry );
            }

            @Override
            public AbstractEntryEncoder add( String pEntry ) {
                if ( addSimple( pEntry ) ) {
                    return this;
                }
                addSep();
                addNonSimple( pEntry );
                return this;
            }

            /**
             * Non-Simple: !null & !empty
             */
            private void addNonSimple( String pEntry ) {
                byte[] zBytes = pEntry.getBytes( UTF8 );
                for ( byte zByte : zBytes ) {
                    addByte( mMutator.encode( zByte ) );
                }
            }

            private void addByte( byte pByte ) {
                appendHex( mCollector, pByte );
            }
        }
    }

    private static class OurArrayDecoder extends AbstractArrayDecoder<String> implements ArrayDecoder {
        public OurArrayDecoder( String pSource, int pToDecodeOffset, ArrayEntryPrefixValidator pPrefixValidator ) {
            super( pSource, pToDecodeOffset, pPrefixValidator );
        }

        @Override
        public String[] decode()
                throws UnacceptableEncodingException {
            return decodeEntries().toArray( EMPTY_ARRAY );
        }

        @Override
        protected EntryDecoder<String> createInitialDecoder() {
            return new InitialDecoder();
        }

        private abstract class AbstractEntryDecoder implements EntryDecoder<String> {
            public final AbstractEntryDecoder decodeEntry( Entry pEntry ) {
                if ( pEntry.isEmpty() ) {
                    mCollector.add( "" );
                    return this;
                }
                if ( pEntry.is( CHAR_FOR_NULL ) ) {
                    mCollector.add( null );
                    return this;
                }
                return decodeContent( pEntry );
            }

            abstract protected AbstractEntryDecoder decodeContent( Entry pEntry );

            protected UnacceptableEncodingException error( Entry pEntry, Integer pReadOffsetAdjust ) {
                return pEntry.error( pReadOffsetAdjust, " not made up of hex value 'pairs'" );
            }
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
                mMutator = new ByteStreamMutatorImpl( CEILING, readByte( pEntry ) );
            }

            private byte readByte( Entry pEntry ) {
                int zHiNibble = fromHex( pEntry.readChar() );
                int zLowNibble = fromHex( pEntry.readChar() );
                int zByte = mergeNibbles( zHiNibble, zLowNibble );
                if ( zByte == -1 ) {
                    throw error( pEntry, -2 );
                }
                return (byte)zByte;
            }

            private void checkPairs( Entry pEntry ) {
                if ( !isEven( pEntry.length() ) ) {
                    throw error( pEntry, null );
                }
            }

            @Override
            protected AbstractEntryDecoder decodeContent( Entry pEntry ) {
                checkPairs( pEntry );
                byte[] zBytes = new byte[pEntry.available() / 2];
                for ( int i = 0; i < zBytes.length; i++ ) {
                    zBytes[i] = mMutator.decode( readByte( pEntry ) );
                }
                String zResult = new String( zBytes, UTF8 );
                mCollector.add( zResult );
                return this;
            }
        }
    }
}
