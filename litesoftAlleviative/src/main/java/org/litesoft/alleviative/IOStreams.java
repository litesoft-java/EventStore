package org.litesoft.alleviative;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IOStreams {
  public static final Charset UTF_8 = StandardCharsets.UTF_8;

  private static final int EOF = -1;
  private static final int SIZE_16K = 1024 * 16;

  public static void copy( InputStream input, OutputStream output )
          throws IOException {
    byte[] buffer = new byte[SIZE_16K];
    for ( int n; EOF != (n = input.read( buffer )); ) {
      output.write( buffer, 0, n );
    }
  }

  public static StringBuilder populateAndClose( StringBuilder sb, InputStream input )
          throws IOException {
    if ( input != null ) {
      InputStreamReader zReader = createReaderUTF8( input );
      try {
        populate( sb, zReader );
      }
      finally {
        Closeables.closeQuietly( zReader );
        Closeables.closeQuietly( input );
      }
    }
    return sb;
  }

  public static StringBuilder populate( StringBuilder sb, InputStreamReader reader )
          throws IOException {
    if ( reader != null ) {
      char[] buffer = new char[SIZE_16K];
      for ( int zReadCount; 0 <= (zReadCount = reader.read( buffer, 0, buffer.length )); ) {
        if ( zReadCount > 0 ) {
          sb.append( buffer, 0, zReadCount );
        }
      }
    }
    return sb;
  }

  public static InputStreamReader createReaderUTF8( InputStream input ) {
    return new InputStreamReader( input, UTF_8 );
  }
}
